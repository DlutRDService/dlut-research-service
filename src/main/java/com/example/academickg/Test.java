package com.example.academickg;


public class Test {

    public static void main(String[] args) {
        String s = "and";
        StrValidate strValidate = new StrValidate();
        strValidate.splitQueryField("(TS=anb and TS=op) or TS=12");
        System.out.println(1);
    }
}
