#! /usr/bin/python3.11
# -*- coding:utf-8 -*-

import pymysql

from dataProcess.CreateESIDict import CreateJournalCategoryDict
from dataProcess.DealPaperTxt import DealPaperInformation
from dataProcess.TittleProcess import get_titles


# TODO paper存储只剩引文信息处理
def import_to_mysql(data_base, titles):
    # 获取ESI字典
    esi_dict = CreateJournalCategoryDict()
    # 使用 cursor() 方法创建一个游标对象 cursor
    cursor = data_base.cursor()
    # 分批次导入mysql
    num_paper = 0
    num_author = 0
    for title in titles:
        AF = ''
        wos_data = DealPaperInformation(
            title, WC=None, Esi_dict=esi_dict
        )
        if wos_data.TI_name == "":
            continue
        for af in wos_data.AF:
            AF += af.AuthorName + '; '
        wos_data.WC = str(wos_data.WC).replace("]", "").replace("[", "").replace("\'", "").replace('\"', '')
        wos_data.DE = str(wos_data.DE).replace(']', '').replace('[', '').replace('\"', '').replace('\'', '')
        # sql_paper = ("""insert into paper(tl, au, de, so, py, wc, esi, tc, nr, ab_path)
        #                 value ('{}', '{}', '{}', '{}', '{}', '{}', '{}', {}, {}, '{}')"""
        #              .format(wos_data.TI_name, AF, wos_data.DE, wos_data.SO, wos_data.PY, wos_data.WC,
        #                      wos_data.ESI, wos_data.TC, wos_data.NR, wos_data.AB))

        try:
            cursor.callproc("insert_or_update_paper",
                            args=(wos_data.TI_name, AF, wos_data.DE, wos_data.SO, wos_data.PY, wos_data.WC,
                                  wos_data.ESI, wos_data.TC, wos_data.NR, wos_data.AB, ""))
            num_paper += 1
            # 执行sql语句
            # cursor.execute(sql_paper)
            # 提交到数据库执行
            data_base.commit()
        except Exception as e:
            print(e)
            data_base.rollback()
        for i in range(len(wos_data.AF)):
            wos_data.AF[i].AuthorOrganization = sorted(wos_data.AF[i].AuthorOrganization)
            wos_data.AF[i].AuthorOrganization = \
                (str(wos_data.AF[i].AuthorOrganization)
                 .replace('[', '')
                 .replace(']', '')
                 .replace("\'", "").replace("\"", ""))

            try:
                cursor.callproc("insert_or_update_author_record",
                                args=(wos_data.AF[i].AuthorName, wos_data.AF[i].AuthorNation,
                                      wos_data.AF[i].AuthorOrganization, wos_data.WC))
                # 提交到数据库执行
                data_base.commit()
            except Exception as e:
                # 如果发生错误回滚
                print(e)
                data_base.rollback()
            num_author += 1
    # 关闭数据库连接
    data_base.close()


# 测试
if __name__ == '__main__':
    # 打开数据库连接,设置路径，端口，用户名，密码，数据库名。
    db = pymysql.connect(host='localhost', user='zsl', passwd='Lish145210@', port=3306, db='RDService')
    # 批量处理数据
    # 将txt文本切割成文献列表
    paper = get_titles(r"../data/AIData/Test.txt")

    import_to_mysql(db, paper)
