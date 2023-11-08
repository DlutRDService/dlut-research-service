package com.dlut.ResearchService.utils;

import com.dlut.ResearchService.entity.constants.Regex;
import com.dlut.ResearchService.entity.constants.TreeNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryUtils {
    @NotNull
    public static String trimWhitespace(@NotNull String s){
        if (s.matches("\\(") || s.matches("\\)") || s.matches("=")){
            s = s.replaceAll(" *\\( *", "(");
            s = s.replaceAll(" *\\) *", "(");
            s = s.replaceAll(" *= *", "=");
        }
        return s;
    }

    /**
     * 字符串转化为中缀表达式
     * @param s 字符串
     * @return 中缀表达式
     */
    @NotNull
    public static List<String> queryToInfixString(String s){
        List<String> treeList = new ArrayList<>();
        Pattern pattern = Pattern.compile(Regex.MATCH_EACH_FIELD_OR_OPERATOR_OR_BRACKET);
        Matcher matcher = pattern.matcher(s);
        treeList.add("(");
        while (matcher.find()){
            treeList.add(matcher.group(0));
        }
        treeList.add(")");
        return treeList;
    }

    /**
     * 中缀表达式转解析树
     * @param tokens 中缀表达式列表
     * @return 解析树
     */
    public static TreeNode infixStringToTreeNode(@NotNull List<String> tokens){
        Stack<TreeNode> stack = new Stack<>();
        for (String token : tokens) {
            if (token.equals("(")){
                stack.push(new TreeNode(token));
            }else if (token.equals(")")){
                TreeNode subTree = null;
                while (!stack.isEmpty() && !stack.peek().value.equals("(")) {
                    TreeNode node = stack.pop();
                    if (subTree != null
                            &&(node.value.equals("AND")
                            || node.value.equals("NOT")
                            || node.value.equals("OR"))
                            && node.left == null && node.right == null
                    ) {
                        node.right = subTree;
                        node.left = stack.pop();
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
        Set<Integer> integers = new HashSet<>(getEvaluateNode(node.left));
        switch (node.value.toString()) {
            case "AND" -> integers.retainAll(getEvaluateNode(node.right));
            case "OR" -> integers.addAll(getEvaluateNode(node.right));
            case "NOT" -> integers.removeAll(getEvaluateNode(node.right));
            default -> {
            }
        }
        return integers;
    }

    /**
     * 匹配相关字符，并将其大写
     */
    @NotNull
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
