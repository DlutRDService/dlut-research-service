package com.example.academickg.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ScriptTriggerUtils {
    public static void execute(String fileName, String argument){
        String line;
        Process proc;
        try {
            String py_path_prefix = "/home/zsl/IdeaProjects/AcademicKG/src/main/java/com/example/academickg/script/python/";
            String path = py_path_prefix + fileName;
            String[] shell = {"python3", path, argument};
            proc = Runtime.getRuntime().exec(shell);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void execute(String fileName, String argument, String num){
        Process proc;
        try {
            String py_path_prefix = "/home/zsl/IdeaProjects/AcademicKG/src/main/java/com/example/academickg/script/python/";
            String path = py_path_prefix + fileName;
            String[] shell = {"python3", path, argument, num};
            proc = Runtime.getRuntime().exec(shell);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
