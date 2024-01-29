from flask import Flask

from routes.DataProcessApi import data_process_blueprint


def create_app():
    app = Flask(__name__)

    app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024  # 限制为16MB

    # 其他配置和蓝图的添加
    # app.register_blueprint(neo_blueprint)
    # app.register_blueprint(milvus_blueprint)
    app.register_blueprint(data_process_blueprint)
    return app


if __name__ == "__main__":
    app = create_app()
    app.run()

