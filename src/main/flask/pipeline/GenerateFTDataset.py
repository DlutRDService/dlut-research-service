#! /usr/bin/python3.11
# -*- coding:utf-8 -*-

"""
Author: zsl
Date: 2024-02-27
Description: Generate the fine-tuning dataset.
"""
import json

from pipeline.PaperInfo import PaperDataset


class GenerateFTDataset:

    def __init__(self, data_file):
        self.data_file = data_file
        self.output_path = None
        self.data = None

    def generate_method_ft_dataset(self, output_path=None, *args):
        """
        Author: zsl
        Date: 2024-02-25
        Description: Generate the fine-tuning dataset about paper method.
        """

        self.data = PaperDataset(self.data_file, *args)
        self.output_path = output_path
        # data = PaperDataset(r'C:\Users\AI\Desktop\data\AI\2024', "TI", "WC", "AB", "ab_seq")

        ft_dataset = []
        try:
            for i in range(0, len(self.data), 2):
                if (self.data[i].r_result and self.data[i].r_result) != '':
                    ft_dataset.append({
                        "Instruction": "What research methods and findings are presented in the paper '{TI}'?".format(
                            TI=self.data[i].TI),
                        "input": "",
                        "Output": "In '{TI}', {r_method} and found that {r_result}. "
                                  "The study contributes significantly to the field of {WC}."
                        .format(TI=self.data[i].TI, r_method=self.data[i].r_method,
                                r_result=self.data[i].r_result, WC=self.data[i].WC)
                    })
                if (self.data[i + 1].r_result and self.data[i + 1].r_result) != "":
                    ft_dataset.append({
                        "Instruction": "Provide the main research methods and conclusions of this paper.",
                        "input": "'{TI}'".format(TI=self.data[i].TI),
                        "Output": "In '{TI}', {r_method} and found that {r_result}. "
                        .format(TI=self.data[i].TI, r_method=self.data[i].r_method, r_result=self.data[i].r_result, )
                    })
        except IndexError as e:
            print(e)
        # 写出到Json文件
        with open(self.output_path, 'w', encoding='utf-8') as file:
            json.dump(ft_dataset, file, ensure_ascii=False, indent=4)

    @staticmethod
    def generate_summarize_abstract_ft_dataset(self):
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

    def paper_info_ft_dataset(self, output_path=None, *args):
        """
        Author: zsl
        Date: 2024-02-25
        Description: Generate the fine-tuning dataset about introducing paper info.
        """

        self.data = PaperDataset(self.data_file, *args)
        self.output_path = output_path
        # data = PaperDataset(r'C:\Users\AI\Desktop\data\conference2023', "TI", "SE", "DE", "WC", "AF", "PY", "AB")

        dataset = [{
            "instruction": "Introduce the paper '{TI}'.".format(TI=i.TI),
            "input": "",
            "output": "This paper '{TI}' was published in {PY} in the conference {SE}, authored by {AU} and others. "
                      "It falls within the {WC} field in WOS database. "
                      "{AB}".format(SE="and ".join(i.SE), AU="; ".join(i.AU[0:2]), AB=i.AB, DE=", ".join(i.DE), PY=i.PY,
                                    TI=i.TI,
                                    WC="and ".join(i.WC))
        } for i in self.data]

        with open(self.output_path, 'w', encoding='utf-8') as file:
            json.dump(dataset, file, ensure_ascii=False, indent=4)

# def create_research_ft_dataset(wosdata):
#     return  [{
#         "Instruction": "Identify the applications of machine learning techniques in optimizing neural network "
#                        "performance in the paper titled '{}'.".format(wosdata.TI_name),
#         "Input": "",
#         "Output": "The paper '{TI_name}' discusses the use of machine learning techniques such as {keywords} to "
#                   "optimize neural network performance, focusing on aspects like {specific_aspects}."
#     } for wosdata in wosdata]
#
# def create_research(wosdata):
#     return  [{
#         "Instruction": "Identify the applications of machine learning techniques in optimizing neural network "
#                        "performance in the paper titled '{}'.".format(wosdata.TI_name),
#         "Input": "",
#         "Output": "The paper '{TI_name}' discusses the use of machine learning techniques such as {keywords} "
#                   "to optimize neural network performance, focusing on aspects like {specific_aspects}."
#     } for wosdata in wosdata]
# def cr2(wosdata):
#     return [{
#         "Instruction": "Identify the applications of machine learning techniques in optimizing neural network "
#                        "performance in the paper titled '{}'.".format(
#             wosdata.TI_name),
#         "Input": "",
#         "Output": "The paper '{TI_name}' discusses the use of machine learning techniques such as {keywords} "
#                   "to optimize neural network performance, focusing on aspects like {specific_aspects}."
#     } for wosdata in wosdata]
if __name__ == '__main__':
    dataset = GenerateFTDataset()
    dataset.generate_method_ft_dataset(r'C:\Users\AI\Desktop\data\AI\2024',
                                       r'C:\Users\AI\Desktop\data\method_ft_dataset.json',
                                       "TI", "WC", "AB", "ab_seq"
                                       )
