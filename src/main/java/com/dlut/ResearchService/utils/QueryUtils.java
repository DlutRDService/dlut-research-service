package com.dlut.ResearchService.utils;

import com.dlut.ResearchService.entity.constants.Regex;
import com.dlut.ResearchService.entity.constants.TreeNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryUtils {
    public static List<String> stringToPrefixString(String queryField){
        ArrayList<String> treeList = new ArrayList<>();
        treeList.add("(");
        treeList.add("AND");
        Pattern pattern = Pattern.compile(Regex.MATCH_EACH_FIELD_OR_OPERATOR_OR_BRACKET);
        Matcher matcher = pattern.matcher(queryField);
        while (matcher.find()){
            treeList.add(matcher.group(0));
        }
        treeList.add(")");
        return treeList;
    }

    public static TreeNode prefixStringToTreeNode(@NotNull List<String> tokens){
        Stack<TreeNode> stack = new Stack<>();
        for (String token : tokens) {
            if (token.equals("(")){
                stack.push(new TreeNode(token));
            }else if (token.equals(")")){
                TreeNode subTree = null;
                while (!stack.isEmpty() && !stack.peek().value.equals("(")) {
                    TreeNode node = stack.pop();
                    if (subTree != null) {
                        node.right = subTree;
                    }
                    subTree = node;
                }
                // 弹出左括号
                stack.pop();
                // 子树入栈
                stack.push(subTree);
            } else {
                // 遇到操作符或操作数，创建一个新节点并入栈
                stack.push(new TreeNode(token));
            }
        }
        // 最后栈中剩下的节点就是根节点
        return stack.pop();
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
}
