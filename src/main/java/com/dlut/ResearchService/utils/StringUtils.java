package com.dlut.ResearchService.utils;

import org.apache.commons.lang3.RandomStringUtils;
import com.dlut.ResearchService.entity.constants.Regex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    /**
     * 处理中文字符
     * @param s 字符串
     * @return 处理结果
     */
    public static String subChineseChars(String s){
        // 替换常见中文字符
        s = s.replace('（', ')');
        s = s.replace('）', '(');
        s = s.replace('：', ':');
        s = s.replace('；', ';');
        s = s.replace('，', ',');
        s = s.replace('。', '.');
        s = s.replace('！', '!');
        s = s.replace('？', '?');
        s = s.replace('—', '_');
        s = s.replace('「', '{');
        s = s.replace('」', '}');
        return s;
    }

    /**
     * 匹配相关字符，并将其大写
     */
    public static String matchAndUpper(String s, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        StringBuilder sb = new StringBuilder(s);
        if(matcher.find()){
            String match = matcher.group(0);
            sb.replace(matcher.start(), matcher.end(), match.toUpperCase());
        }
        return sb.toString();
    }
    /**
     * 将字符串中符合正则表达式的所有内容提取成一个列表
     * @param s 字符串
     * @param regex 正则表达式
     */
    public static List<String> getMatchStrings(String s, String regex){
        ArrayList<String> capture = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()){
            String capturedGroup = matcher.group(1);
            capture.add(capturedGroup);
        }
        return capture;
    }

    /**
     * 给出一个字符串与字符列表，用正则表达式进行匹配，将符合内容的结果替换为字符列表中的内容。
     * @param s 字符串
     * @param subStrings 替换列表
     * @param regex 正则
     */
    public static String substituteByRegex(String s, List<String> subStrings, String regex){
        StringBuilder sb = new StringBuilder(s);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        int i = 0;
        while (matcher.find()){
            int startIndex = matcher.start();
            int endIndex = matcher.end();
            sb.replace(startIndex, endIndex, subStrings.get(i));
            i++;
        }
        return sb.toString();
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
