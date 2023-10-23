package com.dlut.ResearchService.utils;

import java.util.*;

public class SetOperationsAlgorithm {

    public static Set<Integer> intersect(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }

    public static Set<Integer> union(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> result = new HashSet<>(set1);
        result.addAll(set2);
        return result;
    }

    public static Set<Integer> difference(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> result = new HashSet<>(set1);
        result.removeAll(set2);
        return result;
    }

    public static Set<Integer> mixedOperation(Map<String, Set<Integer>> setsMap, String expression) {
        Stack<Set<Integer>> setStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);
            if (Character.isLetter(c)) {
                StringBuilder setName = new StringBuilder();
                while (i < expression.length() && Character.isLetter(expression.charAt(i))) {
                    setName.append(expression.charAt(i));
                    i++;
                }
                setStack.push(setsMap.get(setName.toString()));
            } else if (c == '(') {
                operatorStack.push(c);
                i++;
            } else if (c == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    performOperation(setStack, operatorStack.pop(), setsMap);
                }
                operatorStack.pop(); // Pop '('
                i++;
            } else if (c == ' ') {
                i++;
            } else {
                while (!operatorStack.isEmpty() && getPrecedence(operatorStack.peek()) >= getPrecedence(c)) {
                    performOperation(setStack, operatorStack.pop(), setsMap);
                }
                operatorStack.push(c);
                i++;
            }
        }

        while (!operatorStack.isEmpty()) {
            performOperation(setStack, operatorStack.pop(), setsMap);
        }

        return setStack.pop();
    }

    public static void performOperation(Stack<Set<Integer>> setStack, char operator, Map<String, Set<Integer>> setsMap) {
        Set<Integer> right = setStack.pop();
        Set<Integer> left = setStack.pop();
        if (operator == '∩') {
            setStack.push(intersect(left, right));
        } else if (operator == '∪') {
            setStack.push(union(left, right));
        } else if (operator == '-') {
            setStack.push(difference(left, right));
        }
    }

    public static int getPrecedence(char operator) {
        if (operator == '∪') {
            return 1;
        } else if (operator == '∩') {
            return 2;
        } else if (operator == '-') {
            return 3;
        }
        return -1;
    }

}
