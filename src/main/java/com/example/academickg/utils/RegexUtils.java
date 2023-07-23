package com.example.academickg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    private static String regex;
    public static ArrayList<String> findMatchField(String regex, String s){
        RegexUtils.regex = regex;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        ArrayList<String> result = new ArrayList<>();
        while (matcher.find()){
            result.add(matcher.group());
        }
        return result;
    }
}
