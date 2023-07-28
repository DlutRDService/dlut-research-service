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
from createDatabase import createCollection
from sentenceVector import sentenceVector

connections.connect(host="localhost", port="19530")
collection = Collection("PaperMilvus")
print(collection.is_empty)
print(collection.primary_field)
index = {
    "index_type": "IVF_FLAT",
    "metric_type": "L2",
    "params": {"nlist": 128},
}
collection.create_index("paper_intro", index)
collection.load()
res = collection.query(
    expr = "paper_id == 307",
    offset = 0,
    limit = 10,
    output_fields = ["paper_id", "paper_name"],
)
sorted_res = sorted(res, key=lambda k: k['paper_id'])
print(sorted_res)
# expr = "paper_id in [0,1,2]"
# collection.delete(expr)
connections.disconnect("PaperMilvus")

