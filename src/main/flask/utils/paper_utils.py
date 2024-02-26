#! /usr/bin/python3.11
# -*- coding:utf-8 -*-

import os
import re

import torch
import pandas as pd
from transformers import RobertaTokenizer
from nltk.tokenize import sent_tokenize

from dao.wos_data import WosData, AuthorInformation, CitedReference
from model.RoBERTaGAT.RoBERTaGAT import RobertaGAT


model = RobertaGAT(roberta_model_name="roberta-base", num_classes=5)
model.load_state_dict(torch.load(r'C:\Users\AI\IdeaProjects\dlut-research-service\src\main\flask\model\RoBERTaGAT'
                                 r'\model5.pth', map_location='cuda:0'))
# model = RobertaGAT(roberta_model_name="roberta-base", num_classes=4)
# model.load_state_dict(torch.load('../model/RoBERTaGAT/model.pth', map_location='cuda:0'))
model.eval()


def get_titles(file_path):
    """
    获取路径下的所有文献,或者路径文本
    :param : 论文存储路径
    :return: 返回一个存放所有论文数据的列表
    """
    titles = []
    if file_path is None:
        raise ValueError('Please enter a valid path')
    paths = []
    num = 0
    if os.path.isdir(file_path):
        for (dir_path, dirname, filenames) in os.walk(file_path):
            if filenames is None:
                continue
            for i, filename in enumerate(filenames):
                # 获取当前处理的文件路径
                paths.append(os.path.join(dir_path, filename))
        for path in paths:
            title = split_to_titles(path)
            for i in title:
                num += 1
                if num % 10000 == 0 and num != 0:
                    print('----已经读入{}篇文献数据----'.format(num))
                titles.append(i)
        print("共有{}篇文献".format(len(titles)))
    elif os.path.isfile(file_path):
        title = split_to_titles(file_path)
        for i in title:
            num += 1
            if num % 10000 == 0 and num != 0:
                print('----已经读入{}篇文献数据----'.format(num))
            titles.append(i)
        print("共有{}篇文献".format(len(titles)))
    else:
        raise ValueError('Please enter a valid path')
    return titles

