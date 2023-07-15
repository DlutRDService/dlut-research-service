package com.example.academickg.utils;

import org.apache.commons.lang3.RandomStringUtils;
import com.example.academickg.entity.constants.Regex;

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
    public String[] splitQueryField(String s){
        if (s.contains("and =") || s.contains(") or (") || s.contains(""))
            if (s.contains(" and ") || s.contains(" or ")) {
                String [] fields = s.split(" and | or ");
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].indexOf("=") != 2 ){

                    }

                }
                //System.out.println(Arrays.toString(field));

            }
        if (s.contains("and") && s.contains("(")){

        }

        return null;
    }
}
