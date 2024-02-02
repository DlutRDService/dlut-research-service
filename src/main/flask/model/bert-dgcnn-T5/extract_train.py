import torch
from torch.optim import AdamW
from transformers import BertTokenizer
from torch.utils.data import DataLoader

from bert_dgcnn_T5 import BertDGCNNModel
from dataset import SentenceDataset, collate_fn

import json

# 加载分词
tokenizer = BertTokenizer.from_pretrained("bert-base-uncased")
model = BertDGCNNModel(bert_model='bert-base-uncased', dgcnn_input_dim=768, dgcnn_hidden_dim=384, dgcnn_output_dim=128, num_classes=1)
model.to("cuda:0")

abstracts = []
# 加载训练数据集
with open("dgcnn_dataset.json", 'r', encoding="utf-8") as json_file:
    abstracts.extend(json.load(json_file))

inputs = []
labels = []
for abstract in abstracts:

    try:
        encoder_input = tokenizer(abstract["ab_seqs"], padding="max_length", max_length=128, truncation=True,
                                  return_tensors="pt")
        label = torch.tensor(eval(abstract["label"])).float()

    except ValueError as e:
        continue

    inputs.append(
        tokenizer(abstract["ab_seqs"], padding="max_length", max_length=128, truncation=True, return_tensors="pt"))
    labels.append(torch.tensor(eval(abstract["label"])).float())

# 创建数据集
dataset = SentenceDataset(inputs, labels)
# 加载数据集
data_loader = DataLoader(dataset, batch_size=32, shuffle=True, collate_fn=collate_fn)

criterion = torch.nn.BCELoss()
optimizer = AdamW(filter(lambda p: p.requires_grad, model.parameters()), lr=5e-5)

model.train()
for epoch in range(50):
    total_loss = 0
    for batch in data_loader:
        optimizer.zero_grad()

        input_ids = batch['input_ids'].to("cuda:0")
        attention_mask = batch['attention_mask'].to("cuda:0")
        labels = batch['labels'].to("cuda:0")

        # 前向传播
        outputs = model(input_ids, attention_mask)
        loss = criterion(outputs, labels).to("cuda:0")

        # 梯度计算
        loss.backward()
        optimizer.step()

        # 累计loss
        total_loss += loss.item()

    print(f"Training loss: {total_loss/len(data_loader)}")
