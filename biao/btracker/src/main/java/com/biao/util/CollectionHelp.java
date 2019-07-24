package com.biao.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class CollectionHelp {

    /**
     * set转成字符串
     *
     * @param hs
     * @return
     */
    public static String setToString(Set<String> hs) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = hs.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next() + ",");
        }
        if (sb.length() > 1) {
            return sb.toString().substring(0, sb.length() - 1);
        } else {
            return "";
        }

    }

    public static List<Integer> assemblyIntList(Integer... strings) {
        List<Integer> lists = new ArrayList<>();
        for (Integer string : strings) {
            lists.add(string);
        }
        return lists;
    }

    public static List<String> assemblyStrList(String... strings) {
        List<String> lists = new ArrayList<>();
        for (String string : strings) {
            lists.add(string);
        }
        return lists;
    }

    public static String strListToString(List<String> lists) {
        return strListToString(lists, ",");
    }

    public static String strListToString(List<String> lists, String separator) {
        if (isEmpty(lists)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String list : lists) {
            sb.append(list + separator);
        }
        return StringHelp.substring(sb.toString());
    }

    public static String intListToString(List<Integer> lists) {
        return intListToString(lists, ",");
    }

    public static String intListToString(List<Integer> lists, String separator) {
        if (isEmpty(lists)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Integer list : lists) {
            sb.append(list + separator);
        }
        return StringHelp.substring(sb.toString());
    }

    /**
     * 将数组字符串转换为集合
     *
     * @param arrayStr
     * @param separator
     * @return
     */
    public static List<Integer> arrayStrToIntegerList(String arrayStr, String separator) {
        if (StringUtils.isBlank(arrayStr)) {
            return null;
        }
        String[] arrays = arrayStr.split(separator);
        return arrayToIntegerList(arrays);
    }

    /**
     * 将数组字符串转换为集合(以逗号分隔)
     *
     * @param arrayStr
     * @return
     */
    public static List<Integer> arrayStrToIntegerList(String arrayStr) {
        if (StringUtils.isBlank(arrayStr)) {
            return null;
        }
        String[] arrays = arrayStr.split(",");
        List<String> tmp = new ArrayList<String>();
        for (String str : arrays) {
            if (StringUtils.isNotBlank(str)) {
                tmp.add(str);
            }
        }
        tmp.toArray();
        return arrayToIntegerList(tmp);
    }

    public static String[] arrayStrToArray(String arrayStr, String separator) {
        if (StringUtils.isBlank(arrayStr)) {
            return null;
        }
        if (arrayStr.indexOf(separator) == -1) {
            return new String[]{arrayStr};
        }
        return arrayStr.split(separator);
    }

    /**
     * 将数组字符串转换为集合
     *
     * @param arrayStr
     * @param separator
     * @return
     */
    public static List<String> arrayStrToStringList(String arrayStr, String separator) {
        if (StringUtils.isBlank(arrayStr)) {
            return null;
        }
        String[] arrays = arrayStr.split(separator);
        return arrayToList(arrays);
    }

    /**
     * string数组转Integer集合
     *
     * @param arrays
     * @return
     */
    public static List<Integer> arrayToIntegerList(String[] arrays) {
        List<String> list = arrayToList(arrays);
        return arrayToIntegerList(list);
    }

    /**
     * string集合转Integer集合
     *
     * @param arrays
     * @return
     */
    public static List<Integer> arrayToIntegerList(List<String> arrays) {
        if (CollectionUtils.isEmpty(arrays)) {
            return null;
        }
        List<Integer> outputCollection = new ArrayList<>(arrays.size());
        CollectionUtils.collect(arrays,
                new Transformer() {
                    public Object transform(Object input) {
                        return new Integer((String) input);
                    }
                }, outputCollection);
        return outputCollection;
    }

    /**
     * 数组转集合
     *
     * @param arrays
     * @return
     */
    public static List<String> arrayToList(String[] arrays) {
        if (arrays == null || arrays.length == 0) {
            return null;
        }
        return new ArrayList<>(Arrays.asList(arrays));
    }

    /**
     * sting集合转String数组
     *
     * @param list
     * @return
     */
    public static String[] listToArray(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        String[] array = new String[list.size()];
        return list.toArray(array);
    }

    /**
     * sting集合转String数组
     *
     * @param list
     * @return
     */
    public static String[] setToArray(Set<String> set) {
        if (CollectionUtils.isEmpty(set)) {
            return null;
        }
        String[] array = new String[set.size()];
        return set.toArray(array);
    }

    /**
     * sting集合转integer数组
     *
     * @param list
     * @return
     */
    public static Integer[] listToIntegerArray(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<Integer> integerList = arrayToIntegerList(list);
        Integer[] array = new Integer[integerList.size()];
        return integerList.toArray(array);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return ((collection == null) || (collection.size() == 0));
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return ((collection != null) && (collection.size() > 0));
    }

    /**
     * 数组是否包含值
     *
     * @param values
     * @param value
     * @return
     */
    public static boolean isContain(Integer[] values, Integer value) {
        if (values == null || values.length == 0) {
            return false;
        }
        boolean isContain = false;
        for (Integer type : values) {
            if (type.equals(value)) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    /**
     * 数组是否包含值
     *
     * @param values
     * @param value
     * @return
     */
    public static boolean isContain(int[] values, int value) {
        if (values == null || values.length == 0) {
            return false;
        }
        boolean isContain = false;
        for (Integer type : values) {
            if (type.equals(value)) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    /**
     * 集合是否是Integer集合
     *
     * @param collection
     * @return
     */
    public static boolean isIntegerCollection(Collection<?> collection) {
        if (isEmpty(collection)) {
            return false;
        }
        boolean isInteger = true;
        for (Iterator<?> iter = collection.iterator(); iter.hasNext(); ) {
            Object value = iter.next();
            if (!(value instanceof Integer)) {
                isInteger = false;
            }
        }
        return isInteger;
    }

    /**
     * 集合是否是Integer集合
     *
     * @param collection
     * @return
     */
    public static boolean isStringCollection(Collection<?> collection) {
        if (isEmpty(collection)) {
            return false;
        }
        boolean isString = true;
        for (Iterator<?> iter = collection.iterator(); iter.hasNext(); ) {
            Object value = iter.next();
            if (!(value instanceof String)) {
                isString = false;
            }
        }
        return isString;
    }

    public static List<String> toListStr(Collection<?> collection) {
        List<String> strs = new ArrayList<>();
        if (isEmpty(collection)) {
            return strs;
        }
        for (Iterator<?> iter = collection.iterator(); iter.hasNext(); ) {
            Object value = iter.next();
            if (value != null) {
                strs.add(value.toString());
            }
        }
        return strs;
    }

    public static List<Integer> toListInt(Collection<?> collection) {
        List<Integer> strs = new ArrayList<>();
        if (isEmpty(collection)) {
            return strs;
        }
        for (Iterator<?> iter = collection.iterator(); iter.hasNext(); ) {
            Object value = iter.next();
            if (value != null) {
                strs.add(Integer.parseInt(value.toString()));
            }
        }
        return strs;
    }
}
