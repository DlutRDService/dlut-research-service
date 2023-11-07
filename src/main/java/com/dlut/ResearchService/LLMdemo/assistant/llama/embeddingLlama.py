from langchain.embeddings import LlamaCppEmbeddings


def embedding(text):
    llama = LlamaCppEmbeddings(model_path="/Users/zhihu55/IdeaProjects/demo2/ggml-model-q4_0.bin")
    return llama.embed_query(text)
