from langchain.embeddings import LlamaCppEmbeddings
from pymilvus import connections, Collection, db
from pymilvus.orm import utility
from pymilvus import CollectionSchema, FieldSchema, DataType


def connectCollection(collectionName):
    return Collection(collectionName)


def connection(host, port, db_name):
    connections.connect(host=host, port=port, db_name=db_name)


def disconnect(name):
    connections.disconnect(name)


def embedding(text):
    llama = LlamaCppEmbeddings(model_path="/Users/zhihu55/IdeaProjects/demo2/ggml-model-q4_0.bin")
    return llama.embed_query(text)


def insert(collection, entities):
    collection.insert(entities)


def create_dataBase(db_name):
    db.create_database(db_name)


def drop_database(db_name):
    db.drop_database(db_name)


def createCollection(db_name, collection_name):
    paper_id = FieldSchema(
        name="id",
        dtype=DataType.INT64,
        is_primary=True,
    )
    paper = FieldSchema(
        name="paper",
        dtype=DataType.VARCHAR,
        max_length=200,
        # The default value will be used if this field is left empty during data inserts or upsets.
        # The data type of `default_value` must be the same as that specified in `type`.
        default_value="Unknown"
    )
    vector = FieldSchema(
        name="vector",
        dtype=DataType.FLOAT_VECTOR,
        dim=4096
    )
    description = FieldSchema(
        name="description",
        dtype=DataType.VARCHAR,
        max_length=200,
        # The default value will be used if this field is left empty during data inserts or upserts.
        # The data type of `default_value` must be the same as that specified in `dtype`.
        default_value="Unknown"
    )
    schema = CollectionSchema(
        fields=[paper_id, paper, vector, description],
        description="paper_embedding",
        enable_dynamic_field=True
    )
    collection_name = collection_name
    Collection(
        name=collection_name,
        schema=schema,
        using=db_name,
        shards_num=2
    )


def dropCollection(collection_name):
    utility.drop_collection(collection_name)


def check_collection(collection_name):
    print(utility.has_collection(collection_name))


def createPartition(collection_name, partition_name):
    collection = Collection(collection_name)
    collection.create_partition(partition_name)


def createIndex(collection, field_name):
    index_params = {
        "metric_type": "L2",
        "index_type": "IVF_FLAT",
        "params": {"nlist": 4096}
    }
    collection.create_index(
        field_name=field_name,
        index_params=index_params
    )


class Milvus:
    def __int__(self, name):
        self.name = name
