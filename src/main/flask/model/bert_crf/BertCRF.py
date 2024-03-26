from torch import nn
from transformers import BertModel
from torchcrf import CRF


class BertCRF(nn.Module):
    def __init__(self, bert_model, num_tags):
        super(BertCRF, self).__init__()
        self.bert = BertModel.from_pretrained(bert_model)
        self.dropout = nn.Dropout(0.3)
        self.classifier = nn.Linear(self.bert.config.hidden_size, num_tags)
        self.crf = CRF(num_tags, batch_first=True)

    def forward(self, input_ids, attention_mask=None, labels=None):
        outputs = self.bert(input_ids, attention_mask=attention_mask)

        sequence_output = self.dropout(outputs[0])
        logits = self.classifier(sequence_output)

        if labels is not None:
            loss = -self.crf(logits, labels, mask=attention_mask.byte(), reduction='mean')
            return loss
        else:
            predictions = self.crf.decode(logits, mask=attention_mask)
            return predictions

