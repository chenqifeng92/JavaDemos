package com.chen.map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class TestHashMap {
    /**
     * 统计各空气质量监测点PM2.5的最高值
     */
    @Test
    public void test1()
    {
        String pm25 = "农展馆=423,东四=378,丰台花园=406,天坛=322,海淀区万柳=398,"
                + "官园=406,通州=366,昌平镇=248,怀柔镇=306,定陵=231,前门=422,"
                + "永乐店=368,古城=268,昌平镇=423,怀柔镇=267,定陵=377,前门=299,"
                + "永乐店=285,秀水街=277,农展馆=348,东四=356,丰台花园=179,天坛=277,"
                + "海淀区万柳=270,官园=268,通州=315";
        Map<String,Integer> map = new HashMap<String,Integer>();
        String[] arr = pm25.split("[,=]");
        for(int i=0;i<arr.length;i+=2)
        {
            if(!map.containsKey(arr[i]) || Integer.parseInt(arr[i+1])>map.get(arr[i]))
            {
                map.put(arr[i],Integer.parseInt(arr[i+1]));
            }
        }
        System.out.println(map);
    }

    /**
     * 使用迭代key的方式遍历map集合
     */
    @Test
    public void test2()
    {
        String pm25 = "农展馆=423,东四=378,丰台花园=406,天坛=322,海淀区万柳=398,"
                + "官园=406,通州=366,昌平镇=248,怀柔镇=306,定陵=231,前门=422,"
                + "永乐店=368,古城=268,昌平镇=423,怀柔镇=267,定陵=377,前门=299,"
                + "永乐店=285,秀水街=277,农展馆=348,东四=356,丰台花园=179,天坛=277,"
                + "海淀区万柳=270,官园=268,通州=315";
        Map<String,Integer> map = new HashMap<String,Integer>();
        String[] arr = pm25.split("[,=]");
        for(int i=0;i<arr.length;i+=2)
        {
            if(!map.containsKey(arr[i]) || Integer.parseInt(arr[i+1])>map.get(arr[i]))
            {
                map.put(arr[i],Integer.parseInt(arr[i+1]));
            }
        }

        Set<String> keys = map.keySet();
        for(String key : keys)
        {
            System.out.println(key+":"+map.get(key));
        }
    }

    /**
     * 使用迭代Entry的方式遍历map集合
     */
    @Test
    public void test3()
    {
        String pm25 = "农展馆=423,东四=378,丰台花园=406,天坛=322,海淀区万柳=398,"
                + "官园=406,通州=366,昌平镇=248,怀柔镇=306,定陵=231,前门=422,"
                + "永乐店=368,古城=268,昌平镇=423,怀柔镇=267,定陵=377,前门=299,"
                + "永乐店=285,秀水街=277,农展馆=348,东四=356,丰台花园=179,天坛=277,"
                + "海淀区万柳=270,官园=268,通州=315";
        Map<String,Integer> map = new HashMap<String,Integer>();
        String[] arr = pm25.split("[,=]");
        for(int i=0;i<arr.length;i+=2)
        {
            if(!map.containsKey(arr[i]) || Integer.parseInt(arr[i+1])>map.get(arr[i]))
            {
                map.put(arr[i],Integer.parseInt(arr[i+1]));
            }
        }

        Set<Map.Entry<String, Integer>> entrys = map.entrySet();
        for(Map.Entry<String, Integer> entry : entrys)
        {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }

    /**
     * 按照key放入的顺序遍历集合
     */
    @Test
    public void test4()
    {
        String pm25 = "农展馆=423,东四=378,丰台花园=406,天坛=322,海淀区万柳=398,"
                + "官园=406,通州=366,昌平镇=248,怀柔镇=306,定陵=231,前门=422,"
                + "永乐店=368,古城=268,昌平镇=423,怀柔镇=267,定陵=377,前门=299,"
                + "永乐店=285,秀水街=277,农展馆=348,东四=356,丰台花园=179,天坛=277,"
                + "海淀区万柳=270,官园=268,通州=315";
        Map<String,Integer> map = new LinkedHashMap<String,Integer>();
        String[] arr = pm25.split("[,=]");
        for(int i=0;i<arr.length;i+=2)
        {
            if(!map.containsKey(arr[i]) || Integer.parseInt(arr[i+1])>map.get(arr[i]))
            {
                map.put(arr[i],Integer.parseInt(arr[i+1]));
            }
        }
        Set<Map.Entry<String, Integer>> entrys = map.entrySet();
        for(Map.Entry<String, Integer> entry : entrys)
        {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }

    /**
     * 按照key放入的顺序遍历集合
     */
    @Test
    public void test5()
    {
        String pm25 = "农展馆=423,东四=378,丰台花园=406,天坛=322,海淀区万柳=398,"
                + "官园=406,通州=366,昌平镇=248,怀柔镇=306,定陵=231,前门=422,"
                + "永乐店=368,古城=268,昌平镇=423,怀柔镇=267,定陵=377,前门=299,"
                + "永乐店=285,秀水街=277,农展馆=348,东四=356,丰台花园=179,天坛=277,"
                + "海淀区万柳=270,官园=268,通州=315";
        Map<String,Integer> map = new LinkedHashMap<String,Integer>();
        String[] arr = pm25.split("[,=]");
        for(int i=0;i<arr.length;i+=2)
        {
            if(!map.containsKey(arr[i]) || Integer.parseInt(arr[i+1])>map.get(arr[i]))
            {
                map.put(arr[i],Integer.parseInt(arr[i+1]));
            }
        }

        Set<Map.Entry<String, Integer>> entrys = map.entrySet();
        for(Map.Entry<String, Integer> entry : entrys)
        {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
}

