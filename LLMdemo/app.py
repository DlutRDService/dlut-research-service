from flask import Flask
from openai import OpenAI
from sentence_transformers import SentenceTransformer

from routes.DataProcessApi import data_process_blueprint
from llama_cpp import Llama

def create_app():
    app = Flask(__name__)
    app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024  # 限制为16MB
    # 配置模型
    app.sens_tan_md = SentenceTransformer('all-MiniLM-L6-v2')  # sentence_transformers
    app.llama = Llama(model_path='../../../PycharmProjects/roberta-gat/llama-2-7b.Q4_K_M.gguf')  #llama

    # 连接GPT接口
    app.client = OpenAI()

    # 其他配置和蓝图的添加
    # app.register_blueprint(neo_blueprint)
    # app.register_blueprint(milvus_blueprint)
    app.register_blueprint(data_process_blueprint)
    return app


if __name__ == "__main__":
    app = create_app()
    app.run()

