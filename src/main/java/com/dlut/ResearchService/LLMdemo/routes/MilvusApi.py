from flask import Blueprint, request
from Milvus.Milvus import embedding

# 创建蓝图
milvus_blueprint = Blueprint('Milvus', __name__)


@milvus_blueprint.route('/api/milvus/search', methods=['GET'])
def search():
    query_param = request.args.get('query_param')
    query_embedding = embedding(query_param)


@milvus_blueprint.route('/api/user', methods=['POST'])
def create_user():
    pass
