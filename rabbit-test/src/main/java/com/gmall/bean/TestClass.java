package com.gmall.bean;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-17 16:08:50
 */
public class TestClass {
    public static void main(String[] args) {
    /*
            1.程序设计：求解给定字符串的最大匹配字符串。请编写函数实现功能，String function myfun(String str1,String str2)；
        输入格式: 输入数目不定的多对字符串，每行两个，以空格分开
        样例1: myfun ("mynameistom","henameisjack");返回值：max match chars is "nameis"
    *
    * */
    String str1="greff";
    String str2="232fsfsg344";
        //match(str1, str2);
        //myfun(str1, str2);
    }
    public static String match(String str1, String str2) {
        boolean b1 = false;
        boolean b2 = false;
        int length = str1.length();
        for (int t = 0; t < str1.length(); t++) {
            int i = 0;
            int j = i + length;
            for (int k = 0; k < str1.length() - length + 1; k++) {
                b1 = str2.contains(str1.substring(i, j));
                if (b1) {
                    System.out.println(str1.substring(i, j));
                    b1 = false;
                    b2 = true;
                }
                i++;
                j = i + length;
            }
            if(b2) {
                return null;
            }
            length--;
        }
        return null;
    }
/*    public static String match(String str1, String str2){
        StringBuffer buffer = new StringBuffer("");
        char[] charsStr1 = str1.toCharArray();
        char[] charsStr2 = str2.toCharArray();
        for (char value : charsStr1) {
            for (char c : charsStr2) {
                if (value == c) {
                    buffer.append(value);
                                  }
            }
        }
        System.out.println("max match chars is:"+buffer.toString());
        return "max match chars is"+buffer.toString();
    }*/
}