def DealPaperInformation(title, *args):
    """
    传入一个title，读取统计文献的名称、期刊名、期刊ESI类别、WC类别、年份、引文、摘要、关键词、作者、作者机构、国家
    :param title: 传入文献
    :return:返回论文全部信息的paper类
    """
    if args is None:
        args = ["TI", "AF", "DE", "AB", "SO", "NR", "TC", "WC", "PY", "ab_seq", 'CR', 'DI']

    Esi_dict = CreateJournalCategoryDict()
    wos_data = WosData()

    title = title.split('\n')
    for num, line in enumerate(title):
        # 作者
        if line.find('AF ') == 0 and 'AF' in args:
            Author = AuthorInformation()
            Author.AuthorName = line[3:].replace('\'', '').replace('\"', '')
            wos_data.AF.append(Author)
            i = 1
            while title[num + i][0:3] == '   ':
                Author = AuthorInformation()
                Author.AuthorName = title[num + i][3:].replace('\'', '').replace('\"', '')
                wos_data.AF.append(Author)
                i += 1
            continue
        # 标题
        if line.find('TI ') == 0 and 'TI' in args:
            wos_data.TI = line[3:].replace('\'', '').replace('\"', '').replace('\\', '')
            i = 1
            while title[num + i][0:3] == '   ':
                wos_data.TI += ' ' + title[num + i][3:].replace('\'', '').replace('\"', '').replace('\\', '')
                i += 1
            continue
        # 期刊
        if line.find('SO ') == 0 and 'SO' in args:
            try:
                if ', ' in line:
                    wos_data.SO = line[3:].replace('\'', '').replace('\"', '').split(',')[0]
                    i = 1
                    while title[num + i][0:3] == '   ':
                        wos_data.SO += ' ' + title[num + i][3:].replace('\'', '').replace('\"', '').split(',')[0]
                        i += 1
                    wos_data.ESI = Esi_dict[wos_data.SO]
                elif ':' in line:
                    wos_data.SO = line[3:].replace('\'', '').replace('\"', '').split(': ')[0]
                    i = 1
                    while title[num + i][0:3] == '   ':
                        wos_data.SO += ' ' + title[num + i][3:].replace('\'', '').replace('\"', '').split(': ')[0]
                        i += 1
                    wos_data.ESI = Esi_dict[wos_data.SO]
                else:
                    wos_data.SO = line[3:].replace('\'', '').replace('\"', '').upper()
                    i = 1
                    while title[num + i][0:3] == '   ':
                        wos_data.SO += ' ' + title[num + i][3:].replace('\'', '').replace('\"', '').upper()
                        i += 1
                    wos_data.ESI = Esi_dict[wos_data.SO.replace('\'', '').replace('\"', '')]
            except KeyError:
                wos_data.ESI = ''
                pass
        # 会议
        if line.find('SE ') == 0 and 'SE' in args:
            wos_data.SE = line[3:].replace('\'', '').replace('\"', '')
            i = 1
            while title[num + i][0:3] == '   ':
                wos_data.SE += ' ' + title[num + i][3:].replace('\'', '').replace('\"', '')
                i += 1
            wos_data.SE = wos_data.SE.split('; ')
            continue
        # 关键字
        if line.find("DE ") == 0 and 'DE' in args:
            keywords = line[3:].replace('\'', '').replace('\"', '')
            i = 1
            while title[num + i][0:3] == '   ':
                keywords += ' ' + title[num + i][3:].replace('\'', '').replace('\"', '')
                i += 1
            wos_data.DE = keywords.split('; ')
            continue
        # 摘要
        if line.find("AB ") == 0 and 'AB' in args:
            abstract = line[3:]
            i = 1
            while title[num + i][0:3] == '   ':
                abstract += ' ' + title[num + i][3:]
                i += 1
            wos_data.AB = abstract

            if "ab_seq" in args:
                ab_seqs = sent_tokenize(abstract)

                indices = [list(range(1, len(ab_seqs) + 1)), [0 for _ in range(len(ab_seqs))]]
                indices[0].extend(list(range(1, len(ab_seqs))))
                indices[1].extend(list(range(2, len(ab_seqs) + 1)))

                ab_seqs.insert(0, wos_data.TI)
                input_data = {"seq": ab_seqs, "rel": indices}

                result = seq_annotation(input_data)
                result["label"][0] = 0
                result["label"][-1] = 3

                for i in range(len(result['label'])):
                    if result['label'][i] == 0:
                        wos_data.r_background += result['seq'][i] + " "
                    if result['label'][i] == 1:
                        wos_data.r_method += result['seq'][i] + " "
                    if result['label'][i] == 2:
                        if wos_data.r_method == '':
                            wos_data.r_method = result['seq'][i]
                            continue
                        wos_data.r_result += result['seq'][i] + " "
                    if result['label'][i] == 3:
                        wos_data.r_conclusion += result['seq'][i] + " "
                continue
            continue

        # 作者所在国家、作者所在机构
        if line.find('C1 ') == 0 and 'AF' in args:
            # 国家
            if CheckNation(line) != -1:
                wos_data.Nation.append(CheckNation(line))
            # 所在机构
            if line.find('] ') != -1:
                C1 = line[(line.find('] ') + 1):].replace('\'', '').replace('\"', '').split(',')
                # 读入一级机构
                wos_data.Organization.append(C1[0].strip())
            else:
                C1 = line[3:].replace('\'', '').replace('\"', '').split(',')
                wos_data.Organization.append(C1[0].strip())
            # 写入作者信息(国籍, 机构)
            # 先遍历从AF中获取的作者和作者名字，通过“[]”获取作者名字与同一行的机构和国籍匹配
            if line.find('] ') != -1:
                names = line[(line.find('[') + 1):(line.find('] '))].replace('\'', '').replace('\"', '').split('; ')
                for name in names:
                    writtenFlag = False
                    # 名字在AF存在
                    for AF in wos_data.AF:
                        if name == AF.AuthorName:
                            if len(wos_data.Nation) > 0 and len(wos_data.Organization) > 0:
                                AF.AuthorOrganization.append(wos_data.Organization[-1])
                                AF.AuthorNation = wos_data.Nation[-1]
                            writtenFlag = True
                    # 名字在AF不存在
                    if writtenFlag is False:
                        Author = AuthorInformation()
                        if len(wos_data.Nation) > 0 and len(wos_data.Organization) > 0:
                            Author.AuthorNation = wos_data.Nation[-1]
                            Author.AuthorOrganization.append(wos_data.Organization[-1])
                        wos_data.AF.append(Author)
            i = 1
            while title[num + i][0:2] == '  ':
                Line = title[num + i]
                if CheckNation(Line) != -1:
                    wos_data.Nation.append(CheckNation(Line))
                if Line.find('] ') != -1:
                    C1 = Line[(Line.find('] ') + 1):].replace('\'', '').replace('\"', '').split(', ')
                    wos_data.Organization.append(C1[0].strip())
                else:
                    C1 = Line[3:].replace('\'', '').replace('\"', '').split(',')
                    wos_data.Organization.append(C1[0].strip())
                names = Line[(Line.find('[') + 1):(Line.find('] '))].replace('\'', '').replace('\"', '').split('; ')
                for name in names:
                    writtenFlag = False
                    for AF in wos_data.AF:
                        # 名字在AF存在
                        if name == AF.AuthorName:
                            writtenFlag = True
                            if wos_data.Nation:
                                if AF.AuthorNation == wos_data.Nation[-1]:
                                    if wos_data.Organization is not None:
                                        AF.AuthorOrganization.append(wos_data.Organization[-1])
                                        AF.AuthorOrganization = list(set(AF.AuthorOrganization))
                                if len(AF.AuthorNation) == 0:
                                    if len(wos_data.Nation) > 0 and len(wos_data.Organization) > 0:
                                        AF.AuthorNation = wos_data.Nation[-1]
                                    if wos_data.Organization is not None:
                                        AF.AuthorOrganization.append(wos_data.Organization[-1])
                                        AF.AuthorOrganization = list(set(AF.AuthorOrganization))
                            break
                    # 名字在AF不存在
                    if writtenFlag is False:
                        Author = AuthorInformation()
                        Author.AuthorName = name
                        if len(wos_data.Nation) > 0 and len(wos_data.Organization) > 0:
                            Author.AuthorNation = wos_data.Nation[-1]
                            Author.AuthorOrganization.append(wos_data.Organization[-1])
                            Author.AuthorOrganization = list(set(Author.AuthorOrganization))
                            # print(AF.AuthorOrganization)
                        wos_data.AF.append(Author)
                i += 1
            continue
        # 若没有C1字段,则查找RP字段
        if len(wos_data.Nation) == 0 and len(wos_data.Organization) == 0:
            if line.find('RP ') == 0 and 'AF' in args:
                # 统计国家
                for AF in wos_data.AF:
                    if CheckNation(line) != -1:
                        wos_data.Nation.append(CheckNation(line))
                        AF.AuthorNation = wos_data.Nation[0]
                continue
        # 引文
        if line.find('CR ') == 0 and 'CR' in args:
            cr_list = line[3:].split(', ')
            if len(cr_list) >= 4:
                if cr_list[-1].find('DOI ') == 0:
                    CR = CitedReference(author=cr_list[0], year=cr_list[1],
                                        journal=cr_list[2], doi=cr_list[-1])
                    wos_data.CR.append(CR.to_dict())
            i = 1
            while title[num + i][0:3] == '   ':
                cr_list = title[num + i][0:3].split(', ')
                if len(cr_list) >= 4:
                    if cr_list[-1].find('DOI ') == 0:
                        CR = CitedReference(author=cr_list[0], year=cr_list[1],
                                            journal=cr_list[2], doi=cr_list[-1])
                        wos_data.CR.append(CR.to_dict())
                i += 1
        # 引文数量
        if line.find("NR ") == 0 and 'NR' in args:
            wos_data.NR = line[3:]
            continue
        # 被引TC
        if line.find("TC ") == 0 and 'TC' in args:
            wos_data.TC = line[3:]
        # WC 类别
        if line.find("WC ") == 0 and 'WC' in args:
            wc = line[3:].strip('\n')
            i = 1
            while title[num + i][0:3] == '   ':
                wc += ' ' + title[num + i][3:].strip('\n')
                i += 1
            wos_data.WC = wc.split('; ')
            continue
        # 年份PY
        if (line.find('PY ') == 0 or line.find('EA ') == 0) and 'PY' in args:
            wos_data.PY = line[-4:]
            continue
        if line.find("DI ") == 0 and 'DI' in args:
            wos_data.DI = line[3:]
    if wos_data.AF is not None:
        for j in range(len(wos_data.AF)):
            if wos_data.AF[j] is not None and j <= 2:
                wos_data.AU.append(wos_data.AF[j].AuthorName)
    for i in range(len(wos_data.AF)):
        if wos_data.AF[i].AuthorOrganization is None:
            wos_data.AF[i].AuthorOrganization = ' '
        if wos_data.AF[i].AuthorNation is None:
            wos_data.AF[i].AuthorNation = ' '
    wos_data.AU = list(set(wos_data.AU))
    wos_data.Nation = list(set(wos_data.Nation))
    wos_data.Organization = list(set(wos_data.Organization))
    wos_data.DE = list(set(wos_data.DE))

    return wos_data

