package com.biao;

import java.util.Random;

/**
 *  ""(Myth)
 */
public class Test {
    public static void main(String[] args) {


        final String paramA = "ab";
        final String paramB = "ba";
        final String form = fun(paramA, paramB);
        final String to = fun(paramB, paramA);
        System.out.println(to.equals(form));
        System.out.println(form);
        System.out.println(to);

    }

    public static String fun(String paramA, String paramB) {
        if (paramA.compareTo(paramB) <= 0) {
            return paramA + "-" + paramB;
        } else {
            return paramB + "-" + paramA;
        }
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
