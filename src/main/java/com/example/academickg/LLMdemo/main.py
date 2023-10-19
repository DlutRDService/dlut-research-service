from flask import Flask, request, jsonify

from milvus.Milvus import connection, embedding, insert, connectCollection

app = Flask(__name__)


@app.route('/embeddingApi/search', methods=['POST'])
def my_function():
    data = request.get_json()
    queries = data["queries"]
    embedding_results = embedding(queries)
    entities = [
            [queries],
            [embedding_results],
    ]
    collection = connectCollection("Paper")
    result = insert(collection, entities)
    return jsonify({'result': result})


@app.route('/embeddingApi/getEmbedding', methods=['POST'])
def my_function():
    data = request.get_json()  # 获取请求中的JSON数据
    texts = data["texts"]
    embedding_results = embedding(texts)
    return jsonify({'result': embedding_results})  # 返回JSON响应


@app.route('/embeddingApi/my_function', methods=['POST'])
def my_function():
    data = request.get_json()  # 获取请求中的JSON数据
    # 在这里实现您的函数逻辑
    result = data['a'] + data['b']  # 假设函数是将'a'和'b'相加
    return jsonify({'result': result})  # 返回JSON响应


@app.route('/embeddingApi/my_function', methods=['POST'])
def my_function():
    data = request.get_json()  # 获取请求中的JSON数据
    # 在这里实现您的函数逻辑
    result = data['a'] + data['b']  # 假设函数是将'a'和'b'相加
    return jsonify({'result': result})  # 返回JSON响应


if __name__ == '__main__':
    connection(host="127.0.0.1", port="19530", db_name="Paper")
    app.run()