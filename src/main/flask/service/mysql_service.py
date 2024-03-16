# ! /usr/bin/python3.11
# -*- coding:utf-8 -*-

import pymysql
import torch.cuda

from utils.paper_utils import DealPaperInformation
from utils.paper_utils import get_titles

class MysqlService:

    def __init__(self, db_connection):
        self.db = db_connection
        self.cursor = self.db.cursor()
        pass

    def import_to_mysql(self, titles):

        num_paper = 0   # statistics the papers
        num_author = 0  # statistice the authors
        for title in titles:
            AF = ''
            # get the TI, exexcute the sp(store process) when not exist in mysql
            wos_data = DealPaperInformation(title, "TI")
            if wos_data.TI == "":
                continue
            query = "SELECT EXISTS(SELECT 1 FROM paper WHERE paper.tl = %s)"
            self.cursor.execute(query, (wos_data.TI,))
            result = self.cursor.fetchone()
            if result[0]:
                num_paper += 1
                continue
            wos_data = DealPaperInformation(title)
            for af in wos_data.AF:
                AF += af.AuthorName + '; '
            wos_data.WC = str(wos_data.WC).replace("]", "").replace("[", "").replace("\'", "").replace('\"', '')
            wos_data.DE = str(wos_data.DE).replace(']', '').replace('[', '').replace('\"', '').replace('\'', '')
            wos_data.CR = str(wos_data.CR).replace(']', '').replace('[', '').replace('\"', '').replace('\'', '')
            wos_data.SE = str(wos_data.SE).replace(']', '').replace('[', '').replace('\"', '').replace('\'', '')
            try:
                self.cursor.callproc("insert_or_update_paper",
                                args=(wos_data.TI, AF, wos_data.DE, wos_data.SO, wos_data.PY, wos_data.WC,
                                      wos_data.ESI, wos_data.TC, wos_data.NR, wos_data.AB[0:1500], wos_data.AB,
                                      wos_data.DI, wos_data.CR, wos_data.r_background, wos_data.r_method,
                                      wos_data.r_result, wos_data.r_conclusion))
                num_paper += 1
                # commit and callproc the sp
                self.db.commit()
                if num_paper % 1000 == 0:
                    print("Already insert (update) {} papers".format(num_paper))
            except Exception as e:
                print(e)
                self.db.rollback()
            for i in range(len(wos_data.AF)):
                wos_data.AF[i].AuthorOrganization = sorted(wos_data.AF[i].AuthorOrganization)
                wos_data.AF[i].AuthorOrganization = \
                    (str(wos_data.AF[i].AuthorOrganization)
                     .replace('[', '')
                     .replace(']', '')
                     .replace("\'", "").replace("\"", ""))

                try:
                    self.cursor.callproc("insert_or_update_author_record",
                                    args=(wos_data.AF[i].AuthorName, wos_data.AF[i].AuthorNation,
                                          wos_data.AF[i].AuthorOrganization))
                    # commit
                    self.db.commit()
                except Exception as e:
                    # rollback if error
                    print(e)
                    self.db.rollback()
                num_author += 1
        # close db connection
        self.db.close()

    def update_author_H(self):
        pass


# 测试
if __name__ == '__main__':

    torch.cuda.empty_cache()
    # 打开数据库连接,设置路径，端口，用户名，密码，数据库名。
    db = pymysql.connect(host='localhost', user='AI', passwd='!@#$AI', port=3306, db='dlut_academic_platform')
    # 批量处理数据
    # 将txt文本切割成文献列表
    paper = get_titles(r'C:\Users\AI\Desktop\data\AI\2019')
    a = MysqlService(db)
    a.import_to_mysql(paper)



