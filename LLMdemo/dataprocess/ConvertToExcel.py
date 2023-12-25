import pandas as pd

from dataprocess.CreateESIDict import CreateJournalCategoryDict
from dataprocess.DealPaperTxt import DealPaperInformation


def object_to_dict(obj):
    return obj.__dict__


def convert_to_excel(file):
    esi_dict = CreateJournalCategoryDict()
    file_content = file.read().decode('utf-8')
    file_content = file_content.replace('\r', '')
    titles = file_content.split("\nER\n")
    wos_paper = []
    for title in titles:
        wos_data = DealPaperInformation(
            title, WC=None, Esi_dict=esi_dict
        )
        AF = ''
        if wos_data.AF is not None:
            for j in range(len(wos_data.AF)):
                if wos_data.AF[j] is not None:
                    AF += wos_data.AF[j].AuthorName + "; "
        wos_data.AF = AF
        wos_paper.append(wos_data)
    wos_dict = [object_to_dict(obj) for obj in wos_paper]
    df = pd.DataFrame(wos_dict)
    return df



