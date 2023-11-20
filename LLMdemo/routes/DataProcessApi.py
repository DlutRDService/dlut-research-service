# ! /usr/bin/python3.10
# ! -*- coding:UTF-8 -*-

import pymysql
import os
from flask import Blueprint, request

from dataProcess.CreateESIDict import CreateJournalCategoryDict
from dataProcess.DealPaperInfo import DealPaperInformation
from dataProcess.TittleProcess import get_titles
from assistant.gpt import embeddingGpt
from assistant.llama import embeddingLlama
from assistant.gpt.AbstractSegmentation import abstractSegmentation

data_process_blueprint = Blueprint('dataProcess', __name__)

os.environ['OPENAI_API_KEY'] = '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'

# 获取ESI字典
esi_dict = CreateJournalCategoryDict()
# 打开数据库连接,设置路径，端口，用户名，密码，数据库名。
db = pymysql.connect(host='localhost', user='zsl', passwd='Lish145210@', port=3306, db='RDService')


@data_process_blueprint.route('/api/import/mysql', methods=['POST'])
def importToMysql():
    """
    以下代码为处理上传的文件
    file = request.files['file']
    file_content = file.read().decode('utf-8')
    titles = file_content.split("\nER\n")
    """
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
        sql_paper = ("""insert ignore into paper(tl, au, de, so, py, wc, esi, tc, nr, ab)
                        value ({},'{}','{}',{},'{}','{}', {}, {}, {}, {})"""
                     .format(wos_data.TI_name, wos_data.AF, wos_data.DE, wos_data.SO, wos_data.PY, wos_data.WC,
                             wos_data.ESI, wos_data.TC, wos_data.NR, wos_data.AB))
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
            sql_author = ("""INSERT INTO author(author_name, author_country, author_org, 
                            paper_count, paper_count_per_year, research, H, high_cited_paper)  
                            SELECT {}, '{}', '{}', 1, null, {}, null, 0 from DUAL 
                            where not exists(select author_id from author where author_name='{}' and author_org = '{}')
                            """.format(wos_data.AF[i].AuthorName, wos_data.AF[i].AuthorNation,
                                       wos_data.AF[i].AuthorOrganization, wos_data.WC, wos_data.AF[i].AuthorName,
                                       wos_data.AF[i].AuthorOrganization))
            try:
                # 执行sql语句
                cursor.execute(sql_author)
                # 提交到数据库执行
                db.commit()
            except:
                # 如果发生错误回滚
                print("ERROR", sql_author)
                db.rollback()
            num_author += 1
    # 关闭数据库连接
    db.close()


# TODO GPT的逻辑没写
@data_process_blueprint.route('/api/embedding', methods=['POST'])
def get_embedding():
    # 获取POST请求中的参数
    tokens = request.form.get('token')  # 获取第一个参数的值
    model_name = request.form.get('model')  # 获取第二个参数的值
    if model_name == "GPT":
        result = embeddingGpt.get_embedding(tokens)
    elif model_name == "Llama":
        result = embeddingLlama.get_embedding(tokens)
    # 执行相应的处理逻辑
    # ...

    # 返回处理结果
    # return jsonify(result)


@data_process_blueprint.route('/api/abstractSegment', methods=['Post'])
def abstract_segment():
    abstract = request.form.get('abstract')
    return abstractSegmentation(abstract)

