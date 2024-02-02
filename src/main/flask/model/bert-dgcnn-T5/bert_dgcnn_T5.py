import torch
from torch import nn
from transformers import BertModel


class BertSentenceEmbedding(nn.Module):
    def __init__(self, bert_model):
        super().__init__()
        self.bert = BertModel.from_pretrained(bert_model)
        for param in self.bert.parameters():
            param.requires_grad = False  # 冻结BERT模型的所有参数

    def forward(self, input_ids, attention_mask):
        # input_ids 和 attention_mask 的形状是 [batch_size, num_sentences, seq_length]
        batch_size, num_sentences, seq_length = input_ids.size()

        # 重塑输入以合并batch和句子维度
        input_ids = input_ids.view(-1, seq_length)
        attention_mask = attention_mask.view(-1, seq_length)

        # 获取BERT的最后一层隐藏状态
        outputs = self.bert(input_ids=input_ids, attention_mask=attention_mask)

        last_hidden_state = outputs.last_hidden_state

        # 对每个句子进行平均池化
        sentence_embeddings = torch.mean(last_hidden_state, dim=1)

        # 将句子embedding重塑 [batch_size, num_sentences, embedding_size]
        sentence_embeddings = sentence_embeddings.view(batch_size, num_sentences, -1)

        return sentence_embeddings


class DGCNN(nn.Module):
    def __init__(self, input_dim, hidden_dim, output_dim, num_classes):
        super(DGCNN, self).__init__()
        # 卷积层设置
        self.conv1 = nn.Conv1d(in_channels=input_dim, out_channels=hidden_dim, kernel_size=3, padding=1)
        self.conv2 = nn.Conv1d(in_channels=hidden_dim, out_channels=hidden_dim, kernel_size=3, padding=1)
        self.conv3 = nn.Conv1d(in_channels=hidden_dim, out_channels=output_dim, kernel_size=3, padding=1)
        self.classifier = nn.Linear(output_dim, num_classes)
        self.sigmoid = nn.Sigmoid()

    def forward(self, x):
        x = self.conv1(x)
        x = torch.relu(x)
        for i in range(4):
            x = self.conv2(x)
            x = torch.relu(x)
        x = self.conv3(x)
        x = torch.relu(x)

        x = x.permute(0, 2, 1)
        x = self.classifier(x)
        x = x.squeeze(-1)  # 移除最后的维度

        x = self.sigmoid(x)

        return x



class BertDGCNNModel(nn.Module):
    def __init__(self, bert_model, dgcnn_input_dim, dgcnn_hidden_dim, dgcnn_output_dim, num_classes):
        super(BertDGCNNModel, self).__init__()
        self.bert_sentence_embedding = BertSentenceEmbedding(bert_model)
        self.dgcnn = DGCNN(dgcnn_input_dim, dgcnn_hidden_dim, dgcnn_output_dim, num_classes)

    def forward(self, input_ids, attention_mask):
        # 获取句子embedding
        sentence_embedding = self.bert_sentence_embedding(input_ids, attention_mask)

        # 调整嵌入的形状以适应DGCNN（需要(batch_size, input_dim, seq_length)）
        sentence_embedding = sentence_embedding.permute(0, 2, 1)

        label_predictions = self.dgcnn(sentence_embedding)

        return label_predictions

