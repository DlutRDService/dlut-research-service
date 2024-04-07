import torch
from transformers import BertTokenizer
from model.bert_crf.BertBiLSTMCRF import BertBiLSTMCRF

def chat(inputs: str):
    tokenizer = BertTokenizer.from_pretrained('bert-base-uncased')
    ner_model = BertBiLSTMCRF('bert-base-uncased', num_tags=9, lstm_hidden_size=768).to("cuda:0")
    model_path = r'C:\Users\AI\IdeaProjects\dlut-research-service\src\main\flask\model\bert_crf\best_model.pth'
    ner_model.load_state_dict(torch.load(model_path, map_location=torch.device('cuda:0')))

    ner_model.eval()  # 设置为评估模式

    embedding = tokenizer(inputs)
    input_ids = torch.tensor(embedding['input_ids']).unsqueeze(0).to('cuda:0')
    attention_mask = torch.tensor(embedding['attention_mask']).unsqueeze(0).to('cuda:0')

    output = ner_model(input_ids, attention_mask)[0]
    # pop the eos tag

    outputs = output[1:-1]
    tokens = tokenizer.tokenize(inputs)
    # print(outputs)
    # print(tokens)

    new_tokens = []
    new_outputs = []

    i = 0
    while i < len(tokens):
        token = tokens[i]
        output = outputs[i]
        if output != 0:
            if token.startswith("##"):
                new_tokens[-1] += token[2:]
            else:
                new_tokens.append(token)
                new_outputs.append(output)
        elif token.startswith("##") and (outputs[i - 1] != 0):
            new_tokens[-1] += token[2:]
            new_outputs.append(outputs[i - 1])
        i += 1

    entities = []
    i = 0
    while i < len(new_tokens):
        entity = new_tokens[i]
        if new_outputs[i] == 1:
            j = i + 1
            while j < len(new_tokens) and new_outputs[j] == 2:
                entity += " " + new_tokens[j]
                j += 1
            entities.append((entity, "subject"))
            i = j
        elif new_outputs[i] == 3:
            j = i + 1
            while j < len(new_tokens) and new_outputs[j] == 4:
                entity += " " + new_tokens[j]
                j += 1
            entities.append((entity, "author"))
            i = j
        elif new_outputs[i] == 5:
            j = i + 1
            while j < len(new_tokens) and new_outputs[j] == 6:
                entity += " " + new_tokens[j]
                j += 1
            entities.append((entity, "Publication"))
            i = j
        elif new_outputs[i] == 7:
            j = i + 1
            while j < len(new_tokens) and new_outputs[j] == 8:
                entity += " " + new_tokens[j]
                j += 1
            entities.append((entity, "year"))
            i = j
        else:
            i += 1

    print("Extracted Entities:", entities)





print("input: Do you know the innovative research about transformer and Graph Neutral Network in 2021, Zhangshiliang? ")
chat("Do you know the innovative research about transformer and Graph Neutral Network in 2021, Zhang Shiliang?")

