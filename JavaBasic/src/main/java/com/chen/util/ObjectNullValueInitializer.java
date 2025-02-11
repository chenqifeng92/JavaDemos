package com.chen.util;

import java.lang.reflect.Field;
import java.util.Optional;

public class ObjectNullValueInitializer {
    /**
     * 当对象从字符串取不到值时，则赋予其默认值null,确保返回的对象这个key还在，值为null
     * @param obj
     * @param <T>
     */
    public static <T> void fillDefaultValues(T obj) {
        Class<?> clazz = obj.getClass();
        Field[] fileds = clazz.getDeclaredFields();
        for (Field field : fileds) {
            try{
                field.setAccessible(true);
                //使用Optional处理字段值为null的情况
                String value = (String) Optional.ofNullable(field.get(obj)).orElse(null);
                field.set(obj,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将类似于"Sales Marketing Tech"类似的String转化为String[]
     * @param input
     * @return
     */
    public static String[] splitSpaceStrToArr(String input){
        return Optional.ofNullable(input)
                .filter(s->!s.isEmpty() && !s.equals("[]") && !s.equals("null"))
                .map(s->s.replaceAll("^\\[|\\]$",""))
                .map(s->s.split(" "))
                .orElse(new String[]{});
    }
}
