# !/usr/bin/python3.10
# -*- coding:UTF-8 -*-

from dataprocess import WosData, AuthorInformation


def DealPaperInformation(title, WC=None, Esi_dict=None):
    """
    读取统计文献的名称、期刊名、期刊ESI类别、WC类别、年份、引文、摘要、关键词、作者、作者机构、国家
    :param Esi_dict: 字典，用于获取文献的ESI类别
    :param title: 传入文献
    :param WC: 要获取统计字段
    :return:返回论文全部信息，格式为类
    """
    wos_data = WosData()
    # 是否按照WC类别处理
    if WC is None:
        pass
    else:
        flg = 0
        for i in WC:
            if title[title.find('\nWC '):title.find('\nSC ')].find(i) != -1:
                flg = 1
                break
        if flg == 0:
            return wos_data
    title = title.split('\n')
    wos_data.WC = WC
    for num, line in enumerate(title):
        # 作者
        if line.find('AF ') == 0:
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
        if line.find('TI ') == 0:
            wos_data.TI_name = line[3:].replace('\'', '').replace('\"', '').replace('\\', '')
            i = 1
            while title[num + i][0:3] == '   ':
                wos_data.TI_name += ' ' + title[num + i][3:].replace('\'', '').replace('\"', '').replace('\\', '')
                i += 1
            continue
        # 期刊
        if line.find('SO ') == 0:
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
        # 关键字
        if line.find("DE ") == 0:
            keywords = line[3:].replace('\'', '').replace('\"', '')
            i = 1
            while title[num + i][0:3] == '   ':
                keywords += ' ' + title[num + i][3:].replace('\'', '').replace('\"', '')
                i += 1
            wos_data.DE = keywords.split('; ')
            continue

        if line.find("AB ") == 0:
            wos_data.AB = ''
            pass
        # 作者所在国家、作者所在机构
        if line.find('C1 ') == 0:
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
            if line.find('RP ') == 0:
                # 统计国家
                for AF in wos_data.AF:
                    if CheckNation(line) != -1:
                        wos_data.Nation.append(CheckNation(line))
                        AF.AuthorNation = wos_data.Nation[0]
                continue
        # 引文数量
        if line.find("NR ") == 0:
            wos_data.NR = line[3:]
            continue
        # 被引
        if line.find("TC ") == 0:
            wos_data.TC = line[3:]
        # WC 类别
        if line.find("WC ") == 0:
            wc = line[3:].strip('\n')
            i = 1
            while title[num + i][0:3] == '   ':
                wc += ' ' + title[num + i][3:].strip('\n')
                i += 1
            wos_data.WC = wc.split('; ')
            continue
        if line.find('PY ') == 0 or line.find('EA ') == 0:
            wos_data.PY = line[-4:]
            continue
    for i in range(len(wos_data.AF)):
        if wos_data.AF[i].AuthorOrganization is None:
            wos_data.AF[i].AuthorOrganization = ' '
        if wos_data.AF[i].AuthorNation is None:
            wos_data.AF[i].AuthorNation = ' '
    wos_data.Nation = list(set(wos_data.Nation))
    wos_data.Organization = list(set(wos_data.Organization))
    wos_data.DE = list(set(wos_data.DE))
    return wos_data


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
