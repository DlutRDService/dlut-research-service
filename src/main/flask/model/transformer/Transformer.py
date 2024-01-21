

def embedding_by_transformer(model, sentences):
    embeddings = model.encode(sentences)
    return zip(sentences, embeddings)


def ner(file):
    return None


def abstract_segmentation(roberta_gat, abstract):
    return None