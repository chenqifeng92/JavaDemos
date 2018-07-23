package com.chen.map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateMaps {
    /**
     * 生成一个HashMap
     * 键不可重复，无序，线程安全
     * @return
     */
    public Map<String,String> generateHashMap(){
        String param = "(Integer)8640000, (String)8640100, (Long)8640000, (Boolean)true";
        List<String> vtList = Arrays.asList(param.split(","));
        Map<String,String> hashMap = new HashMap<String, String>();
        for(String vt : vtList){
            String key = vt.substring(vt.indexOf("(")+1,vt.indexOf(")")).trim();
            String value = vt.replaceAll("\\([^0]*\\)","").trim();
            hashMap.put(key,value);
        }
        return hashMap;
    }
}
