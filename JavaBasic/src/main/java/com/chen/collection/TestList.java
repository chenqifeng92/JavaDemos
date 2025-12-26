package com.chen.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TestList {
    /**
     * 测试set方法和get方法
     */
    @Test
    public void testGetAndSet(){
        List<String> list = new ArrayList<String>();
        list.add("java");
        list.add("cpp");
        list.add("php");
        list.add("c#");
        list.add("objective-c");

        //get方法遍历List
        for(int i=0;i<list.size();i++){
            System.out.print(list.get(i).toUpperCase()+" ");
        }

        String value = list.set(1,"c++");//list从0开始计数
        //将索引为1处的变量替换为c++，并且返回原来索引为1处的值cpp赋给value
        System.out.println(value);
        System.out.println(list);

        //交换位置1和3处的元素
        list.set(1, list.set(3, list.get(1)));
        System.out.println(list);
    }
    /**
     * 测试插入和移除元素
     */
    @Test
    public void testInsertAndRemove(){
        List<String> list = new ArrayList<String>();
        list.add("java");
        list.add("c#");
        System.out.println(list);

        list.add(1,"cpp");
        System.out.println(list);//[java,cpp,c#]

        list.remove(2);
        System.out.println(list);//[java,cpp]
    }
    /**
     * 测试subList方法
     */
    @Test
    public void testSubList(){
        List<Integer> list = new ArrayList<Integer>();
        for(int i=0;i<10;i++){
            list.add(i);
        }
        System.out.println(list);//[0,1,2,3,4,5,6,7,8,9]

        List<Integer> subList = list.subList(3, 8);

        //subList获得的List和源List，它们在内存中地址是相同的
        for(int i=0;i<subList.size();i++){
            subList.set(i, subList.get(i)*10);
        }
        System.out.println(subList.size());//5
        System.out.println(subList);//[30,40,50,60,70]
        System.out.println(list);//[0,1,2,30,40,50,60,70,8,9]

        //可以用于删除连续元素
        list.subList(3, 8).clear();
        System.out.println(list);//[0,1,2,8,9]
    }
    /**
     * 测试toArray方法
     */
    @Test
    public void testListToArray(){
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");

        String[] strArr = list.toArray(new String[] {});
        System.out.println(Arrays.toString(strArr));//[a,b,c]
    }
    /**
     * 测试asList方法
     */
    @Test
    public void testArrayToList(){
        String[] strArr = {"a","b","c"};

        List<String> list = Arrays.asList(strArr);
        System.out.println(list);//[a,b,c]
        //list.add("d");// will throw UnsupportedOperationException

        System.out.println(list.getClass().getName());//java.util.Arrays$ArrayList

        List<String> list1 = new ArrayList<String>();
        list1.addAll(Arrays.asList(strArr));
    }
}
