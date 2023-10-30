package com.dlut.ResearchService.entity.constants;

import com.dlut.ResearchService.annotation.Description;

public class Regex {
    @Description(value = "大工邮箱")
    public static final String DLUT_MAIL = ".*@(mail\\.dlut\\.edu\\.cn|dlut\\.edu\\.cn)$";
    @Description(value = "包含字母")
    public static final String CONTAIN_LETTER_REGEX = ".*[0-9a-zA-Z].*";
    @Description(value = "标准字段检索式", example = "au=")
    public static final String FORMAT_QUERY = "[a-z]{2}=.*";
    @Description(value = "", example = "au=")
    public static final String FORMAT_START_QUERY = "( \\([a-z]{2}=.*|[a-z]{2}=.*)";
    public static final String TS_FORMAT = " *ts *=(.*?)(?=\\)|\\s+(?=AND|OR|NOT)\\b|$)";
    // 判断一个检索式匹配格式
    public static final String JUDGE_ONLY_ONE_CONDITION_WITHOUT_BOOLEAN =
            "^(\\(| )*[a-z]{2} *=((?! and | not | or ).)*$";
    public static final String MATCH_ONLY_ONE_CONDITION_WITHOUT_BOOLEAN = "[a-z]{2} *=((?! and | not | or ).)*$";
    public static final String JUDGE_ONLY_ONE_CONDITION_WITH_BOOLEAN = "^(\\(| )*[a-z]{2} *=(.*( and | or | not ).*)*$";
    public static final String MATCH_ONLY_ONE_CONDITION_WITH_BOOLEAN = "[a-z]{2} *=(.*( and | or | not ).*)*$";
    public static final String MATCH_MULTI_CONDITION = "[a-z]{2} *=.*(and|or|not) *(\\(| *)[a-z]{2} *=";
    public static final String MATCH_BRACKET = "= *(\\(|\\))";
    public static final String TRUE_BOOLEAN_FORMAT = "(and|or|not) *(?=[a-z]{2} *=)";
    @Description(value = "匹配具有Boolean运算含义的and、not、or字段", example = "and au= | not (au=")
    public static final String MATCH_OPERATOR = "(and|or|not)(?= *(\\([a-z]{2} *=|[a-z]{2} *=))";
    @Description(value = "匹配每一个查询字段", example = "au=zsl")
    public static final String MATCH_EACH_QUERY_FIELD =
            "( *[a-z]{2} *=.*(?=\\) *[a-z]{2} *=)| *[a-z]{2} *=.*(?=[a-z]{2} *=))";
    @Description(value = "匹配查询字段或者布尔操作符或者括号")
    public static final String MATCH_EACH_FIELD_OR_OPERATOR_OR_BRACKET =
            "(" +
                    "([a-z]{2} *=.*(?=\\) *[a-z]{2} *=)|[a-z]{2} *=.*(?=[a-z]{2} *=))" +
                    "|(AND|NOT|OR)" +
                    "|(\\(|\\))" +
                    ")";
}
