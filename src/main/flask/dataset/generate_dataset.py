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


    def generate_summarize_abstract_ft_dataset(self) -> None:
        """
        Author: zsl
        Date: 2024-02
        """
        ft_dataset = []

        for num, i in enumerate(self.data_loader):
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


    def paper_info_ft_dataset(self) -> None:
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
                num += 1
                if (num + 1) % self.batch_size == 0:
                    self.save_dataset(self.output_dir, "record-{}-{}.json".format(num - self.batch_size + 2, num + 1),
                                      dataset)
                    dataset = []

    def generate_ner_dataset(self, file_path: str) -> None:
        data = []
        results = self.read_json_list_file(file_path)

        for num, result in enumerate(results):
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
                    # find the start
                    for i in range(len(tokenized_sentence) - len(entity_tokens) + 1):
                         if tokenized_sentence[i:i + len(entity_tokens)] == entity_tokens:
                            start_index = i
                            break
                    # tag BIO for the entity
                    if start_index is not None:
                        labels[start_index] = f"B-{entity_type}"
                        for i in range(start_index + 1, start_index + len(entity_tokens)):
                            labels[i] = f"I-{entity_type}"
                data.append({"text": sentence, "labels": labels})
            if num % self.batch_size == 0:
                 file_name = "record-{}-{}.json".format(num-self.batch_size + 2, num)
                 self.save_dataset(self.output_dir, file_name, data)
                 data = []

    @staticmethod
    def save_dataset(output_dir, file_name, data:list | str) -> None:
        with open('{}/{}'.format(output_dir,file_name), 'w', encoding='utf-8') as file:
            json.dump(data, file, ensure_ascii=False, indent=4)
        print("Saved {} records".format(file_name).replace(".json", ""))

    @staticmethod
    def read_json_list_file(file_path: str) -> list:
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

