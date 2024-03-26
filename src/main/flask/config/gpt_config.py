import os
import time

from openai import OpenAI

os.environ["OPENAI_API_KEY"] = "sk-9DTRcJyDuhIBhuy6Ef61B3C9Df77431e832a0f68E9F827Bd"

def gpt_api(message: list) -> str:
    while True:
        try:
            client = OpenAI(base_url="https://hk.xty.app/v1")
            response = client.chat.completions.create(
                model="gpt-3.5-turbo",
                messages=message
            )
            return response.choices[0].message.content
        except Exception:
            time.sleep(1800)
            continue


def embedding(client, text, model="text-embedding-ada-002"):
    text = text.replace("\n", " ")
    return client.embeddings.create(input=[text], model=model)['data'][0]['embedding']