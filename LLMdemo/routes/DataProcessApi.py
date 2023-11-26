# ! /usr/bin/python3.10
# ! -*- coding:UTF-8 -*-

import pymysql
import os
from flask import Blueprint, request

from dataProcess.CreateESIDict import CreateJournalCategoryDict
from assistant.gpt import embeddingGpt
from assistant.llama import embeddingLlama
from assistant.gpt.AbstractSegmentation import abstractSegmentation
from dataProcess.ImportToMysql import import_to_mysql

data_process_blueprint = Blueprint('dataProcess', __name__)

# os.environ['OPENAI_API_KEY'] = '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'

# 打开数据库连接,设置路径，端口，用户名，密码，数据库名。
db = pymysql.connect(host='localhost', user='root', passwd='lish145210', port=3306, db='rdService')


@data_process_blueprint.route('/api/import/mysql', methods=['POST'])
def importToMysql():
    # 以下代码为处理上传的文件
    file = request.files['file']
    file_content = file.read().decode('utf-8')
    paper = file_content.split("\nER\n")
    import_to_mysql(db, paper)


# TODO GPT的逻辑没写
@data_process_blueprint.route('/api/embedding', methods=['POST'])
def get_embedding():
    # 获取POST请求中的参数
    tokens = request.form.get('token')  # 获取第一个参数的值
    model_name = request.form.get('model')  # 获取第二个参数的值
    if model_name == "GPT":
        result = embeddingGpt.get_embedding(tokens)
    elif model_name == "Llama":
        result = embeddingLlama.get_embedding(tokens)
    # 执行相应的处理逻辑
    # ...

    # 返回处理结果
    # return jsonify(result)


@data_process_blueprint.route('/api/abstractSegment', methods=['Post'])
def abstract_segment():
    abstract = request.form.get('abstract')
    return abstractSegmentation(abstract)

