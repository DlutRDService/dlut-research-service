from flask import Blueprint

# 创建蓝图
milvus_blueprint = Blueprint('milvus', __name__)


@milvus_blueprint.route('/api/user', methods=['GET'])
def get_user():
    pass


@milvus_blueprint.route('/api/user', methods=['POST'])
def create_user():
    pass