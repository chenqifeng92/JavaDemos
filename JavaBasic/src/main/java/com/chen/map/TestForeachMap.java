package com.chen.map;

import java.util.Iterator;
import java.util.Map;

public class TestForeachMap {

    /**
     * 使用Entry遍历Map
     * 这是最常见的并且在大多数情况下也是最可取的遍历方式。在键值都需要时使用。
     * 该方法比entrySet遍历在性能上稍好（快了10%），而且代码更加干净。
     * @param map
     */
    public static void foreachMapByEntry(Map<String,String> map){
        for(Map.Entry<String,String> entry : map.entrySet()){
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    /**
     * 分别打印Map的key和value
     * @param map
     */
    public static void foreachMapByForeach(Map<String,String> map){
        for(String key:map.keySet()){
            System.out.println("Key = " + key);
        }
        for(String value:map.values()){
            System.out.println("Value = " + value);
        }
    }

    /**
     * 使用迭代器遍历打印map（使用泛型）
     * @param map
     */
    public static void foreachMapByIteratorWithGenericity(Map<String,String> map){
        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    /**
     * 使用迭代器遍历打印map（不使用泛型）
     *
     * 该种方式看起来冗余却有其优点所在。首先，在老版本java中这是惟一遍历map的方式。
     * 另一个好处是，你可以在遍历时调用iterator.remove()来删除entries，另两个方法则不能。
     * 根据javadoc的说明，如果在for-each遍历中尝试使用此方法，结果是不可预测的。
     * 从性能方面看，该方法类同于for-each遍历的性能。
     *
     * @param map
     */
    public static void foreachMapByIteratorWithoutGenericity(Map<String,String> map){
        Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            System.out.println("Key = " + key + ", Value = " + value);
        }
    }

    /**
     * 通过键找值遍历Map（效率低）
     * @param map
     */
    public static void foreachMapByKeyToValue(Map<String,String> map){
        for (String key : map.keySet()) {
            String value = map.get(key);
            System.out.println("Key = " + key + ", Value = " + value);
        }
    }

    public static void main(String[] args){
        GenerateMaps generateMaps = new GenerateMaps();
        Map<String,String> map = generateMaps.generateHashMap();

        foreachMapByEntry(map);
        //foreachMapByForeach(map);

        //foreachMapByIteratorWithGenericity(map);
        //foreachMapByIteratorWithoutGenericity(map);

        //foreachMapByKeyToValue(map);
    }

}

