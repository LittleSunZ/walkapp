package com.zxc.walk.framework.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author xyu
 * @describe
 * @date 2015年7月29日
 */
public class ArrayUtils {

    /**
     * 多个数组合并为一个数组
     *
     * @return
     */
    public static <T> T[] mergeArray(T[]... objs) {
        int length = 0;
        for (int i = 0; i < objs.length; i++) {
            length += objs[i].length;
        }
        @SuppressWarnings("unchecked")
        T[] mergeArr = (T[]) Array.newInstance(objs.getClass().getComponentType().getComponentType(), length);
        for (int i = 0, dstPos = 0; i < objs.length; i++) {
            dstPos += i - 1 < 0 ? 0 : objs[i - 1].length;
            System.arraycopy(objs[i], 0, mergeArr, dstPos, objs[i].length);
        }
        return mergeArr;
    }

    /**
     * 检测集合是否不存在或者为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(Collection list) {
        return list == null || list.isEmpty();
    }

    public static String join(Collection collection, String regx) {
        if (isEmpty(collection)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj :
                collection) {
            sb.append(obj.toString());
            sb.append(regx);
        }
        return sb.substring(0, sb.length() - regx.length());
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map sortMapByKey(Map map, Comparator comparator) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map sortMap = new TreeMap(
                comparator);

        sortMap.putAll(map);

        return sortMap;
    }

    /**
     * list转int数组
     *
     * @param integers
     * @return
     */
    public static int[] listToIntArray(List<Integer> integers) {
        if (ArrayUtils.isEmpty(integers)) {
            return new int[0];
        }
        int[] result = new int[integers.size()];
        for (int i = 0; i < integers.size(); i++) {
            result[i] = integers.get(i);
        }
        return result;
    }

    /**
     * list转char数组
     *
     * @param characters
     * @return
     */
    public static char[] listToCharArray(List<Character> characters) {
        if (ArrayUtils.isEmpty(characters)) {
            return new char[0];
        }
        char[] result = new char[characters.size()];
        for (int i = 0; i < characters.size(); i++) {
            result[i] = characters.get(i);
        }
        return result;
    }
}