#! /usr/bin/python3.10
# -*- coding: utf-8 -*-

from pymilvus import (
    connections,
    utility,
    FieldSchema,
    CollectionSchema,
    DataType,
    Collection,
    db
)
from sentence_transformers import SentenceTransformer
from createDatabase import createCollection
from sentenceVector import sentenceVector
import sys


if __name__ == '__main__':
    # 连接milvus服务器,注意端口映射，要和启动docker时设置的端口一致
    connections.connect(host="localhost", port="19530")
    # if "paper" not in db.list_database():
    #     # 创建名为paper的数据库
    #     db.create_database("paper")
    # if utility.has_collection("PaperMilvus"):
    #     pass
    # else:
    #     # 创建名为PaperMilvus的集合
    #     paper_collection = createCollection("paper_id", "paper_name", "paper_intro", "PaperMilvus")
    #     paper_collection.create_partition("Swarm intelligence")
    collection = Collection("PaperMilvus")
    vector = sentenceVector(sys.argv[1])
    entities = [
        [int(sys.argv[2])],
        [sys.argv[1]],
        [vector],
    ]
    insert_result = collection.insert(entities)
    print(insert_result)
    collection.flush()
    connections.disconnect("default")
# 插入数据 要插入的数据的数据类型必须与集合的模式匹配，否则 Milvus 将引发异常。
"""Insert data into the collection.

Args:
    data (``list/tuple/pandas.DataFrame``): The specified data to insert
    partition_name (``str``): The partition name which the data will be inserted to,
        if partition name is not passed, then the data will be inserted
        to default partition
    timeout (float, optional): an optional duration of time in seconds to allow
        for the RPCs. If timeout is not set, the client keeps waiting until the
        server responds or an error occurs.
Returns:
    MutationResult: contains 2 properties `insert_count`, and, `primary_keys`
        `insert_count`: how may entites have been inserted into Milvus,
        `primary_keys`: list of primary keys of the inserted entities
Raises:
    MilvusException: If anything goes wrong.

Examples:
    >>> from pymilvus import Collection, FieldSchema, CollectionSchema, DataType
    >>> import random
    >>> schema = CollectionSchema([
    ...     FieldSchema("film_id", DataType.INT64, is_primary=True),
    ...     FieldSchema("films", dtype=DataType.FLOAT_VECTOR, dim=2)
    ... ])
    >>> collection = Collection("test_collection_insert", schema)
    >>> data = [
    ...     [random.randint(1, 100) for _ in range(10)],
    ...     [[random.random() for _ in range(2)] for _ in range(10)],
    ... ]
    >>> res = collection.insert(data)
    >>> res.insert_count
    10
"""


