package com.dlut.ResearchService.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class ScriptTriggerUtils {
    // TODO 修改成可以传多个参数
    public static void execute(String filePath, String argument){
        String line;
        Process proc;
        try {
            String[] shell = {"python3", filePath, argument};
            proc = Runtime.getRuntime().exec(shell);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error(e.toString());
        }
    }

    public static void execute(String filePath, String argument, String num){
        Process proc;
        try {
            String[] shell = {"python3", filePath, argument, num};
            proc = Runtime.getRuntime().exec(shell);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error(e.toString());
        }
    }
}
