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





# 创建集合
def createCollection(id, name, intro, collectionName):
    paper_id = FieldSchema(
        name=id,
        dtype=DataType.INT64,
        is_primary=True,
    )
    paper_name = FieldSchema(
        name=name,
        dtype=DataType.VARCHAR,
        max_length=400,
    )
    paper_intro = FieldSchema(
        name=intro,
        dtype=DataType.FLOAT_VECTOR,
        dim=384
    )
    schema = CollectionSchema(
        fields=[paper_id, paper_name, paper_intro],
        description="Collection",
        enable_dynamic_field=True
    )
    return Collection(name=collectionName, schema=schema, using='default', shards_num=2)

