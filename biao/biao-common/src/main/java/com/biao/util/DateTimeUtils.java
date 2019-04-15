package com.biao.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {


    public static String format(LocalDateTime time, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(time);
    }

    public static String format(String pattern) {
        return format(LocalDateTime.now(), pattern);
    }

    public static void main(String[] args) {
       String str = DateTimeUtils.format("yyyy-MM-dd");
        System.out.println(str);
    }
}
