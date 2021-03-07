package com.chen.util;

import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
public class ArrayDiff {

    /**
     * 并集，Set唯一性
     * @param arr1
     * @param arr2
     * @return
     */
    public static String[] union (String[] arr1, String[] arr2){
        Set<String> hs = new HashSet<String>();
        for(String str:arr1){
            hs.add(str);
        }
        for(String str:arr2){
            hs.add(str);
        }
        String[] result={};
        return hs.toArray(result);
    }

    /**
     * 交集
     * 注意结果集中若使用LinkedList添加，则需要判断是否包含该元素，否则其中会包含重复的元素
     * @param arr1
     * @param arr2
     * @return
     */
    public static String[] intersect(String[] arr1, String[] arr2){
        List<String> l = new LinkedList<String>();
        Set<String> common = new HashSet<String>();
        for(String str:arr1){
            if(!l.contains(str)){
                l.add(str);
            }
        }
        for(String str:arr2){
            if(l.contains(str)){
                common.add(str);
            }
        }
        String[] result={};
        return common.toArray(result);
    }

    /**
     * 求两个数组的差集
     * @param arr1
     * @param arr2
     * @return
     */
    public static String[] substract(String[] arr1, String[] arr2) {
        LinkedList<String> list = new LinkedList<String>();
        for (String str : arr1) {
            if(!list.contains(str)) {
                list.add(str);
            }
        }
        for (String str : arr2) {
            if (list.contains(str)) {
                list.remove(str);
            }
        }
        String[] result = {};
        return list.toArray(result);
    }

    //单元测试模块
    @Test
    public void testUnion(){
        //测试union 并集
        String[] arr1 = {"a","b","c"};
        String[] arr2 = {"c","d","e"};
        String[] result_union = union(arr1, arr2);
        System.out.println("求并集的结果如下：");
        for (String str : result_union) {
            System.out.print(str+" ");
        }
    }

    @Test
    public void testInsect(){
        //测试insect 交集
        String[] arr1 = {"a","b","c"};
        String[] arr2 = {"c","d","e"};
        String[] result_insect = intersect(arr1, arr2);
        System.out.println("求交集的结果如下：");
        for (String str : result_insect) {
            System.out.print(str+" ");
        }
    }

    @Test
    public void testSubstract(){
        //测试substract 差集
        String[] arr1 = {"a","b","c"};
        String[] arr2 = {"c","d","e"};
        String[] result_minus = substract(arr1, arr2);
        System.out.println("求差集的结果如下：");
        for (String str : result_minus) {
            System.out.print(str+" ");
        }
    }
}

