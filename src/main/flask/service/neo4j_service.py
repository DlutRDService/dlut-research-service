from py2neo import Graph
import logging

from dao.paper import Paper
from pipeline.data_loarder import DataLoader

# 配置日志
logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(levelname)s - %(message)s')

class Neo4jService:
    def __init__(self, uri, user, pwd):
        self.__uri = uri
        self.__user = user
        self.__pwd = pwd
        self.__graph = None
        self.connect()

    def connect(self) -> bool:
        try:
            self.__graph = Graph(self.__uri, auth=(self.__user, self.__pwd))
            print("Connected successfully to Neo4j.")
            return True
        except Exception as e:
            print("Failed to connect to Neo4j:", e)
            return False

    def close(self):
        if self.__graph is not None:
            self.__graph = None
            print("Connection to Neo4j closed.")

    def paper_to_neo4j(self, paper: Paper) -> None:

        for keyword in paper.DE:
            keyword = keyword.title()
            self.add_node("Keyword", {"name": keyword})
        # self.add_node(paper.TI)
        # for author in paper.AU:
        #     self.add_node(author)


    def add_node(self, label: str, properties: dict) -> None:
        """add new node"""
        transaction = self.__graph.begin()  # start a new transation
        try:
            prop_string = ', '.join(f'{key}: "{value}"' for key, value in properties.items())
            cypher = f"MERGE (n:{label} {{ {prop_string} }})"
            transaction.run(cypher)
            self.__graph.commit(transaction)  # commit
            logging.info(f"Node {label} successfully added.")
        except Exception as e:
            self.__graph.rollback()  # rollback
            logging.info(f"Failed to add node: {e}")

    @staticmethod
    def add_relationship(graph, from_node, rel_type, to_node, properties=None) -> None:
        """ add new relationship """
        if properties is None:
            properties = {}
        prop_string = ', '.join(f'{key}: "{value}"' for key, value in properties.items())
        query = (f"MATCH (a), (b) WHERE a.name = '{from_node}' AND b.name = '{to_node}' "
                 f"CREATE (a)-[r:{rel_type} {{ {prop_string} }}]->(b)")
        graph.run(query)


# demo
if __name__ == "__main__":
    uri = "bolt://localhost:7687"
    user = "neo4j"
    pwd = "AcademicKG"

    papers = DataLoader(r'C:\Users\AI\Desktop\data\AI\2018-2024\2019', 'DE')

    neo4j_service = Neo4jService(uri, user, pwd)
    for paper in papers:
        neo4j_service.paper_to_neo4j(paper)


