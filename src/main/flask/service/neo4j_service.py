from config.neo4j_config import Neo4jConnection

class Neo4jService:
    def __init__(self, uri, user, pwd):
        self.conn = Neo4jConnection(uri, user, pwd)
        self.graph = self.conn.connect()

    def get_data(self, query):
        """ 获取数据 """
        return self.conn.query(query)

    def add_node(self, label, properties):
        """添加新的节点"""
        tx = self.graph.begin()  # 开启一个新事务
        try:
            prop_string = ', '.join(f'{key}: "{value}"' for key, value in properties.items())
            query = f"CREATE (n:{label} {{ {prop_string} }})"
            tx.run(query)
            tx.commit()  # 提交事务
            return "Node successfully added."
        except Exception as e:
            tx.rollback()  # 报错回滚
            return f"Failed to add node: {e}"

    def add_relationship(self, from_node, rel_type, to_node, properties=None):
        """ 添加新的关系 """
        if properties is None:
            properties = {}
        prop_string = ', '.join(f'{key}: "{value}"' for key, value in properties.items())
        query = (f"MATCH (a), (b) WHERE a.name = '{from_node}' AND b.name = '{to_node}' "
                 f"CREATE (a)-[r:{rel_type} {{ {prop_string} }}]->(b)")
        self.conn.query(query)

    def close(self):
        """ 关闭连接 """
        self.conn.close()

# 使用示例
if __name__ == "__main__":
    # 替换为您的 Neo4j 实例信息
    uri = "bolt://localhost:7687"
    user = "neo4j_username"
    pwd = "neo4j_password"

    neo4j_service = Neo4jService(uri, user, pwd)

    # 示例：获取数据
    result = neo4j_service.get_data("MATCH (n) RETURN n LIMIT 5")
    for record in result:
        print(record)

    # 示例：添加节点
    neo4j_service.add_node("Person", {"name": "Alice", "age": 30})

    # 示例：添加关系
    neo4j_service.add_relationship("Alice", "KNOWS", "Bob")

    # 关闭服务
    neo4j_service.close()
