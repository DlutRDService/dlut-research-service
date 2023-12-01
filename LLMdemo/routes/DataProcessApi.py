# ! /usr/bin/python3.11
# ! -*- coding:UTF-8 -*-

import pymysql
from flask import Blueprint, request, jsonify

from dataProcess.ConvertToExcel import convert_to_excel
from model.transformer import Transformer
from model.gpt import Gpt
from model.llama import Llama
from dataProcess.ImportToMysql import import_to_mysql

data_process_blueprint = Blueprint('dataProcess', __name__)

# 连接数据库
db = pymysql.connect(host='localhost', user='zsl', passwd='Lish145210@', port=3306, db='RDService')


# TODO GPT的逻辑没写
@data_process_blueprint.route('/api/embedding', methods=['POST'])
def get_embedding():
    # 获取POST请求中的参数
    model_name = request.form.get('model')
    sentences = request.form.get('sentences')
    # 选择模型
    if model_name == "GPT":
        return Gpt.embedding(sentences)
    elif model_name == "Llama":
        return Llama.embedding(llama_embedding, sentences)
    elif model_name == "bert":
        return Transformer.embedding_by_transformer(sentences)

# TODO flask的返回相应信息如何处理
@data_process_blueprint.route('/api/import_mysql', methods=['POST'])
def importToMysql():
    file = request.files['file']

    if file.filename == '':
        return jsonify({'error': 'No file selected for uploading'}), 400

    try:
        file_content = file.read().decode('utf-8')
        paper = file_content.split("\nER\n")

        import_to_mysql(db, paper)
        return jsonify({'message': 'File successfully processed'}), 200
    except Exception as e:
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
@data_process_blueprint.route('/api/abstract_segment', methods=['Post'])
def abstract_segment():
    model_name = request.form.get('model')
    abstract = request.form.get('abstract')
    if model_name == "GPT":
        return Gpt.abstract_segmentation(abstract)
    elif model_name == "Llama":
        return Llama.abstract_segmentation(abstract)
    elif model_name == "roberta+GAT":
        return Transformer.abstract_segmentation(abstract)


# TODO ner
@data_process_blueprint.route('/api/ner', methods=['POST'])
def ner():
    file = request.files['file']
    model = request.form.get('model')

    if file is None:
        return jsonify({'error': 'No file selected for uploading'}), 400
    if model == 'gpt':
        return Gpt.ner(file)
    if model == 'llama':
        return Llama.ner(llama_embedding, file)
    if model == 'bert':
        return Transformer.ner(file)
    pass

# TODO txt 转成格式化excel
@data_process_blueprint.route('/api/txt_excel', methods=['POST'])
def txt_to_excel():
    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'No file selected for uploading'}), 400
    try:
        convert_to_excel(file)
        return jsonify({'message': 'File successfully processed'}), 200
    except Exception as e:
        # 如果出现任何异常，返回错误信息
        return jsonify({'error': str(e)}), 500

@data_process_blueprint.route('/api/question_answering', methods=['POST'])
def question_answering():
    model_name = request.form.get('model')
    question = request.form.get('question')
    if model_name == "GPT":
        return Gpt.question_answering(question)
    return Gpt.question_answering(question)

@data_process_blueprint.route('/api/classification', methods=['POST'])
def classification():
    model_name = request.form.get('model')
    text = request.form.get('text')
    pass
