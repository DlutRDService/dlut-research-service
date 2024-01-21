import torch.nn as nn
from transformers import RobertaModel, RobertaConfig
from torch_geometric.nn.conv import GATConv


config = RobertaConfig.from_pretrained('roberta-base')


class GATConvWithAttention(GATConv):
    def forward(self, x, edge_index, edge_attr=None, size=None, return_attention_weights=True):
        # 修改GAT，输出节点注意力权重
        out, attention_weights = super().forward(x, edge_index, edge_attr, size, return_attention_weights)
        return out, attention_weights


class RobertaPartialEncoder(nn.Module):
    def __init__(self, roberta_model_name, start_layer, end_layer):
        super().__init__()
        # roberta模型分层
        self.roberta = RobertaModel.from_pretrained(roberta_model_name, config=config)
        self.encoder_layers = self.roberta.encoder.layer[start_layer:end_layer]

    def forward(self, hidden_states, attention_mask):
        # 修改掩码维度
        extended_attention_mask = attention_mask[:, None, None, :]
        extended_attention_mask = extended_attention_mask.to(dtype=hidden_states.dtype)
        extended_attention_mask = (1.0 - extended_attention_mask) * -10000.0

        # 计算每一层输出
        for layer in self.encoder_layers:
            layer_outputs = layer(hidden_states, attention_mask=extended_attention_mask)
            hidden_states = layer_outputs[0]

        return hidden_states


class RobertaGAT(nn.Module):
    def __init__(self, roberta_model_name, num_classes):
        super(RobertaGAT, self).__init__()
        # 加载Roberta模型
        self.roberta = RobertaModel.from_pretrained(roberta_model_name, config=config)
        # roberta分层
        self.roberta_embeddings = self.roberta.embeddings
        self.roberta_first_half = RobertaPartialEncoder('roberta-base', 0, 6)
        self.roberta_second_half = RobertaPartialEncoder('roberta-base', 6, 12)
        # 加载GAT模型
        self.gat = GATConvWithAttention(self.roberta.config.hidden_size, num_classes)

    def forward(self, input_ids, attention_mask, edge_index):
        # 编码
        outputs = self.roberta_embeddings(input_ids=input_ids)

        # 通过Roberta第一层网络
        outputs = self.roberta_first_half(outputs, attention_mask=attention_mask)

        # 将中间输出移动到 GPU 1 并进行后续处理
        outputs = outputs
        attention_mask = attention_mask
        outputs = self.roberta_second_half(outputs, attention_mask=attention_mask)

        # 将roberta输出结果转移到GPU 1
        sentence_embeddings = outputs[:, 0, :]

        # 输入GAT，得到输出结果
        gat_output, attention_weights = self.gat(sentence_embeddings, edge_index)
        return gat_output, attention_weights


#
# import torch.nn as nn
# from transformers import RobertaModel, RobertaConfig
# from torch_geometric.nn.conv import GATConv
#
#
# config = RobertaConfig.from_pretrained('roberta-base')
#
#
# class GATConvWithAttention(GATConv):
#     def forward(self, x, edge_index, edge_attr=None, size=None, return_attention_weights=True):
#         # 修改GAT，输出节点注意力权重
#         out, attention_weights = super().forward(x, edge_index, edge_attr, size, return_attention_weights)
#         return out, attention_weights
#
#
# class RobertaPartialEncoder(nn.Module):
#     def __init__(self, roberta_model_name, start_layer, end_layer):
#         super().__init__()
#         # roberta模型分层
#         self.roberta = RobertaModel.from_pretrained(roberta_model_name, config=config)
#         self.encoder_layers = self.roberta.encoder.layer[start_layer:end_layer]
#
#     def forward(self, hidden_states, attention_mask):
#         # 修改掩码维度
#         extended_attention_mask = attention_mask[:, None, None, :]
#         extended_attention_mask = extended_attention_mask.to(dtype=hidden_states.dtype)
#         extended_attention_mask = (1.0 - extended_attention_mask) * -10000.0
#
#         # 计算每一层输出
#         for layer in self.encoder_layers:
#             layer_outputs = layer(hidden_states, attention_mask=extended_attention_mask)
#             hidden_states = layer_outputs[0]
#
#         return hidden_states
#
#
# class RobertaGAT(nn.Module):
#     def __init__(self, roberta_model_name, num_classes):
#         super(RobertaGAT, self).__init__()
#         # 加载Roberta模型
#         self.roberta = RobertaModel.from_pretrained(roberta_model_name, config=config)
#         # roberta分层
#         self.roberta_embeddings = self.roberta.embeddings.to('cuda:0')
#         self.roberta_first_half = RobertaPartialEncoder('roberta-base', 0, 6).to('cuda:0')
#         self.roberta_second_half = RobertaPartialEncoder('roberta-base', 6, 12).to('cuda:1')
#         # 加载GAT模型
#         self.gat = GATConvWithAttention(self.roberta.config.hidden_size, num_classes).to('cuda:1')
#
#     def forward(self, input_ids, attention_mask, edge_index):
#         # 编码
#         outputs = self.roberta_embeddings(input_ids=input_ids)
#
#         # 通过Roberta第一层网络
#         outputs = self.roberta_first_half(outputs, attention_mask=attention_mask)
#
#         # 将中间输出移动到 GPU 1 并进行后续处理
#         outputs = outputs.to('cuda:1')
#         attention_mask = attention_mask.to('cuda:1')
#         outputs = self.roberta_second_half(outputs, attention_mask=attention_mask)
#
#         # 将roberta输出结果转移到GPU 1
#         sentence_embeddings = outputs[:, 0, :].to('cuda:1')
#
#         # 输入GAT，得到输出结果
#         gat_output, attention_weights = self.gat(sentence_embeddings, edge_index)
#         return gat_output, attention_weights
