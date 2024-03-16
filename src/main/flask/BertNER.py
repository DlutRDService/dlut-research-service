# -*- coding: gb2312 -*-
import ast
import json

from transformers import BertTokenizer
from datasets import load_dataset


def generate_ner_dataset(file_path:str) -> None:
    data = []
    with open(file_path, "r", encoding="utf-8") as f:
        results = json.load(f)
    for result in results:
        sentence = result['Text']
        try:
            result_list = ast.literal_eval(result['tag'])
        except Exception:
            continue
        tokenizer = BertTokenizer.from_pretrained("bert-base-uncased")
        # 对句子进行分词
        tokenized_sentence = tokenizer.tokenize(sentence)
        labels = ['O'] * len(tokenized_sentence)
        result_list = [item for item in result_list if isinstance(item, tuple)]
        if result_list:
            try :
                for entity, entity_type in result_list:
                    entity_tokens = tokenizer.tokenize(entity)
                    start_index = None
                    # 在分词后的句子中查找实体的开始位置
                    for i in range(len(tokenized_sentence) - len(entity_tokens) + 1):
                        if tokenized_sentence[i:i + len(entity_tokens)] == entity_tokens:
                            start_index = i
                            break
                    # 如果找到实体，为其分配BIO标签
                    if start_index is not None:
                        labels[start_index] = f"B-{entity_type}"
                        for i in range(start_index + 1, start_index + len(entity_tokens)):
                            labels[i] = f"I-{entity_type}"
                data.append({"sentence": sentence, "labels": labels})
            except ValueError:
                continue
    with open("ner_data.json", "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=4)

generate_ner_dataset("result.json")
a = load_dataset("json", data_files="ner_data.json")

def token_func(example):
    return tokenizer(example['sentence'], padding=True, truncation=True, max_length=128)







