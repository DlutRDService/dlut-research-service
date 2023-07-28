#! /usr/bin/python3.10
from sentence_transformers import SentenceTransformer


def sentenceVector(str):
    model = SentenceTransformer('all-MiniLM-L6-v2')
    sentences_embedding = model.encode(str)
    return sentences_embedding