def split_to_titles(file_path):
    with open(file_path, "r+", encoding="utf8") as f:
        content = f.read()
        return content.split('\nER\n')

def CheckNation(Str=''):
    nationList = [
        'USA',
        'England',
        'Abkhazia',
        'Afghanistan',
        'Albania',
        'Algeria',
        'Andorra',
        'Angola',
        'Antigua and Barbuda',
        'Argentina',
        'Armenia',
        'Australia',
        'Austria',
        'Azerbaijan',
        'Commonwealth oftheBahamas',
        'Bahrain',
        'Bangladesh',
        'Barbados',
        'Belarus',
        'Belgium',
        'Belize',
        'Benin',
        'Bhutan',
        'Bolivia',
        'Bosnia and Herzegovina',
        'Botswana',
        'Brazil',
        'Brunei',
        'Bulgaria',
        'Burkina Faso',
        'BurundiCambodia',
        'Cameroon',
        'Canada',
        'Cape Verde',
        'Catalen',
        'Central African Republic',
        'Chad',
        'Chile',
        'China',
        'Colombia',
        'Comoros',
        'Congo (Brazzaville)',
        'Congo (Kinshasa)',
        'Cook Islands',
        'Costa Rica',
        'Côte d\'Ivoire',
        'Croatia',
        'Cuba',
        'Cyprus',
        'Czech Republic',
        'Denmark',
        'Djibouti',
        'Donetsk People\'s Republic',
        'Dominica',
        'Dominican Republic',
        'Ecuador',
        'Egypt',
        'El Salvador',
        'Equatorial Guinea',
        'Eritrea',
        'Estonia',
        'Ethiopia',
        'Fiji',
        'Finland',
        'France',
        'Gabon',
        'Gambia',
        'Georgia',
        'Germany',
        'Ghana',
        'Greece',
        'Grenada',
        'Guatemala',
        'Guinea',
        'Guinea-Bissau',
        'Guyana',
        'Haiti',
        'Honduras',
        'Hungary',
        'Iceland',
        'India',
        'Indonesia',
        'Iran',
        'Iraq',
        'Ireland',
        'Israel',
        'Italy',
        'Jamaica',
        'Japan',
        'Jordan',
        'Kazakhstan',
        'Kenya',
        'Kiribati',
        'South Korea',
        'Kosovo',
        'Kuwait',
        'Kyrgyzstan',
        'Laos',
        'Latvia',
        'Lebanon',
        'Lesotho',
        'Liberia',
        'Libya',
        'Liechtenstein',
        'Lithuania',
        'Luxembourg',
        'Madagascar',
        'Malawi',
        'Malaysia',
        'Maldives',
        'Maltese Knights',
        'Mali',
        'Malta',
        'Marshall Islands',
        'Mauritania',
        'Mauritius',
        'Mexico',
        'Micronesia',
        'Moldova',
        'Monaco',
        'Mongolia',
        'Montenegro',
        'Morocco',
        'Mozambique',
        'Myanmar',
        'Nagorno-Karabakh',
        'Namibia',
        'Nauru',
        'Nepal',
        'Netherlands',
        'New Zealand',
        'Nicaragua',
        'Niger',
        'Nigeria',
        'Niue',
        'Northern Cyprus',
        'North Macedonia',
        'Norway',
        'Oman',
        'Pakistan',
        'Palau',
        'Palestine',
        'Panama',
        'Papua New Guinea',
        'Paraguay',
        'People\'s Republic of Korea',
        'Peru',
        'Philippines',
        'Poland',
        'Portugal',
        'Pridnestrovie',
        'Puntland',
        'Qatar',
        'Romania',
        'Russia',
        'Rwanda',
        'Saint Christopher and Nevis',
        'Saint Lucia',
        'Saint Vincent and the Grenadines',
        'Samoa',
        'San Marino',
        'São Tomé and Príncipe',
        'Saudi Arabia',
        'Senegal',
        'Serbia',
        'Seychelles',
        'Sierra Leone',
        'Singapore',
        'Slovakia',
        'Slovenia',
        'Solomon Islands',
        'Somali',
        'Somaliland',
        'South Africa',
        'South Ossetia',
        'South Sudan',
        'Spain',
        'Sri Lanka',
        'Sudan',
        'Suriname',
        'Swaziland',
        'Sweden',
        'Switzerland',
        'Syria',
        'Tajikistan',
        'Tanzania',
        'Thailand',
        'Timor-Leste',
        'Togo',
        'Tonga',
        'Trinidad and Tobago',
        'Tunisia',
        'Turkey',
        'Turkmenistan',
        'Tuvalu',
        'Uganda',
        'Ukraine',
        'United Arab Emirates',
        'United Kingdom',
        'United States',
        'Uruguay',
        'Uzbekistan',
        'Vanuatu',
        'Vatican city(the Holy see)',
        'Venezuela',
        'Vietnam',
        'Western Sahara',
        'Yemen',
        'Zambia',
        'Zimbabwe'
    ]
    for nation in nationList:
        if Str.find(nation) != -1:
            return nation
    return -1

