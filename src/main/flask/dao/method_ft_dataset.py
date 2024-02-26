#! /usr/bin/python3.11
# -*- coding:utf-8 -*-

"""
Author: zsl
Date: 2024-02-25
Description: Generate the fine-tuning dataset about paper method.
"""

import json

from dao.PaperDataset import PaperDataset


if __name__ == '__main__':

    # 加载数据集
    data = PaperDataset('../data/AI2024', "TI", "WC", "ab_seq")
    ft_dataset = []
    for i in range(0, len(data), 2):
        if (data[i].r_result and data[i].r_result) != '':
            ft_dataset.append({
                "Instruction": "What research methods and findings are presented in the paper '{TI}'?".format(TI=data[i].TI),
                "input": "",
                "Output": "In '{TI}', the researchers used {r_method} and found that {r_result}. "
                          "The study contributes significantly to the field of {WC}.".format(TI=data[i].TI,
                                                                                             r_method=data[i].r_method,
                                                                                             r_result=data[i].r_result,
                                                                                             WC=data[i].WC)
            })
        if (data[i+1].r_result and data[i+1].r_result) != "":
            ft_dataset.append({
                "Instruction": "Provide the main research methods and conclusions of this paper.",
                "input": "'{TI}'".format(TI=data[i].TI),
                "Output": "In '{TI}', the researchers used {r_method} and found that {r_result}. "
                          .format(TI=data[i].TI, r_method=data[i].r_method, r_result=data[i].r_result,)
            })
    # 写出到Json文件
    with open('../data/paper_info_ft_dataset.json', 'w', encoding='utf-8') as file:
        json.dump(ft_dataset, file, ensure_ascii=False, indent=4)


