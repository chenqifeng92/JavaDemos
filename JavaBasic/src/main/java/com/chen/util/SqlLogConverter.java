package com.chen.util;

import java.util.Arrays;
import java.util.List;

/**
 * 将控制台打印的不带参SQL和Parameters参数拼接成SQL
 *
 * 刚开始我想用Map存放键值对，但是能保证有序的LinkedHashMap和TreeMap的key无法重复，
 * 而key允许重复的IdentityHashMap又是无序的
 */
public class SqlLogConverter {

    private String getParamAndSql(String mybatisLog) {
        return null;
    }

    private String generateSqlLog(String param,String sql){
        List<String> vtList = Arrays.asList(param.split(","));
        for(String vt : vtList){
            String type = vt.substring(vt.indexOf("(")+1,vt.indexOf(")")).trim();
            String value = vt.replaceAll("\\([^0]*\\)","").trim();
            if (type.equals("String")){
                sql = sql.replaceFirst("\\?","\'"+value+"\'");
            }else if (type.equals("Integer") || type.equals("Long")){
                sql = sql.replaceFirst("\\?",value);
            }
        }
        return sql;
    }

    public static void main(String[] args) {
        String param = "";
        String sql = "";
        SqlLogConverter sqlLogConverter = new SqlLogConverter();
        String resultSql = sqlLogConverter.generateSqlLog(param,sql);
        System.out.println(resultSql);
    }
}
