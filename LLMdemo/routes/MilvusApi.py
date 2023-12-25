from flask import Blueprint, request
from model.gpt.Gpt import embedding
from pymilvus import Collection

milvus_blueprint = Blueprint('milvus', __name__)  # 创建蓝图

collection = Collection("book")      # Get an existing collection.
collection.load()

search_params = {    # 查询参数条件
    "metric_type": "L2",
    "offset": 0,
    "ignore_growing": False,
    "params": {"nprobe": 10}
}


@milvus_blueprint.route('/api/milvus_search', methods=['GET'])
def search():
    """
    get a String from request. Search the similary embedding list
    """
    query_param = request.args.get("query_param")  # 获取查询参数
    query_embedding = embedding(query_param)
    results = collection.search(
        data=query_embedding,
        anns_field="book_intro",
        # the sum of `offset` in `param` and `limit`
        # should be less than 16384.
        param=search_params,
        limit=10,
        expr=None,
        # set the names of the fields you want to
        # retrieve from the search result.
        output_fields=['title'],
        consistency_level="Strong"
    )
    return results


@milvus_blueprint.route('/api/embedding', methods=['POST'])
def create_user():
    pass
