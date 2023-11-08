from openai import OpenAI

client = OpenAI()


# TODO 这个方法的数据结构还没弄明白
def get_embedding(text, model="text-embedding-ada-002"):
    text = text.replace("\n", " ")
    return client.embeddings.create(input=[text], model=model)['data'][0]['embedding']

