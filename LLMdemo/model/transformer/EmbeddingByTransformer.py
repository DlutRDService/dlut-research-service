from sentence_transformers import SentenceTransformer


def embedding_by_transformer(sentences):
    model = SentenceTransformer('all-MiniLM-L6-v2')
    embeddings = model.encode(sentences)
    return zip(sentences, embeddings)