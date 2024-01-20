from py2neo import Graph

class Neo4jConnection:
    def __init__(self, uri, user, pwd):
        self.__uri = uri
        self.__user = user
        self.__pwd = pwd
        self.__graph = None
        self.connect()

    def connect(self):
        try:
            self.__graph = Graph(self.__uri, auth=(self.__user, self.__pwd))
            print("Connected successfully to Neo4j.")
        except Exception as e:
            print("Failed to connect to Neo4j:", e)

    def close(self):
        if self.__graph is not None:
            self.__graph = None
            print("Connection to Neo4j closed.")

    def query(self, query):
        try:
            return self.__graph.run(query)
        except Exception as e:
            print("Query failed:", e)

# 使用示例
if __name__ == "__main__":
    # 替换为您的 Neo4j 实例信息
    uri = "bolt://localhost:7687"
    user = "neo4j_username"
    pwd = "neo4j_password"

    conn = Neo4jConnection(uri, user, pwd)

    # 示例查询
    query_result = conn.query("MATCH (n) RETURN n LIMIT 5")
    for record in query_result:
        print(record)

    # 关闭连接
    conn.close()
