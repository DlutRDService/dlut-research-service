package com.dlut.ResearchService.utils;

import com.dlut.ResearchService.entity.constants.StatusCode;
import org.apache.commons.lang3.RandomStringUtils;
import com.dlut.ResearchService.entity.constants.Regex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    public static String handleQueryField(String queryField){
        // 字符串转化为小写
        queryField = queryField.toLowerCase();
        // 替换中文字符
        queryField = queryField.replace('（', ')');
        queryField = queryField.replace('）', '(');
        queryField = queryField.replace('：', ':');
        queryField = queryField.replace('；', ';');
        queryField = queryField.replace('，', ',');
        queryField = queryField.replace('。', '.');
        return queryField;
    }

    /**
     * 生成随机数
     */
    public static String getRandomNumber(Integer count){
        return RandomStringUtils.random(count, false, true);
    }
    public static Boolean containNumOrChar(String s){
        return s.matches(Regex.CONTAIN_LETTER_REGEX) || s.contains(Regex.CONTAIN_DIGIT_REGEX);
    }

    /**
     * 将AND、NOT、OR三个Boolean关键字替换为逻辑运算符号
     */
    public static String substituteLogicalOperators(String s){
        s = s.replace(" and ", "&&");
        s = s.replace(" or ", "||");
        s = s.replace(" not ", "！");
        return s;
    }
    public static HashMap<String, Integer> singleSetRegexQueryMatch(String s, String regex){
        HashMap<String, Integer> hashMap = new HashMap<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        int first;
        int end = 0;
        while (matcher.find()){
            if (matcher.groupCount() == 2){
                hashMap.put(matcher.group(),1);
                return hashMap;
            }
            first = s.indexOf(matcher.group());
            String subQuery = s.substring(end, first);
            end = s.lastIndexOf(matcher.group());
            subQuery = StringUtils.getRegexMatchContent(subQuery, Regex.FORMAT_QUERY);
            if (s.substring(first, end).contains(Regex.AND_MATCH)){
                hashMap.put(subQuery, 1);
            }
            if (s.substring(first, end).contains(Regex.OR_MATCH)){
                hashMap.put(subQuery, 2);
            }
            if (s.substring(first, end).contains(Regex.NOT_MATCH)){
                hashMap.put(subQuery, 3);
            }
        }
        return hashMap;
    }
    public static String getRegexMatchContent(String s, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.group();
    }

    public static List<Float> toFLoatList(List<String> stringList){
        List<Float> floats = new ArrayList<>(stringList.size());
        for (String s : stringList) {
            if (s!=null){
                floats.add(toFloat(s));
            }
        }
        return floats;
    }

    public static Float toFloat(String s){
        return Float.parseFloat(s);
    }
}
