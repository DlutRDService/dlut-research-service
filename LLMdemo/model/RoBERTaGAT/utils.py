import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np
import csv
import os
from llama_cpp import Llama
from transformers import RobertaTokenizer


# llama = Llama(model_path=r'./llama-2-7b.Q4_K_M.gguf',
#               embedding=True,
#               n_ctx=4096,
#               )


def get_sentence_rel(path):
    """
    以文章为单位，构建关系（abs_sentence-title）
    :param path:
    :return:
    """
    flag = 0
    df = pd.read_csv(path, encoding="utf-8")
    rel = []
    rels = []
    for i in range(0, len(df['label'])):
        if df['label'][i] == 4:
            rel = []
            flag = i
            continue
        rel.append([i, flag])
        if i+1 == len(df['label']):
            rels.append(rel)
            break
        if df['label'][i] != 4 and df['label'][i + 1] != 4:
            rel.append([i, i + 1])
            continue
        else:
            rels.append(rel)
    return rels


def get_paper_rel(array, num):
    """
    获取文章直接的关系（title-title）
    :param array:
    :return:
    """
    rels = []
    for i in range(0, len(array)):
        for j in range(i + 1, len(array)):
            cos = cos_sim(array[i], array[j])
            if cos >= 0.93:
                rels.append([num + i, num + j])
    return rels


def get_edge_index(sen_rel, abs_rel):
    """
    按节点，构建图关系
    """
    df = pd.read_csv('data/test.csv')
    rels = []
    for i in range(len(df['label'])):
        rel = []
        for j in (sen_rel + abs_rel):
            if i in j:
                rel.append(j)
        rels.append(rel)
    return rels


def save_embedding(index, abstract, name):
    abstract_embedding = llama.create_embedding(input=abstract).get('data')[0].get('embedding')
    abstract_embedding = np.array(abstract_embedding)
    embedding_file = f"abstract_embedding{index}.npy"
    np.save(os.path.join("", "temp", embedding_file), abstract_embedding)

    with open(os.path.join('', 'data', f'abstract_embedding_{name}.csv'), 'a', newline='',
              encoding='utf-8') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow([abstract, embedding_file])


def get_abstract_embedding(path, start, name):
    """
    Llama编码获取摘要embedding。处理结果为[[][]]
    :param name:
    :param path:
    :param start:
    """
    df = pd.read_csv(path, encoding='GB2312')
    abstract = ''
    for i in range(start, len(df['label'])):
        if df['label'][i] != 4:
            abstract += df['text'][i]
        if i+1 == len(df['label']) or (df['label'][i] == 3 and df['label'][i + 1] == 4):
            abstract_embedding = llama.create_embedding(input=abstract).get('data')[0].get('embedding')
            abstract_embedding = np.array(abstract_embedding)
            np.save(f"../temp/abstract_embedding{i}.npy", abstract_embedding)
            with open(f'../data/abstract_embedding_{name}.csv', 'a', newline='', encoding='utf-8') as csvfile:
                writer = csv.writer(csvfile)
                writer.writerow([abstract, f'abstract_embedding{i}.npy'])
            abstract = ''
    tmp = []
    files = os.listdir("temp", )
    # 获取每个文件的完整路径
    full_paths = [os.path.join("temp", file) for file in files]
    # 按创建时间对文件进行排序
    sorted_files = sorted(full_paths, key=os.path.getctime)
    for file in sorted_files:
        if file.endswith('.npy'):
            tmp.append(np.load(f'{file}', allow_pickle=True))
    np.array(tmp)
    np.save(f'../data/abstract_embedding_{name}.npy', tmp)


def cos_sim(a, b):
    return cosine_similarity([a, b])[0][1]


def spilt_node(test_data):
    test_data_split = []
    flag = 0
    for index in range(len(test_data)):
        if index + 1 == len(test_data) or (test_data[index]['label'] == 3 and test_data[index+1]['label'] == 4):
            test_data_split.append(test_data[flag:index+1])
            flag = index + 1
    return test_data_split


def encode_batch(abstract):
    tokenizer = RobertaTokenizer.from_pretrained("roberta-base")
    return tokenizer(abstract, padding='max_length', truncation=True, max_length=96, return_tensors="pt")


