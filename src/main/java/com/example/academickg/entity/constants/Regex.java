package com.example.academickg.entity.constants;

public class Regex {
    // 纯数字
    public static final String DIGIT_REGEX = "[0-9]+";
    // 含有数字
    public static final String CONTAIN_DIGIT_REGEX = ".*[0-9].*";
    // 纯字母
    public static final String LETTER_REGEX = "[a-zA-Z]+";
    // 包含字母
    public static final String CONTAIN_LETTER_REGEX = ".*[a-zA-z].*";
    // 纯中文
    public static final String CHINESE_REGEX = "[一-龥]";
    // 仅仅包含字母和数字
    public static final String LETTER_DIGIT_REGEX = "^[a-z0-9A-Z]+$";
    public static final String CHINESE_LETTER_REGEX = "([一-龥]+|[a-zA-Z]+)";
    public static final String CHINESE_LETTER_DIGIT_REGEX = "^[a-z0-9A-Z一-龥]+$";
}
