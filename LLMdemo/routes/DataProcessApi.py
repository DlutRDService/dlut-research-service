# ! /usr/bin/python3.11
# ! -*- coding:UTF-8 -*-

import pymysql
import os
from flask import Blueprint, request, jsonify

from model.transformer import EmbeddingByTransformer
from model.gpt import EmbeddingByGpt
from model.llama import EmbeddingByLlama
from model.gpt.AbstractSegmentation import abstractSegmentation
from dataProcess.ImportToMysql import import_to_mysql


data_process_blueprint = Blueprint('dataProcess', __name__)

# 打开数据库连接,设置路径，端口，用户名，密码，数据库名。
db = pymysql.connect(host='localhost', user='zsl', passwd='Lish145210@', port=3306, db='RDService')


# TODO flask的返回相应信息如何处理
@data_process_blueprint.route('/api/import/mysql', methods=['POST'])
def importToMysql():
    # 检查是否存在文件
    try:
        # 处理上传的文件
        file = request.files['file']
        # ...
    except Exception as e:
        app.logger.error('Failed to import file: %s', str(e))
        return jsonify({'error': 'Internal server error'}), 500
    # 检查文件是否有名字，即用户是否上传了文件
    if file.filename == '':
        return jsonify({'error': 'No file selected for uploading'}), 400

    try:
        # 读取文件内容
        file_content = file.read().decode('utf-8')
        paper = file_content.split("\nER\n")
        # 此处假设import_to_mysql函数存在并已正确定义

        import_to_mysql(db, paper)
        return jsonify({'message': 'File successfully processed'}), 200
    except Exception as e:
        print(e)
        # 如果出现任何异常，返回错误信息
        return jsonify({'error': str(e)}), 500


# TODO GPT的逻辑没写
@data_process_blueprint.route('/api/embedding', methods=['POST'])
def get_embedding():
    # 获取POST请求中的参数
    model_name = request.form.get('model')  # 获取第一个参数的值
    sentences = request.form.get('sentences')  # 获取第二个参数的值
    if model_name == "GPT":
        return EmbeddingByGpt.embedding(sentences)
    elif model_name == "Llama":
        return EmbeddingByLlama.embedding(sentences)
    elif model_name == "Transformer":
        return EmbeddingByTransformer.embedding_by_transformer(sentences)


@data_process_blueprint.route('/api/abstractSegment', methods=['Post'])
def abstract_segment():
    abstract = request.form.get('abstract')
    return abstractSegmentation(abstract)

