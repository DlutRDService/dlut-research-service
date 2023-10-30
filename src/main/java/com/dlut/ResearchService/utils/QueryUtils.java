package com.dlut.ResearchService.utils;

import com.dlut.ResearchService.entity.constants.Regex;
import com.dlut.ResearchService.entity.constants.TreeNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryUtils {
    public static List<String> queryToInfixString(String queryField){
        ArrayList<String> treeList = new ArrayList<>();
        treeList.add("(");
        treeList.add("AND");
        Pattern pattern = Pattern.compile(Regex.MATCH_EACH_FIELD_OR_OPERATOR_OR_BRACKET);
        Matcher matcher = pattern.matcher(queryField);
        while (matcher.find()){
            // 匹配括号或者逻辑表达式
            if (matcher.group(0).matches(Regex.)){
                treeList.add(matcher.group(0));
                continue;
            }
            //
            if (matcher.group(0).matches())

        }
        treeList.add(")");
        return treeList;
    }

    public static TreeNode infixStringToTreeNode(@NotNull List<String> tokens){
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
     * 处理解析树
     * @param node 解析树
     * @return 返回结果列表Set类型
     */
    public static Set<Integer> getEvaluateNode(TreeNode node){
        if (node == null) {
            return null;
        }
        Set<Integer> result = new HashSet<>(getEvaluateNode(node.left));
        switch (node.value.toString()) {
            case "AND" -> result.retainAll(getEvaluateNode(node.right));
            case "OR" -> result.addAll(getEvaluateNode(node.right));
            case "NOT" -> result.removeAll(getEvaluateNode(node.right));
            default -> {
            }
        }
        return result;
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
