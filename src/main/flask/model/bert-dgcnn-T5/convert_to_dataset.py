import json
import os

from openai import OpenAI

from utils.paper_utils import replace_abbreviations, get_titles, split_sentence
from utils.paper_utils import DealPaperInformation

os.environ["OPENAI_API_KEY"] = "sk-lIOHkYLv2dRd9UpdE0038d957e8f4609870a510f3b8b063e"
client = OpenAI(base_url="https://api.kwwai.top/v1")


def condense_abstract(abstract):
    """
    Condense the summary of the paper by gpt
    """
    completion = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {"role": "system", "content": "You are an intelligent robot specializing in summarizing and condensing text. I will provide you with a summary text, and your task is to condense it to around 200 words."},
            {"role": "user", "content": abstract}
        ]
    )
    return completion.choices[0].message.content


if __name__ == "__main__":
    titles = get_titles('../../data/Test.txt')

    T5_dataset_js = []
    dgcnn_dataset_js = []

    for title in titles:
        wosdata = DealPaperInformation(title)
        long_abs = replace_abbreviations(wosdata.AB)
        short_abs = condense_abstract(long_abs)
        ab_seqs = split_sentence(short_abs)
        T5_dataset_js.append(
            {
                "long_abs": long_abs,
                "short_abs": short_abs
            }
        )
        dgcnn_dataset_js.append({
            "ab_seqs": ab_seqs,
            "label": [0 for _ in range(len(ab_seqs))]
        })

    with open('t5_dataset.json', 'w', encoding='utf-8') as file:
        json.dump(T5_dataset_js, file, ensure_ascii=False, indent=4)

    with open('dgcnn_dataset.json', 'w', encoding='utf-8') as file:
        json.dump(dgcnn_dataset_js, file, ensure_ascii=False, indent=4)







