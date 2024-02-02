import torch
from torch.nn.utils.rnn import pad_sequence
from torch.utils.data import Dataset
from transformers import BertTokenizer


tokenizer = BertTokenizer.from_pretrained("bert-base-uncased")

class SentenceDataset(Dataset):
    def __init__(self, encoded_inputs, labels):
        self.encoded_inputs = encoded_inputs
        self.labels = labels

    def __len__(self):
        return len(self.labels)

    def __getitem__(self, idx):
        item = self.encoded_inputs[idx]
        item['labels'] = self.labels[idx]
        return item




def collate_fn(batch):
    """
    batch: a list, 每个元素都是Dataset.__getitem__的输出
    """
    # 分别处理input_ids和attention_mask，以及labels
    input_ids = [item['input_ids'] for item in batch]
    attention_mask = [item['attention_mask'] for item in batch]
    labels = [item['labels'] for item in batch]

    input_ids_padded = pad_sequence(input_ids, batch_first=True, padding_value=tokenizer.pad_token_id)
    attention_mask_padded = pad_sequence(attention_mask, batch_first=True, padding_value=0)
    labels_padded = pad_sequence(labels, batch_first=True, padding_value=0)  # 使用-1作为填充值

    return {
        'input_ids': input_ids_padded,
        'attention_mask': attention_mask_padded,
        'labels': labels_padded
    }