def CreateJournalCategoryDict():
    """
    Create a dict of ESI Journal list for charging the simple journal name to full name
    :return: dict with ESI Journal
    """
    '''
    python3.9以上不在支持xlrd1.2，xlrd1.2以上不在支持xlsx文件格式
    # table = xlrd.open_workbook(r'./esi-master-journal-list-4-2021.xlsx').sheets()[0]
    # fullTitle = np.matrix(table.col_values(0)).tolist()[0]
    # Title_2017 = np.matrix(table.col_values(1)).tolist()[0]
    # Title_2019 = np.matrix(table.col_values(2)).tolist()[0]
    # esiCategory = np.matrix(table.col_values(5)).tolist()[0]
    '''

    # 获取索引为index的sheet表格
    # 用 pandas 读取 Excel 文件的第一个sheet
    df = pd.read_excel(r"C:\Users\AI\IdeaProjects\dlut-research-service\src\main\flask\data\esi-master-journal-list-4-2021.xlsx", sheet_name=0)

    # 获取所需的列并将它们转换为列表
    fullTitle = df.iloc[:, 0].tolist()
    Title_2017 = df.iloc[:, 1].tolist()
    Title_2019 = df.iloc[:, 2].tolist()
    esiCategory = df.iloc[:, 5].tolist()
    journalList = [fullTitle, Title_2017, Title_2019, esiCategory]
    esi_dict = {journalList[0][i].upper(): journalList[3][i] for i in range(0, len(journalList[3]))}
    esi_dict1 = {journalList[1][i].upper(): journalList[3][i] for i in range(0, len(journalList[3]))}
    esi_dict2 = {journalList[2][i].upper(): journalList[3][i] for i in range(0, len(journalList[3]))}
    esi_dict1.update(esi_dict2)
    esi_dict.update(esi_dict1)
    return esi_dict

