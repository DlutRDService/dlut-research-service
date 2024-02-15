"""
Author: AI
Date: 2024-02
Description:
This script generates a summarized dataset by t5_dataset.json
"""

import json

if __name__ == '__main__':
    t5_file_path = '../data/t5_dataset.json'

    with open(t5_file_path, 'r', encoding='utf-8') as file:
        data = json.load(file)

    dataset = [{
        "Instruction": "Summarize this English abstract to about 150-200 words.",
        "Input": i["long_abs"],
        "Output": i["short_abs"]
    } for i in data]

    with open('../data/summarize_abstract_dataset.json', 'w', encoding='utf-8') as f:
        json.dump(dataset, f, ensure_ascii=False, indent=4)

