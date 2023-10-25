from flask import Flask
from routes.NeoApi import neo_blueprint
from routes.MilvusApi import milvus_blueprint
from routes.DataProcessApi import data_process_blueprint

app = Flask(__name__)

# 注册
app.register_blueprint(neo_blueprint, milvus_blueprint)
app.register_blueprint(milvus_blueprint)
app.register_blueprint(data_process_blueprint)


if __name__ == "__main__":
    app.run()
