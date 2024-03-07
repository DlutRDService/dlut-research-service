from pymilvus import connections, Collection

class MilvusConnection:
    def __init__(self, host, port):
        self.host = host
        self.port = port
        self.connect()

    def connect(self):
        """connect Milvus"""
        try:
            connections.connect(host=self.host, port=self.port)
            print("Connected successfully to Milvus.")
        except Exception as e:
            print(f"Failed to connect to Milvus: {e}")

    def create_collection(self, collection_name, fields):
        collection = Collection(name=collection_name, schema=fields)
        return collection

    def insert_data(self, collection_name, data):
        """input data"""
        collection = Collection(name=collection_name)
        ids = collection.insert(data)
        return ids

    def search(self, collection_name, query, top_k, params):
        """search"""
        collection = Collection(name=collection_name)
        results = collection.search(query, top_k=top_k, params=params)
        return results

    def close(self):
        """close connection"""
        connections.disconnect()
        print("Disconnected from Milvus.")

# demo
if __name__ == "__main__":
    # 替换为您的 Milvus 实例信息
    host = "localhost"
    port = "19530"

    milvus_conn = MilvusConnection(host, port)

    # 示例操作：创建集合、插入数据、搜索等
    # ...

    # 关闭连接
    milvus_conn.close()