def seq_annotation(data):
    tokenizer = RobertaTokenizer.from_pretrained("roberta-base")
    tokenors = tokenizer(data['seq'], padding='max_length', truncation=True, max_length=96, return_tensors="pt")
    output = model(tokenors['input_ids'], tokenors['attention_mask'], torch.tensor(data['rel']))[0]
    output = output.argmax(dim=1).tolist()
    return {"seq": data['seq'][1:], "label": output[1:]}

def object_to_dict(obj):
    return obj.__dict__

def convert_to_excel(file):
    file_content = file.read().decode('utf-8')
    file_content = file_content.replace('\r', '')
    titles = file_content.split("\nER\n")
    wos_paper = []
    for title in titles:
        wos_data = DealPaperInformation(title)
        AF = ''
        if wos_data.AF is not None:
            for j in range(len(wos_data.AF)):
                if wos_data.AF[j] is not None:
                    AF += wos_data.AF[j].AuthorName + "; "
        wos_data.AF = AF
        wos_paper.append(wos_data)
    wos_dict = [object_to_dict(obj) for obj in wos_paper]
    df = pd.DataFrame(wos_dict)
    return df

def split_sentence(abstract):
    abstract = replace_abbreviations(abstract)
    ab_seqs = sent_tokenize(abstract)
    return ab_seqs

