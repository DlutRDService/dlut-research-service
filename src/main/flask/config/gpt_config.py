import os
import time

from openai import OpenAI


def gpt_api(message: list, vision: str) -> str:
    while True:
        if vision == 'gpt-3.5-turbo':
            try:
                os.environ["OPENAI_API_KEY"] = "sk-9DTRcJyDuhIBhuy6Ef61B3C9Df77431e832a0f68E9F827Bd"
                client = OpenAI(base_url="https://hk.xty.app/v1")
                response = client.chat.completions.create(
                    model=vision,
                    messages=message
                )
                return response.choices[0].message.content
            except Exception:
                time.sleep(300)
                continue

        if vision == 'gpt-4-1106-preview':
            try:
                os.environ["OPENAI_API_KEY"] = "sk-xTmwGELIcqEKcg8v06Ef5592F73e4fDaB9B995660cEe530d"
                client = OpenAI(base_url="https://hk.xty.app/v1")
                response = client.chat.completions.create(
                    model=vision,
                    messages=message
                )
                return response.choices[0].message.content
            except Exception:
                time.sleep(300)
                continue


def embedding(client, text, model="text-embedding-ada-002"):
    text = text.replace("\n", " ")
    return client.embeddings.create(input=[text], model=model)['data'][0]['embedding']