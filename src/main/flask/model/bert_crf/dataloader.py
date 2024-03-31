from torch.utils.data import Dataset


class NERDataset(Dataset):
    def __init__(self, tokenized_data):
        self.tokenized_data = tokenized_data

    def __len__(self):
        return len(self.tokenized_data)

    def __getitem__(self, idx):
        data_item = self.tokenized_data[idx]

        item = {
            'text': data_item['text'],
            'tags': data_item['tags']
        }
        return item