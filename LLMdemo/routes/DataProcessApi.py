# ! /usr/bin/python3.11
# ! -*- coding:UTF-8 -*-

import pymysql
from flask import Blueprint, request, jsonify
from langchain.embeddings import LlamaCppEmbeddings

from model.transformer import EmbeddingByTransformer
from model.gpt import EmbeddingByGpt
from model.llama import EmbeddingByLlama
from model.gpt.AbstractSegmentation import abstractSegmentation
from dataProcess.ImportToMysql import import_to_mysql

data_process_blueprint = Blueprint('dataProcess', __name__)

# 连接数据库
db = pymysql.connect(host='localhost', user='zsl', passwd='Lish145210@', port=3306, db='RDService')

# 初始化 Llama 模型
llama_embedding = LlamaCppEmbeddings(model_path="./llama-2-7b.Q4_K_M.gguf")

# TODO GPT的逻辑没写
@data_process_blueprint.route('/api/embedding', methods=['POST'])
def get_embedding():
    # 获取POST请求中的参数
    model_name = request.form.get('model')
    sentences = request.form.get('sentences')
    # 选择模型
    if model_name == "GPT":
        return EmbeddingByGpt.embedding(sentences)
    elif model_name == "Llama":
        return EmbeddingByLlama.embedding(llama_embedding, sentences)
    elif model_name == "Transformer":
        return EmbeddingByTransformer.embedding_by_transformer(sentences)

# TODO flask的返回相应信息如何处理
# 导入数据到mysql
@data_process_blueprint.route('/api/import_mysql', methods=['POST'])
def importToMysql():
    file = request.files['file']
    # 检查文件是否有名字，即用户是否上传了文件
    if file.filename == '':
        return jsonify({'error': 'No file selected for uploading'}), 400
    try:
        # 读取文件内容
        file_content = file.read().decode('utf-8')
        paper = file_content.split("\nER\n")

        import_to_mysql(db, paper)
        return jsonify({'message': 'File successfully processed'}), 200
    except Exception as e:
        print(e)
        # 如果出现任何异常，返回错误信息
        return jsonify({'error': str(e)}), 500

# TODO 向neo4j中导入数据
@data_process_blueprint.route('/api/import_neo4j', methods=['POST'])
def import_neo4j():
    pass

# TODO 向milvus中导入数据
@data_process_blueprint.route('/api/import_milvus', methods=['POST'])
def import_milvus():
    pass

# TODO 摘要序列标注
@data_process_blueprint.route('/api/abstractSegment', methods=['Post'])
def abstract_segment():
    abstract = request.form.get('abstract')
    return abstractSegmentation(abstract)

# TODO txt 转成格式化excel
@data_process_blueprint.route('/api/get_excel', methods=['POST'])
def get_excel():
    file = request.files['file']
    pass
