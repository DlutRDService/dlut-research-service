package com.dlut.ResearchService.utils;

import org.apache.commons.lang3.RandomStringUtils;
import com.dlut.ResearchService.entity.constants.Regex;

import java.util.ArrayList;
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
     * 给出一个字符串与字符列表，用正则表达式进行匹配，将符合内容的结果替换为字符列表中的内容，前提是内容要匹配
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

    /**
     * 判断字符串中是否包含字符
     */
    public static Boolean containLetter(String s){
        return s.matches(Regex.CONTAIN_LETTER_REGEX);
    }

    public static String getRegexMatchContent(String s, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.group();
    }

    public static List<Float> stringListToFLoatList(List<String> stringList){
        List<Float> floats = new ArrayList<>(stringList.size());
        for (String s : stringList) {
            if (s!=null){
                floats.add(toFloat(s));
            }
        }
        return floats;
    }

    public static Float stringToFloat(String s){
        return Float.parseFloat(s);
    }
}
