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


if __name__ == "__main__":
    connections.connect(host="localhost", port="19530")
    collection = Collection("PaperMilvus")      # Get an existing collection.
    collection.load()
    query_vector = sentenceVector(sys.argv[1])
    search_params = {
        "metric_type": "L2",
        "offset": 5,
        "ignore_growing": False,
        "params": {"nprobe": 10}
    }
    results = collection.search(
        data=[query_vector],
        anns_field="paper_intro",
        # the sum of `offset` in `param` and `limit`
        # should be less than 16384.
        param=search_params,
        limit=1000,
        expr=None,
        # set the names of the fields you want to
        # retrieve from the search result.
        output_fields=['paper_id'],
        consistency_level="Strong"
    )

    results[0].ids
    results[0].distances
    hit = results[0][0]
    hit.entity.get('title')
