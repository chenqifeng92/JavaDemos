package com.chen.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class TestIterator {
    /**
     * 测试Iterator中的hasNext方法和next方法
     */
    @Test
    public void testHasNextAndNext(){
        Collection<String> c = new HashSet<String>();
        c.add("java");
        c.add("cpp");
        c.add("php");
        c.add("c#");
        c.add("objective-c");

        //hashSet是有序的，遍历的时候自然按照排序好之后的顺序遍历
        Iterator<String> it = c.iterator();
        while(it.hasNext()){
            String str = it.next();
            System.out.println(str);
        }
    }
    /**
     * 测试Iterator中的remove方法
     */
    @Test
    public void testRemove(){
        Collection<String> c = new ArrayList<String>();
        c.add("java");
        c.add("cpp");
        c.add("php");
        c.add("c#");
        c.add("objective-c");
        //ArrayList不进行排序
        System.out.println(c);

        Iterator<String> it = c.iterator();
        while(it.hasNext()){
            String str = it.next();
            if(str.indexOf('c')!=-1){
                it.remove();
            }
        }
        System.out.println(c);
    }
    /**
     * 测试foreach循环
     */
    @Test
    public void testForeach(){
        Collection<String> c = new HashSet<String>();
        c.add("java");
        c.add("cpp");
        c.add("php");
        c.add("c#");
        c.add("objective-c");
        for(String str : c){
            System.out.println(str);
            System.out.println(str.toUpperCase()+" ");
        }
    }
}
