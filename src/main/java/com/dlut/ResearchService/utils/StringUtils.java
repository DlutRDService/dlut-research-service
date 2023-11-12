package com.dlut.ResearchService.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.RandomStringUtils;
import com.dlut.ResearchService.entity.constants.Regex;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    public static String replaceChineseChars(String s){
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
    @NotNull
    public static List<String> getMatchStringList(String s, String regex){
        ArrayList<String> captureList = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()){
            String capturedGroup = matcher.group(1);
            captureList.add(capturedGroup);
        }
        return captureList;
    }

    /**
     * 给出一个字符串与字符列表，用正则表达式进行匹配，将符合内容的结果一一替换为字符列表中的内容
     * @param s 字符串
     * @param subStrings 替换列表
     * @param regex 正则
     */
    @NotNull
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
    @NotNull
    public static String getRandomNumber(Integer count){
        return RandomStringUtils.random(count, false, true);
    }

    /**
     * 判断字符串中是否包含字符
     */
    @NotNull
    @Contract(pure = true)
    public static Boolean containLetter(@NotNull String s){
        return s.matches(Regex.CONTAIN_LETTER_REGEX);
    }

    /**
     * 得到匹配内容
     * @param s 字符串
     * @param regex 正则表达式
     * @return 匹配内容
     */
    public static String getRegexMatchContent(String s, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.group();
    }

    /**
     * String列表转Float列表
     * @param stringList 字符串列表
     * @return Float列表
     */
    @NotNull
    public static List<Float> stringListToFLoatList(@NotNull List<String> stringList){
        List<Float> floats = new ArrayList<>(stringList.size());
        for (String s : stringList) {
            if (s!=null){
                floats.add(Float.parseFloat(s));
            }
        }
        return floats;
    }

    /**
     * String列表转Integer列表
     * @param stringList 字符串列表
     * @return Integer列表
     */
    @NotNull
    public static List<Integer> stringListToIntegerList(@NotNull List<String> stringList){
        List<Integer> integers = new ArrayList<>(stringList.size());
        for (String s : stringList) {
            if (s != null) {
                integers.add(Integer.parseInt(s));
            }
        }
        return integers;
    }

    /**
     * 统计目标字符数
     * @param s 字符串
     * @param targetChar 目标字符
     * @return 字符串中包含的字符数量
     */
    public static Integer getCharCount(@NotNull String s, char targetChar){
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == targetChar) {
                count++;
            }
        }
        return count;
    }

    public static List<String> stringJsonToList(@NotNull String s){
         return JSON.parseArray(s, String.class);
    }
}
