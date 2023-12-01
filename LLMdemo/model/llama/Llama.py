# TODO 查看返回结果的数据结构

def embedding(llama_embedding, text):
    doc_result = llama_embedding.embed_documents([text])
    query_result = llama_embedding.embed_query(text)


def ner(llama_embedding, file):
    return None

def question_answering(llama, question, context):
    output = llama(
        "Q: Name the planets in the solar system? A: ",  # Prompt
        max_tokens=32,  # Generate up to 32 tokens
        stop=["Q:", "\n"],  # Stop generating just before the model would generate a new question
        echo=True  # Echo the prompt back in the output
    )  # Generate a completion, can also call create_completion


def abstract_segmentation(llama, abstract):
    pass