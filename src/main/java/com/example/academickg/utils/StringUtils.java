package com.example.academickg.utils;

import org.apache.commons.lang3.RandomStringUtils;
import com.example.academickg.entity.constants.Regex;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * 生成随机数
     */
    public static String getRandomNumber(Integer count){
        return RandomStringUtils.random(count, false, true);
    }

    public Boolean containNumOrChar(String s){
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
}
