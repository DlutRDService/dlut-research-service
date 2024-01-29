#! /usr/bin/python3.11
# -*- coding:utf-8 -*-

class ResearchFtDataset:
    def __init__(self, wosdatas):
        self.wosdatas = wosdatas

    def __getitem__(self, idx):
        return self.wosdatas[idx]

    def create_research_ft_dataset(self):
        return  [{
            "Instruction": "Identify the applications of machine learning techniques in optimizing neural network performance in the paper titled '{}'.".format(wosdata.TI_name),
            "Input": "",
            "Output": "The paper '{TI_name}' discusses the use of machine learning techniques such as {keywords} to optimize neural network performance, focusing on aspects like {specific_aspects}."
        } for wosdata in self.wosdatas]

    def create_research(self):
        return  [{
            "Instruction": "Identify the applications of machine learning techniques in optimizing neural network performance in the paper titled '{}'.".format(wosdata.TI_name),
            "Input": "",
            "Output": "The paper '{TI_name}' discusses the use of machine learning techniques such as {keywords} to optimize neural network performance, focusing on aspects like {specific_aspects}."
        } for wosdata in self.wosdatas]
    def cr2(self):
        return [{
            "Instruction": "Identify the applications of machine learning techniques in optimizing neural network performance in the paper titled '{}'.".format(
                wosdata.TI_name),
            "Input": "",
            "Output": "The paper '{TI_name}' discusses the use of machine learning techniques such as {keywords} to optimize neural network performance, focusing on aspects like {specific_aspects}."
        } for wosdata in self.wosdatas]
