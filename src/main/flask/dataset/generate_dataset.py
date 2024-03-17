#! /usr/bin/python3.11
# -*- coding:utf-8 -*-

"""
Author: zsl
Date: 2024-02-27
"""

import ast
import json

from transformers import BertTokenizer
from pipeline.data_loarder import DataLoader


class GenerateDataset:
    r"""
    Description:
    Generate the deep learning dataset for training.
    Args:
        data_path (string):
        Path to the load WOS formatted paper
        batch_size (int):
        Batch size for generating dataset and output
        **kwargs (str):
        Paper fields like TI,DE u dataset need.
    """

    def __init__(self, data_file, batch_size:int = None, *args):
        self.data_loader = DataLoader(data_file, *args)
        self.batch_size = batch_size if batch_size is not None else 500

    def generate_method_ft_dataset(self):
        """
        Description: Generate the fine-tuning dataset about paper method.
        # fileds["TI", "WC", "AB", "ab_seq"]
        """
        ft_dataset = []
        try:
            for i in range(0, len(self.data_loader), 2):
                if (self.data_loader[i].r_result and self.data_loader[i].r_result) != '':
                    ft_dataset.append({
                        "Instruction": "What research methods and findings are presented in the paper '{TI}'?".format(
                            TI=self.data_loader[i].TI),
                        "input": "",
                        "Output": "In '{TI}', {r_method} and found that {r_result}. "
                                  "The study contributes significantly to the field of {WC}."
                        .format(TI=self.data_loader[i].TI, r_method=self.data_loader[i].r_method,
                                r_result=self.data_loader[i].r_result, WC=self.data_loader[i].WC)
                    })
                if (self.data_loader[i + 1].r_result and self.data_loader[i + 1].r_result) != "":
                    ft_dataset.append({
                        "Instruction": "Provide the main research methods and conclusions of this paper.",
                        "input": "'{TI}'".format(TI=self.data_loader[i].TI),
                        "Output": "In '{TI}', {r_method} and found that {r_result}. "
                        .format(TI=self.data_loader[i].TI, r_method=self.data_loader[i].r_method, r_result=self.data_loader[i].r_result, )
                    })
        except IndexError as e:
            print(e)

    @staticmethod
    def generate_summarize_abstract_ft_dataset():
        """
        Author: AI
        Date: 2024-02
        Description: This script generates a summarized dataset by t5_dataset.json
        """
        t5_file_path = r'C:\Users\AI\Desktop\data\t5_dataset.json'

        with open(t5_file_path, 'r', encoding='utf-8') as file:
            data = json.load(file)

        dataset = [{
            "Instruction": "Summarize this English abstract to about 150-200 words.",
            "Input": i["long_abs"],
            "Output": i["short_abs"]
        } for i in data]

        with open(r'C:\Users\AI\Desktop\data\summarize_abstract_dataset.json', 'w', encoding='utf-8') as f:
            json.dump(dataset, f, ensure_ascii=False, indent=4)

    def paper_info_ft_dataset(self):
        """
        Author: zsl
        Date: 2024-02-25
        Description: Generate the fine-tuning dataset about introducing paper info.
        fileds["TI", "SE", "DE", "WC", "AF", "PY", "AB"]
        """
        dataset = []
        for i in self.data_loader:
            dataset.append({
                "instruction": "Introduce the paper '{TI}'.".format(TI=i.TI),
                "input": "",
                "output": "This paper '{TI}' was published in {PY} in the {SE}, authored by {AU} and others. "
                          "It falls within the {WC} field in WOS database. "
                          "{AB}".format(SE="and ".join(i.SE), AU="; ".join(i.AU[0:2]), AB=i.AB, DE=", ".join(i.DE),
                                        PY=i.PY,
                                        TI=i.TI,
                                        WC="and ".join(i.WC))
            })


    def generate_word_seq_dataset(self) -> None:
        dataset = []
        num:int = 0
        for i in self.data_loader:
            if not i.DE:
                continue
            au_tag = [(j, "author") for j in i.AU if i.AU]
            so_tag = [(i.SO, "Academic Publications") if i.SO else ""]
            de_tag = [(j, "object") for j in i.DE if i.DE]
            '''
            # gpt generate
            messages=[
                    {"role": "system",
                     "content": "You are a AI model. You need to understand a piece of text and based on this text"
                                "to tag the words in this phrase list as 'method' or 'object'."
                                "You only need to return a list like [(word, tag)]."},
                    {"role": "user",
                     "content": "Text:{}, List:{}".format(i.TI, i.DE)},
            ]
            result = gpt_api(messages).replace("\n", "")
            '''
            # without gpt
            if de_tag and au_tag and so_tag:
                dataset.append({"title": i.TI, "de_tag": str(de_tag), "au_tag": str(au_tag), "so_tag":str(so_tag)})
                num += 1
                if (num + 1) % self.batch_size == 0:
                    self.save_dataset("record-{}-{}.json".format(num - self.batch_size + 2, num + 1), dataset)
                    dataset = []

    @staticmethod
    def generate_ner_dataset(file_path: str) -> None:
        data = []
        with open(file_path, "r", encoding="utf-8") as f:
            results = json.load(f)
        for result in results:
            sentence = result['Text']
            result_list = ast.literal_eval(result['tag'])
            tokenizer = BertTokenizer.from_pretrained("bert-base-uncased")
            # 对句子进行分词
            tokenized_sentence = tokenizer.tokenize(sentence)
            labels = ['O'] * len(tokenized_sentence)
            result_list = [item for item in result_list if isinstance(item, tuple)]
            if result_list:
                try:
                    for entity, entity_type in result_list:
                        entity_tokens = tokenizer.tokenize(entity)
                        start_index = None
                        # 在分词后的句子中查找实体的开始位置
                        for i in range(len(tokenized_sentence) - len(entity_tokens) + 1):
                            if tokenized_sentence[i:i + len(entity_tokens)] == entity_tokens:
                                start_index = i
                                break
                        # 如果找到实体，为其分配BIO标签
                        if start_index is not None:
                            labels[start_index] = f"B-{entity_type}"
                            for i in range(start_index + 1, start_index + len(entity_tokens)):
                                labels[i] = f"I-{entity_type}"
                    data.append({"sentence": sentence, "labels": labels})
                except ValueError:
                    continue
        with open("ner_data.json", "w", encoding="utf-8") as f:
            json.dump(data, f, ensure_ascii=False, indent=4)

    @staticmethod
    def save_dataset(file_path, data:list | str) -> None:
        with open('../data/{}'.format(file_path), 'w', encoding='utf-8') as file:
            json.dump(data, file, ensure_ascii=False, indent=4)
        print("Saved {} records".format(file_path).replace(".json", ""))


