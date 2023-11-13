#! /usr/bin/python3.10
# -*- coding:utf-8 -*-

import os


def get_titles(filepath=None):
    """
    获取路径下的所有文献,或者路径文本
    :param filepath: 论文存储路径
    :return: 返回一个存放所有论文数据的列表
    """
    titles = []
    if filepath is None:
        raise ValueError('Please enter a valid path')
    paths = []
    num = 0
    if os.path.isdir(filepath):
        for (dir_path, dirname, filenames) in os.walk(filepath):
            if filenames is None:
                continue
            for i, filename in enumerate(filenames):
                # 获取当前处理的文件路径
                paths.append(os.path.join(dir_path, filename))
        for path in paths:
            title = split_to_titles(path)
            for i in title:
                num += 1
                if num % 10000 == 0 and num != 0:
                    print('----已经读入{}篇文献数据----'.format(num))
                titles.append(i)
        print("共有{}篇文献".format(len(titles)))
    elif os.path.isfile(filepath):
        title = split_to_titles(filepath)
        for i in title:
            num += 1
            if num % 10000 == 0 and num != 0:
                print('----已经读入{}篇文献数据----'.format(num))
            titles.append(i)
        print("共有{}篇文献".format(len(titles)))
    return titles


def split_to_titles(file_path):
    with open(file_path, "r+", encoding="utf8") as f:
        content = f.read()
        # 切割文本，分割成一篇一篇的文献
        return content.split('\nER\n')

