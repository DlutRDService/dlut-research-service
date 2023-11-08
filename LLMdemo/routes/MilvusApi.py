from flask import Blueprint, request
from assistant.llama.embeddingLlama import get_embedding

# 创建蓝图
milvus_blueprint = Blueprint('Milvus', __name__)


@milvus_blueprint.route('/api/milvus/search', methods=['GET'])
def search():
    query_param = request.args.get('query_param')
    query_embedding = get_embedding(query_param)


@milvus_blueprint.route('/api/user', methods=['POST'])
def create_user():
    pass
