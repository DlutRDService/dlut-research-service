import json
import os

from openai import OpenAI

from utils.paper_utils import replace_abbreviations, get_titles, split_sentence
from utils.paper_utils import DealPaperInformation

os.environ["OPENAI_API_KEY"] = "sk-sHmCmmuLKCX5juXB7a3c4651Ad4c4072A4C89bE016092a5a"
client = OpenAI(base_url="https://api.kwwai.top/v1")


def condense_abstract(abstract):
    """
    Condense the summary of the paper by gpt
    """
    if abstract is None:
        raise ValueError("Abstract cannot be None. Please provide an abstract")

    completion = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {"role": "system", "content": "You are an intelligent robot specializing in summarizing and condensing text. I will provide you with a summary text, and your task is to condense it to around 200 words."},
            {"role": "user", "content": abstract}
        ]
    )

    return completion.choices[0].message.content


def seq_annotation(seqs):
    """
    Annotation the summary sentences of the paper by gpt
    """
    if seqs is None:
        raise ValueError("Sequences cannot be None. Please provide sequences to annotate")

    completion = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {"role": "system", "content": "You are an intelligent assistant, annotating sentences in a summary based on their importance, labeling them as 1 (important) or 0 (not important). The input is a list of sentences, and the output is a normal list of annotations."},
            {"role": "user", "content": str(seqs)}
        ]
    )

    return completion.choices[0].message.content



if __name__ == "__main__":
    titles = get_titles('../../data/conference2023')

    T5_dataset_js = []
    dgcnn_dataset_js = []

    for num, title in enumerate(titles):
        if num > 2998:
            wosdata = DealPaperInformation(title, "AB")
            long_abs = replace_abbreviations(wosdata.AB)
            try:
                short_abs = condense_abstract(long_abs)
                ab_seqs = split_sentence(short_abs)
                ab_labels = seq_annotation(ab_seqs)
            except ValueError as e:
                continue
            T5_dataset_js.append(
                {
                    "long_abs": long_abs,
                    "short_abs": short_abs
                }
            )
            try:
                if len(ab_seqs) == len(eval(ab_labels)):
                    dgcnn_dataset_js.append({
                        "ab_seqs": ab_seqs,
                        "label": ab_labels
                        # "label": [0 for _ in range(len(ab_seqs))]
                    })
            except Exception as e:
                continue
            if (num + 1) % 200 == 0:
                print("----{}-----".format(num))
                with open('t5_dataset_{}.json'.format(num + 1), 'w', encoding='utf-8') as file:
                    json.dump(T5_dataset_js, file, ensure_ascii=False, indent=4)

                with open('dgcnn_dataset_{}.json'.format(num + 1), 'w', encoding='utf-8') as file:
                    json.dump(dgcnn_dataset_js, file, ensure_ascii=False, indent=4)









