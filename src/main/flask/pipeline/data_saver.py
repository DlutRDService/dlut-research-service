# -*- coding:utf-8 -*-
import json
from data_loarder import DataLoader

class DataSaver:
    def __init__(self, data_loader: DataLoader, output_dir: str):
        self.data_loader = data_loader
        self.output_dir = output_dir

    def save_data(self) -> None:
        batch = []
        batch_size = 500
        end = len(self.data_loader)
        for idx, data_item in enumerate(self.data_loader, start=1):
            batch.append(data_item.to_dict())
            if idx % batch_size == 0:
                file_name = "record-{}-{}.json".format(idx - batch_size + 1, idx)
                file_path = f"{self.output_dir}{file_name}"
                with open(file_path, "w", encoding="utf-8") as file:
                    json.dump(batch, file, ensure_ascii=False, indent=4)
                batch = []

        if batch:
            file_name = "record-{}-{}.json".format(end - len(batch) + 1, end)
            file_path = f"{self.output_dir}{file_name}"
            with open(file_path, "w", encoding="utf-8") as file:
                json.dump(batch, file, ensure_ascii=False, indent=4)



