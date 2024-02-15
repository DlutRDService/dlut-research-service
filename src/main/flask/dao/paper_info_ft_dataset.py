"""
Author: AI
Date: 2024-02
Description: Generate the dataset about introducing paper info.
"""

import json

from dao.Dataset import Dataset
import torch



if __name__ == '__main__':

    torch.cuda.empty_cache()
    data = Dataset('../data/conference2023', "TI", "SE", "DE", "WC", "AF", "PY", "AB")

    dataset = [{
        "instruction": "Introduce the paper '{TI}'.".format(TI=i.TI),
        "input": "",
        "output": "This paper '{TI}' was published in {PY} in the conference {SE}, authored by {AU} and others. "
                  "It falls within the {WC} field in WOS database. "
                  "{AB}".format(SE="and ".join(i.SE), AU="; ".join(i.AU[0:2]), AB=i.AB, DE=", ".join(i.DE), PY=i.PY,
                                TI=i.TI,
                                WC="and ".join(i.WC))
    } for i in data]

    with open('paper_info_ft_dataset.json', 'w', encoding='utf-8') as file:
        json.dump(dataset, file, ensure_ascii=False, indent=4)


