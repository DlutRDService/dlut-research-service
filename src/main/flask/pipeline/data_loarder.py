#! /usr/bin/python3.11
# -*- coding:utf-8 -*-

"""
Author: zsl
Date: 2023-02-23
Description: A object get the paperInfo from local files, each item is a paper. every item can get the special fileds
inputed
"""
from pipeline.paper_process import get_titles, DealPaperInformation


class DataLoader:

    def __init__(self, file_path, *args):
        self.file_path = file_path
        self.titles = get_titles(file_path)
        self.args = args
        self._cache = {}

    def __getitem__(self, idx):

        if idx in self._cache:
            return self._cache[idx]

        title = self.titles[idx]
        paper = DealPaperInformation(title, *self.args)
        self._cache[idx] = paper
        return paper

    def __len__(self):
        return len(self.titles)

    def __iter__(self):
        self.index = 0
        return self

    def __next__(self):
        if self.index < len(self.titles):
            result = self.__getitem__(self.index)
            self.index += 1
            return result
        else:
            raise StopIteration

