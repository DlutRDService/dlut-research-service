#! /usr/bin/python3.11
# -*- coding:utf-8 -*-
"""
Author: AI
Date: 2024-02
Description: Generate the dataset about research.
"""


def create_research_ft_dataset(wosdata):
    return  [{
        "Instruction": "Identify the applications of machine learning techniques in optimizing neural network "
                       "performance in the paper titled '{}'.".format(wosdata.TI_name),
        "Input": "",
        "Output": "The paper '{TI_name}' discusses the use of machine learning techniques such as {keywords} to "
                  "optimize neural network performance, focusing on aspects like {specific_aspects}."
    } for wosdata in wosdata]

def create_research(wosdata):
    return  [{
        "Instruction": "Identify the applications of machine learning techniques in optimizing neural network "
                       "performance in the paper titled '{}'.".format(wosdata.TI_name),
        "Input": "",
        "Output": "The paper '{TI_name}' discusses the use of machine learning techniques such as {keywords} "
                  "to optimize neural network performance, focusing on aspects like {specific_aspects}."
    } for wosdata in wosdata]
def cr2(wosdata):
    return [{
        "Instruction": "Identify the applications of machine learning techniques in optimizing neural network "
                       "performance in the paper titled '{}'.".format(
            wosdata.TI_name),
        "Input": "",
        "Output": "The paper '{TI_name}' discusses the use of machine learning techniques such as {keywords} "
                  "to optimize neural network performance, focusing on aspects like {specific_aspects}."
    } for wosdata in wosdata]


if __name__ == "__main__":
    pass
