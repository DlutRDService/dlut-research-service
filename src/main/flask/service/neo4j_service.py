from config.neo4j_config import Neo4jConfig

class Neo4jService:
    def __init__(self, uri, user, pwd):
        self.conn = Neo4jConfig(uri, user, pwd)
        self.graph = self.conn.connect()

    def get_data(self, query):
        """ 获取数据 """
        return self.conn.query(query)

    def add_node(self, label, properties):
        """add new node"""
        tx = self.graph.begin()  # start a new transation
        try:
            prop_string = ', '.join(f'{key}: "{value}"' for key, value in properties.items())
            query = f"CREATE (n:{label} {{ {prop_string} }})"
            tx.run(query)
            tx.commit()  # commit
            return "Node successfully added."
        except Exception as e:
            tx.rollback()  # rollback
            return f"Failed to add node: {e}"

    def add_relationship(self, from_node, rel_type, to_node, properties=None):
        """ add new relationship """
        if properties is None:
            properties = {}
        prop_string = ', '.join(f'{key}: "{value}"' for key, value in properties.items())
        query = (f"MATCH (a), (b) WHERE a.name = '{from_node}' AND b.name = '{to_node}' "
                 f"CREATE (a)-[r:{rel_type} {{ {prop_string} }}]->(b)")
        self.conn.query(query)

    def close(self):
        """ 关闭连接 """
        self.conn.close()

# demo
if __name__ == "__main__":
    uri = "bolt://localhost:7687"
    user = "neo4j_username"
    pwd = "neo4j_password"

    neo4j_service = Neo4jService(uri, user, pwd)

