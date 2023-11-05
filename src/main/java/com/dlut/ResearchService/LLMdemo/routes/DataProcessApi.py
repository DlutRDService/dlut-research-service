# ! /usr/bin/python3.10
# ! -*- coding:UTF-8 -*-

import pymysql
from flask import Blueprint

from dataProcess.CreateESIDict import CreateJournalCategoryDict
from dataProcess.DealPaperInfo import DealPaperInformation
from dataProcess.TittleProcess import get_titles

# 创建蓝图
data_process_blueprint = Blueprint('dataProcess', __name__)
# 获取ESI字典
esi_dict = CreateJournalCategoryDict()
# 打开数据库连接,设置路径，端口，用户名，密码，数据库名。
db = pymysql.connect(host='localhost', user='zsl', passwd='Lish145210@', port=3306, db='AcademicKG')


@data_process_blueprint.route('/api/import/mysql', methods=['POST'])
def importToMysql():
    # 批量处理数据
    # 将txt文本切割成文献列表
    titles = get_titles(r"../dataProcess/data/AIData/Test.txt")

    # 使用 cursor() 方法创建一个游标对象 cursor
    cursor = db.cursor()
    # 分批次导入mysql
    num_paper = 0
    num_author = 0
    for title in titles:
        wos_data = DealPaperInformation(
            title, WC=None, Esi_dict=esi_dict
        )
        if wos_data.TI_name == "":
            continue
        wos_data.WC = str(wos_data.WC).replace("]", "").replace("[", "").replace("\'", "").replace('\"', '')
        sql_paper = ("""insert ignore into paper(id,Title,Journal,Year,wc,esi) 
        value ({},'{}','{}',{},'{}','{}')"""
                     .format(num_paper, wos_data.TI_name, wos_data.SO, wos_data.PY, wos_data.WC, wos_data.ESI))
        num_paper += 1
        try:
            # 执行sql语句
            cursor.execute(sql_paper)
            # 提交到数据库执行
            db.commit()
            if num_paper % 1000 == 0:
                print('数据已导入{}'.format(num_paper))
        except:
            # 若发生错误则回滚
            print("Error,Back")
            db.rollback()

        for i in range(len(wos_data.AF)):
            wos_data.AF[i].AuthorOrganization = \
                (str(wos_data.AF[i].AuthorOrganization)
                 .replace('[', '')
                 .replace(']', '')
                 .replace("\'", "").replace("\"", ""))
            sql_author = """INSERT INTO Author (id, Name, Country, Organization) SELECT {}, '{}', '{}', '{}' from DUAL 
            where not exists(select id from Author where `Name`='{}' and Organization = '{}')""".format(
                num_author, wos_data.AF[i].AuthorName, wos_data.AF[i].AuthorNation, wos_data.AF[i].AuthorOrganization,
                wos_data.AF[i].AuthorName, wos_data.AF[i].AuthorOrganization)
            try:
                # 执行sql语句
                cursor.execute(sql_author)
                # 提交到数据库执行
                db.commit()
            except:
                # 如果发生错误回滚
                print("ERROR", sql_author)
                db.rollback()
            sql_paper_author = ("""Insert into paper_author (Paper_id, Author_id) value ({},{})"""
                                .format(num_paper - 1, num_author))
            try:
                cursor.execute(sql_paper_author)
                db.commit()
            except:
                # 若发生错误则回滚
                print("ERROR", sql_paper_author)
                db.rollback()
            num_author += 1

    # 关闭数据库连接
    db.close()


@data_process_blueprint.route('/api/user', methods=['POST'])
def create_user():
    pass
