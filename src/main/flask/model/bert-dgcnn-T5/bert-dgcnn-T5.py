import torch
from torch import nn
from transformers import BertModel


class BertSentenceEmbedding(nn.Module):
    def __init__(self, bert_model):
        super().__init__()
        self.bert = BertModel.from_pretrained(bert_model)

        # 冻结BERT层
        for param in self.bert.parameters():
            param.requires_grad = False

    def forward(self, input_ids, attention_mask):
        # 获取BERT的最后一层隐藏状态
        with torch.no_grad():
            outputs = self.bert(input_ids=input_ids, attention_mask=attention_mask)

        last_hidden_state = outputs.last_hidden_state

        # 平均池化
        avg_pool = torch.mean(last_hidden_state, dim=1)
        return avg_pool


class DGCNN(nn.Module):
    def __init__(self, input_dim, hidden_dim, output_dim, num_classes):
        super(DGCNN, self).__init__()
        # 一维卷积层
        self.conv1 = nn.Conv1d(in_channels=input_dim, out_channels=hidden_dim, kernel_size=3, padding=1)
        self.conv2 = nn.Conv1d(in_channels=hidden_dim, out_channels=output_dim, kernel_size=3, padding=1)
        # 线性层输出类别预测
        self.classifier = nn.Linear(output_dim, num_classes)

    def forward(self, x):
        x = self.conv1(x)
        x = torch.relu(x)
        x = self.conv2(x)
        x = torch.relu(x)
        # 假设x的形状是(batch_size, output_dim, seq_length)，我们对最后一个维度进行池化
        x = torch.mean(x, dim=2)
        # 应用线性层进行分类
        x = self.classifier(x)
        return x



class BertDGCNNModel(nn.Module):
    def __init__(self, bert_model, dgcnn_input_dim, dgcnn_hidden_dim, dgcnn_output_dim, num_classes):
        super(BertDGCNNModel, self).__init__()
        self.bert_sentence_embedding = BertSentenceEmbedding(bert_model)
        self.dgcnn = DGCNN(dgcnn_input_dim, dgcnn_hidden_dim, dgcnn_output_dim, num_classes)

    def forward(self, input_ids, attention_mask):
        # 获取句子嵌入
        sentence_embedding = self.bert_sentence_embedding(input_ids, attention_mask)
        # 调整嵌入的形状以适应DGCNN（需要(batch_size, input_dim, seq_length)）
        sentence_embedding = sentence_embedding.permute(0, 2, 1)
        # 获取标签预测
        label_predictions = self.dgcnn(sentence_embedding)
        return label_predictions

