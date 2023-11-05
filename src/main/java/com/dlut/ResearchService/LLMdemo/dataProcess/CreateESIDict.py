# ! /usr/bin/python3.10
# ! -*- coding:utf-8 -*-


import numpy as np
import xlrd


def CreateJournalCategoryDict():
    """
    创建ESI期刊列表索引字典
    :return: 返回一个检索字典
    """
    # 获取索引为index的sheet表格
    table = xlrd.open_workbook(r'./esi-master-journal-list-4-2021.xlsx').sheets()[0]
    fullTitle = np.matrix(table.col_values(0)).tolist()[0]
    Title_2017 = np.matrix(table.col_values(1)).tolist()[0]
    Title_2019 = np.matrix(table.col_values(2)).tolist()[0]
    esiCategory = np.matrix(table.col_values(5)).tolist()[0]
    journalList = [fullTitle, Title_2017, Title_2019, esiCategory]
    esi_dict = {journalList[0][i].upper(): journalList[3][i] for i in range(0, len(journalList[3]))}
    esi_dict1 = {journalList[1][i].upper(): journalList[3][i] for i in range(0, len(journalList[3]))}
    esi_dict2 = {journalList[2][i].upper(): journalList[3][i] for i in range(0, len(journalList[3]))}
    esi_dict1.update(esi_dict2)
    esi_dict.update(esi_dict1)
    return esi_dict
