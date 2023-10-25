from flask import Blueprint

# 创建蓝图
data_process_blueprint = Blueprint('dataProcess', __name__)


@data_process_blueprint.route('/api/user', methods=['GET'])
def get_user():
    pass


@data_process_blueprint.route('/api/user', methods=['POST'])
def create_user():
    pass