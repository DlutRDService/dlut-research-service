# TODO 查看返回结果的数据结构

def embedding(llama_embedding, text):
    doc_result = llama_embedding.embed_documents([text])
    query_result = llama_embedding.embed_query(text)


