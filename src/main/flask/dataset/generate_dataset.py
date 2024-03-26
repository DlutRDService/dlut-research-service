#! /usr/bin/python3.11
# -*- coding:utf-8 -*-

"""
Author: zsl
Date: 2024-02-27
"""

import ast
import json
import os

from transformers import BertTokenizer

from config.gpt_config import gpt_api
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

    def __init__(self, data_file, output_dir, batch_size:int = None, *args):
        self.data_loader = DataLoader(data_file, *args)
        self.batch_size = batch_size if batch_size is not None else 500
        self.output_dir = output_dir
        self.exit_file_num = len(os.listdir(output_dir))

    def generate_method_ft_dataset(self) -> None:
        """
        Description: Generate the fine-tuning dataset about paper method.
        # fileds["TI", "WC", "AB", "ab_seq"]
        """
        ft_dataset = []
        try:
            for i in range(0, len(self.data_loader), 2):
                if (self.data_loader[i].r_result and self.data_loader[i].r_result) != '':
                    ft_dataset.append({
                        "instruction": "What research methods and findings are presented in the paper '{TI}'?".format(
                            TI=self.data_loader[i].TI),
                        "input": "",
                        "output": "In '{TI}', {r_method} and found that {r_result}. "
                                  "The study contributes significantly to the field of {WC}."
                        .format(TI=self.data_loader[i].TI, r_method=self.data_loader[i].r_method,
                                r_result=self.data_loader[i].r_result, WC=self.data_loader[i].WC)
                    })
                if (self.data_loader[i + 1].r_result and self.data_loader[i + 1].r_result) != "":
                    ft_dataset.append({
                        "instruction": "Provide the main research methods and conclusions of this paper.",
                        "input": "'{TI}'".format(TI=self.data_loader[i].TI),
                        "output": "In '{TI}', {r_method} and found that {r_result}. "
                        .format(TI=self.data_loader[i].TI, r_method=self.data_loader[i].r_method, r_result=self.data_loader[i].r_result, )
                    })
        except IndexError:
            pass

    def generate_summarize_topic_ft_dataset(self) -> None:
        """
         Generate the fine-tuning dataset about paper method by gpt.
         # fileds["TI", "AB"]
        """
        ft_dataset = []
        for num, i in enumerate(self.data_loader):
            messages = [
                {"role": "system",
                 "content": "You are an intelligent robot specializing in summarizing topic. I will provide you with a "
                            " text, and your summarize the research topic"},
                {"role": "user", "content": "Summarize the theme of the paper in one sentence. This is the paper "
                                            "content: " + i.AB}
            ]
            result = gpt_api(messages)
            if result is not None:
                ft_dataset.append({
                    "instruction": "You are an intelligent robot specializing in summarizing topic. "
                                   "I will provide you with a text, and your summarize the research topic",
                    "input": "Summarize the theme of the paper in one sentence. This is the paper "
                                            "content: " + i.AB,
                    "output": result
                })
            if (num + 1) % self.batch_size == 0:
                file_name = "summarize_topic_record-{}-{}.json".format(num - self.batch_size + 2, num)
                self.save_dataset(self.output_dir, file_name, ft_dataset)
                ft_dataset = []


    def generate_summarize_abstract_ft_dataset(self) -> None:
        """
        Author: zsl
        Date: 2024-02
        """
        ft_dataset = []

        for num, i in enumerate(self.data_loader):
            if not i.AB:
                continue
            messages = [
                {"role": "system",
                 "content": "You are an intelligent robot specializing in summarizing and condensing text. I will provide you with a summary text, and your task is to condense it to around 200 words."},
                {"role": "user", "content": i.AB}
            ]

            result = gpt_api(messages)
            ft_dataset.append({
                "instruction": "You are an intelligent robot specializing in summarizing and condensing text. I will "
                               "provide you with a summary text, and your task is to condense it to around 200 words.",
                "input": i.AB,
                "output": result
            })
            if (num + 1) % self.batch_size == 0:
                self.save_dataset(self.output_dir,
                                  "sum_AB-{}-{}.json".format(num - self.batch_size + 2, num + 1),
                                  ft_dataset)
                ft_dataset = []

    def generate_paper_info_ft_dataset(self) -> None:
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

    def generate_dataset(self) -> None:
        ft_dataset = []
        for i in self.data_loader:
            ft_dataset.append({
                "instruction": "You are an AI assistant. Provide a detailed answer so user don’t need to search outside to understand the answer.",
                "input": "I want to learn about recent research related to {} and {}".format(i.AB[0],i.DE[1]),
                "output": "Of course, research on large models and GNN is a popular field. At {} in {}, "
                          "a paper '{}' show that {}.".format(i.SO, i.SE, i.TI, i.AB)
            })


    def generate_word_seq_dataset(self) -> None:
        dataset = []
        for num, i in enumerate(self.data_loader, start=1):
            if not i.DE:
                continue
            au_tag = [(j, "author") for j in i.AU if i.AU]
            so_tag = [(i.SO, "Academic Publications") if i.SO else ""]
            de_tag = [(j, "object") for j in i.DE if i.DE]
            py_tag = [(i.PY, "Year") if i.PY else ""]
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
            if de_tag and au_tag and so_tag and py_tag:
                i.TI = "Paper '{}' authored by {} and published on {} in {}".format(
                    i.TI, ", ".join(i.AU), i.SO, i.PY
                )
                dataset.append({"title": i.TI,
                                "de_tag": str(de_tag),
                                "au_tag": str(au_tag),
                                "so_tag": str(so_tag),
                                "py_tag": str(py_tag)
                                })
                if num % self.batch_size == 0:
                    self.save_dataset(self.output_dir, "record-{}-{}.json".format(num - self.batch_size + 1, num),
                                      dataset)
                    dataset = []

    def ner_ins_to_seq(self, file_path: str) -> None:
        data = []
        results = self.read_file(file_path)

        for num, result in enumerate(results, start=1):
            sentence = result['title']
            de_list = ast.literal_eval(result['de_tag'])
            au_list = ast.literal_eval(result['au_tag'])
            so_list = ast.literal_eval(result['so_tag'])
            py_list = ast.literal_eval(result['py_tag'])
            tags = de_list + au_list + so_list + py_list

            # token
            tokenizer = BertTokenizer.from_pretrained("bert-base-uncased")
            tokenized_sentence = tokenizer.tokenize(sentence)
            labels = ['O'] * len(tokenized_sentence)
            if tags:
                for entity, entity_type in tags:
                    entity_tokens = tokenizer.tokenize(entity)
                    start_index = None
                    # find the start BIO position
                    for i in range(len(tokenized_sentence) - len(entity_tokens) + 1):
                         if tokenized_sentence[i:i + len(entity_tokens)] == entity_tokens:
                            start_index = i
                            break
                    # tag BIO for the entity
                    if start_index is not None:
                        labels[start_index] = f"B-{entity_type}"
                        for i in range(start_index + 1, start_index + len(entity_tokens)):
                            labels[i] = f"I-{entity_type}"
                data.append({"text": sentence, "tags": labels})
            if num % self.batch_size == 0:
                 file_name = "record-{}-{}.json".format(num-self.batch_size + 1 + self.exit_file_num * self.batch_size,
                                                        num + self.exit_file_num * self.batch_size)
                 self.save_dataset(self.output_dir, file_name, data)
                 data = []

    def ner_ins_to_seq1(self, file_path:str):
        all_results = []
        tokenizer = BertTokenizer.from_pretrained("bert-base-uncased")
        for filename in os.listdir(file_path):
            full_path = os.path.join(file_path, filename)
            if os.path.isfile(full_path):
                with open(full_path, "r", encoding="utf-8") as f:
                    results = json.load(f)
                    for i in results:
                        if len(tokenizer.tokenize(i["text"])) < 511:
                            tag_to_id = {
                                "O": 0,
                                "B-object": 1,
                                "I-object": 2,
                                "B-author": 3,
                                "I-author": 4,
                                "B-Academic Publications": 5,
                                "I-Academic Publications": 6,
                                "B-Year": 7,
                            }
                            tags_ids = [tag_to_id[tag] for tag in i['tags']]
                            i['tags'] = tags_ids
                            all_results.append(i)
        self.save_dataset()
        with open('../data/ner1/demo.json', "w", encoding="utf-8") as f:
            json.dump(all_results, f, ensure_ascii=False, indent=4)

    @staticmethod
    def save_dataset(output_dir, file_name, data:list | str) -> None:
        with open('{}/{}'.format(output_dir,file_name), 'w', encoding='utf-8') as file:
            json.dump(data, file, ensure_ascii=False, indent=4)
        print("Saved {} records".format(file_name).replace(".json", ""))

    @staticmethod
    def read_file(file_path: str) -> list:
        all_results = []
        if os.path.isdir(file_path):
            for filename in os.listdir(file_path):
                full_path = os.path.join(file_path, filename)
                if os.path.isfile(full_path):
                    with open(full_path, "r", encoding="utf-8") as f:
                        results = json.load(f)
                        all_results.extend(results)
        elif os.path.isfile(file_path):
            with open(file_path, "r", encoding="utf-8") as f:
                all_results = json.load(f)
        return all_results




if __name__ == "__main__":
    a = GenerateDataset(r"C:\Users\AI\Desktop\data\AI\2010-2017", "../data/ner1", 1000)
    a.ner_ins_to_seq("../data/demo")
    # a.demo("../data/ner_2018")