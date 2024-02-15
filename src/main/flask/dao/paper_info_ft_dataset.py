"""
Author: AI
Date: 2024-02
Description: Generate the dataset about introducing paper info.
"""

import json

from dao.Dataset import Dataset


if __name__ == '__main__':
    data = Dataset('../data/conference2023', "TI", "SE", "DE", "WC", "AF", "PY")

    dataset = [{
        "instruction": "Introduce the paper '{TI}'.".format(TI=i.TI),
        "input": "",
        "output": "This paper '{TI}' was published in {PY} in the conference '{SE}', authored by {AF} and others. "
                  "It falls within the {WC} field, focusing mainly on {DE}. "
                  "{AB}".format(SE=i.SE, AF=i.AF, AB=i.AB, DE=i.DE, PY=i.PY, TI=i.TI, WC=i.WC)
    } for i in data]

    with open('paper_info_ft_dataset.json', 'w', encoding='utf-8') as file:
        json.dump(dataset, file, ensure_ascii=False, indent=4)