def replace_abbreviations(text):
    # 常见省略词
    abbreviations = {
        "e.g.": "for example",
        "i.e.": "that is",
        "etc.": "and so on",
        "viz.": "namely",
        "cf.": "compare",
        "vs.": "versus",
        "esp.": "especially",
        "a.m.": "before noon",
        "p.m.": "after noon",
        "Dr.": "Doctor",
        "Mr.": "Mister",
        "Mrs.": "Mistress",
        "Ms.": "Miss",
        "Prof.": "Professor",
        "Sr.": "Senior",
        "Jr.": "Junior",
        "Inc.": "Incorporated",
        "Ltd.": "Limited",
        "Co.": "Company",
        "Corp.": "Corporation",
        "Pty.": "Proprietary",
        "Plc.": "Public limited company",
        "LLC.": "Limited Liability Company",
        "LP.": "Limited Partnership",
        "LLP.": "Limited Liability Partnership",
        "No.": "number",
        "St.": "Street",
        "Ave.": "Avenue",
        "Blvd.": "Boulevard",
        "Mt.": "Mount",
        "Ft.": "Fort",
        "Dept.": "Department",
        "Univ.": "University",
        "Assn.": "Association",
        "Bros.": "Brothers",
        "Est.": "Established",
        "Fig.": "Figure",
        "Max.": "Maximum",
        "Min.": "Minimum",
        "Gov.": "Governor",
        "Sgt.": "Sergeant",
        "Capt.": "Captain",
        "Gen.": "General",
        "Lt.": "Lieutenant",
        "Col.": "Colonel",
        "Adm.": "Admiral",
        "Pres.": "President",
        "VP.": "Vice President",
        "Sec.": "Secretary",
        "Treas.": "Treasurer"
    }


    pattern = re.compile(
        r'\b(' + '|'.join(re.escape(abbreviation) for abbreviation in abbreviations.keys()) + r')(?=\W|$)')

    def replace(match):
        return abbreviations[match.group(0)]

    # Replace abbreviations in the provided text
    return pattern.sub(replace, text)


if __name__ == '__main__':
    titles = get_titles('../data/Test.txt')
    wos_js = []
    for title in titles:
        wosdata = DealPaperInformation(title, "AB", "ab_seq")
        print(wosdata)
        # break
        print('B:' + wosdata.r_background)
        print("-----")
        print("M:" + wosdata.r_method)
        print("-----")
        print("R:" + wosdata.r_result)
        print("-----")
        print("C:" + wosdata.r_conclusion)

        print("----------------next------------------")
        # wos_js.append(wosdata.to_json())
    # 将整个列表转换为 JSON 字符串
    # wos_js_json = json.dumps(wos_js, ensure_ascii=False, indent=4)

    # 将 JSON 字符串写入文件
    # with open('../data/1.json', 'w', encoding='utf-8') as f:
        # f.write(wos_js_json)

