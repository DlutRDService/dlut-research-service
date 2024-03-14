# -*- coding: gb2312 -*-

from transformers import BertTokenizer

# 假设的实体识别结果
keywords = []
entities = [("directional object detection", "Object"), ("convolutional neural network", "Object")]

# 示例句子
sentence = "A novel directional object detection method for piled objects using a hybrid region-based convolutional neural network."

# 初始化BERT分词器
# tokenizer = BertTokenizer.from_pretrained('dslim/bert-base-NER')
tokenizer = BertTokenizer.from_pretrained("bert-base-uncased")
# 对句子进行分词
tokenized_sentence = tokenizer.tokenize(sentence)
print(tokenized_sentence)
# 创建一个与分词句子等长的列表，初始值为'O'
labels = ['O'] * len(tokenized_sentence)

# 为识别出的实体分配BIO标签
for entity, entity_type in entities:
    entity_tokens = tokenizer.tokenize(entity)
    start_index = None
    # 在分词后的句子中查找实体的开始位置
    for i in range(len(tokenized_sentence) - len(entity_tokens) + 1):
        if tokenized_sentence[i:i+len(entity_tokens)] == entity_tokens:
            start_index = i
            break
    # 如果找到实体，为其分配BIO标签
    if start_index is not None:
        labels[start_index] = f"B-{entity_type}"
        for i in range(start_index + 1, start_index + len(entity_tokens)):
            labels[i] = f"I-{entity_type}"

# print({"":tokenized_sentence, labels)
# 打印结果
for token, label in zip(tokenized_sentence, labels):
    print(f"{token}\t{label}")

