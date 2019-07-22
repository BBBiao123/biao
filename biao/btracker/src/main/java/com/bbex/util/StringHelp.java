package com.bbex.util;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringHelp {

    /**
     * 将集合转成字符串
     *
     * @param lists
     * @return
     */
    public static String turnString(List<String> lists) {
        StringBuilder builder = new StringBuilder();
        for (String list : lists) {
            builder.append("'");
            builder.append(list);
            builder.append("'");
            builder.append(",");
        }
        return substring(builder.toString());
    }

    public static boolean isArrayStr(String arrayStr) {
        if (StringUtils.isBlank(arrayStr)) {
            return false;
        }
        String start = arrayStr.substring(0, 1);
        String end = arrayStr.substring(arrayStr.length() - 1, arrayStr.length());
        if (start.equals("[") && end.equals("]")) {
            return true;
        }
        return false;
    }

    /**
     * 将集合转成字符串
     *
     * @param lists
     * @return
     */
    public static String turnInt(List<Integer> lists) {
        StringBuilder builder = new StringBuilder();
        for (Integer list : lists) {
            builder.append(list);
            builder.append(",");
        }
        return substring(builder.toString());
    }

    /**
     * 截取字符串(去掉最后一个逗号符号位)
     *
     * @param str
     * @return
     */
    public static String substring(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        if (str.endsWith(",")) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 截取字符串(去掉最后一个符号位)
     *
     * @param str
     * @param symbol
     * @return
     */
    public static String substring(String str, String symbol) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        if (str.endsWith(symbol)) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 截取字符串(0,length-1)
     *
     * @param str
     * @return
     */
    public static String substringLast(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * 构建全like查询字符串
     *
     * @param cloumn
     * @return
     */
    public static String likeStr(String cloumn) {
        StringBuilder builder = new StringBuilder();
        builder.append("LIKE CONCAT");
        builder.append("(CONCAT('%',#{");
        builder.append(cloumn);
        builder.append("}),'%')");
        return builder.toString();
    }

    /**
     * 构建后like查询字符串
     *
     * @param cloumn
     * @return
     */
    public static String likeLastStr(String cloumn) {
        StringBuilder builder = new StringBuilder();
        builder.append("LIKE CONCAT");
        builder.append("(#{");
        builder.append(cloumn);
        builder.append("},'%')");
        return builder.toString();
    }

    /**
     * byte[]类型的字符串转为byte数组
     *
     * @param byteStr
     * @return
     */
    public static byte[] parseStrByte(String byteStr) {
        String[] split = byteStr.substring(1, byteStr.length() - 1).split(",");
        byte[] buff = new byte[split.length];
        for (int i = 0; i < split.length; i++) {
            buff[i] = Byte.parseByte(split[i]);
        }
        return buff;
    }

    /**
     * int[]类型的字符串转为int数组
     *
     * @param byteStr
     * @return
     */
    public static int[] parseStrInt(String byteStr) {
        String[] split = byteStr.substring(1, byteStr.length() - 1).split(",");
        int[] buff = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            buff[i] = Integer.parseInt(split[i]);
        }
        return buff;
    }

    /**
     * byte数组转为字符串[2,1,3]
     *
     * @param buff
     * @return
     */
    public static String StringToByteStr(String[] buff) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < buff.length; i++) {
            //NOTE YeBing 如果传过来的值非-127~127则会抛异常
            builder.append((byte) Integer.valueOf(buff[i]).intValue() + ",");
            //builder.append(Byte.parseByte(buff[i])+",");
        }
        return "[" + StringHelp.substring(builder.toString()) + "]";
    }

    /**
     * byte数组转为字符串[2,1,3]
     *
     * @param buff
     * @return
     */
    public static String StringToIntStr(String[] buff) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < buff.length; i++) {
            builder.append(Integer.parseInt(buff[i]) + ",");
        }
        return "[" + StringHelp.substring(builder.toString()) + "]";
    }

    /**
     * byte数组转为字符串[2,1,3]
     *
     * @param buff
     * @return
     */
    public static String byteToStr(byte[] buff) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < buff.length; i++) {
            builder.append(buff[i] + ",");
        }
        return "[" + StringHelp.substring(builder.toString()) + "]";
    }

    /**
     * byte数组转为字符串[2,1,3]
     *
     * @param buff
     * @return
     */
    public static String byteToStr(Byte[] buff) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < buff.length; i++) {
            builder.append(buff[i] + ",");
        }
        return "[" + StringHelp.substring(builder.toString()) + "]";
    }

    /**
     * int数组转为字符串[2,1,3]
     *
     * @param buff
     * @return
     */
    public static String intToStr(int[] buff) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < buff.length; i++) {
            builder.append(buff[i] + ",");
        }
        return "[" + StringHelp.substring(builder.toString()) + "]";
    }

    /**
     * integer数组转为字符串[2,1,3]
     *
     * @param buff
     * @return
     */
    public static String integerToStr(Integer[] buff) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < buff.length; i++) {
            builder.append(buff[i] + ",");
        }
        return "[" + StringHelp.substring(builder.toString()) + "]";
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            Pattern regex = Pattern.compile("^(1\\d{10,11})$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 是否包含
     *
     * @param excludes 数字字符串列表
     * @param target   目标数字
     * @return true 包含,false 不包含
     */
    public static boolean isInclude(String excludes, Integer target) {
        if (StringUtils.isBlank(excludes)) {
            return false;
        }
        String[] arrays = excludes.split(",");
        List<Integer> exDtIds = CollectionHelp.arrayToIntegerList(arrays);
        if (exDtIds.contains(target)) {
            return true;
        }
        return false;
    }

    /**
     * 验证是否为正整数
     *
     * @return
     */
    public static boolean regexPositiveValue(String value) {
        return regexMatcher("^([1-9]*[1-9][0-9]*)|0$", value);
    }

    /**
     * 判断正则表达式是否匹配
     *
     * @param regexPattern
     * @param value
     * @return true:匹配
     */
    public static boolean regexMatcher(String regexPattern, String value) {
        Pattern pattern = Pattern.compile(regexPattern);
        return pattern.matcher(value).matches();
    }

    public static boolean regexFirstFind(String regexPattern, String value) {
        Pattern pattern = Pattern.compile(regexPattern);
        return pattern.matcher(value).find();
    }

    public static String regexFirstMatcherStr(String regexPattern, String value) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * int类型是否有效且大于0
     *
     * @param number
     * @return
     */
    public static boolean isIntGt0(Integer number) {
        return number != null && number > 0;
    }

    public static String getHostIp() {
        String ip = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            addr.getHostAddress();
            if (StringUtils.isNotBlank(ip)) {
                return ip;
            }
        } catch (Exception e) {
        }
        try {
            Enumeration<NetworkInterface> netInterfaces = null;
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                String tempIp = null;
                while (ips.hasMoreElements()) {
                    tempIp = ips.nextElement().getHostAddress();
                    if (regexMatcher("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}", tempIp)) {
                        ip = tempIp;
                        break;
                    }
                }
                if (StringUtils.isNotBlank(ip)) {
                    break;
                }
            }
        } catch (Exception e) {
        }
        return ip;

    }

    public static String getWindowHostIp() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress();
            return ip;
        } catch (Exception e) {
            throw new RuntimeException("获取本机ip异常");
        }
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        if (str.startsWith(prefix)) {
            return true;
        }
        if (str.length() < prefix.length()) {
            return false;
        }
        String lcStr = str.substring(0, prefix.length()).toLowerCase();
        String lcPrefix = prefix.toLowerCase();
        return lcStr.equals(lcPrefix);
    }

    public static boolean hasText(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }
}
