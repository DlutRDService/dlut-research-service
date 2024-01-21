from flask import Blueprint

# 创建蓝图
neo_blueprint = Blueprint('neo', __name__)


@neo_blueprint.route('/api/user', methods=['GET'])
def get_user():
    pass


@neo_blueprint.route('/api/user', methods=['POST'])
def create_user():
    pass