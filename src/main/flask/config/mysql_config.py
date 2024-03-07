import pymysql

class MySQLConnection:
    def __init__(self, host, user, password, db):
        self.connection = None
        self.host = host
        self.user = user
        self.password = password
        self.db = db
        self.connect()

    def connect(self):
        """connect to Mysql"""
        try:
            self.connection = pymysql.connect(
                host=self.host,
                user=self.user,
                password=self.password,
                db=self.db,
                charset='utf8mb4',
                cursorclass=pymysql.cursors.DictCursor
            )
            print("Connected successfully to MySQL.")
        except pymysql.MySQLError as e:
            print(f"Failed to connect to MySQL: {e}")

    def query(self, sql, params=None):
        """query"""
        with self.connection.cursor() as cursor:
            cursor.execute(sql, params)
            return cursor.fetchall()

    def execute(self, sql, params=None):
        """run"""
        with self.connection.cursor() as cursor:
            cursor.execute(sql, params)
            self.connection.commit()

    def close(self):
        """close connection"""
        if self.connection is not None:
            self.connection.close()
            print("Connection to MySQL closed.")

# demo
if __name__ == "__main__":
    # 替换为您的 MySQL 实例信息
    host = "localhost"
    user = "your_username"
    password = "your_password"
    db = "your_database"

    mysql_conn = MySQLConnection(host, user, password, db)

    # 示例查询
    result = mysql_conn.query("SELECT * FROM your_table LIMIT 5")
    for record in result:
        print(record)

    # 关闭连接
    mysql_conn.close()
