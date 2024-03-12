#! /usr/bin/python3.11
# -*- coding:utf-8 -*-

"""
Author: zsl
Date: 2023-02-23
Description: A object get the paperInfo from local files, each item is a paper. every item can get the special fileds
inputed
"""

from utils.paper_utils import DealPaperInformation, get_titles

class PaperDataset:
    def __init__(self, file_path, *arg):

        self.titles = get_titles(file_path)
        self.wosdata = [DealPaperInformation(title, *arg) for title in self.titles]

    def __getitem__(self, idx):
        return self.wosdata[idx]

    def __len__(self):
        return len(self.wosdata)


if "__main__" == __name__:
    data = PaperDataset(r"C:\Users\AI\Desktop\data\AI\2021\record-1-500.txt", "TI", "PY", "DE")
