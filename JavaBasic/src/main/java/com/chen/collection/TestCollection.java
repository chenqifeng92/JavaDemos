package com.chen.collection;

import com.chen.entity.Cell;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class TestCollection {
    /**
     * 测试add方法
     */
    @Test
    public void testAdd(){
        Collection<String> c = new ArrayList<String>();
        System.out.println(c);//[]
        c.add("a");
        c.add("b");
        c.add("c");
        System.out.println(c);//[a,b,c]
    }
    /**
     * 测试contains方法
     */
    @Test
    public void testContains(){
        Collection<Cell> cells = new ArrayList<Cell>();

        cells.add(new Cell(1,2));
        cells.add(new Cell(1,3));
        cells.add(new Cell(2,2));
        cells.add(new Cell(2,3));

        Cell cell = new Cell(1,3);

        //List集合contains方法和对象的equals方法相关
        boolean flag = cells.contains(cell);
        //如果Cell不重写equals方法将为false
        System.out.println(flag);//true
    }
    /**
     * 测试size方法、clear方法、isEmpty方法
     */
    @Test
    public void testSizeAndClearAndIsEmpty(){
        Collection<String> c = new HashSet<String>();
        System.out.println(c.isEmpty());//true

        c.add("java");
        c.add("cpp");
        c.add("php");
        c.add("c#");
        c.add("objective-c");

        System.out.println("isEmpty:"+c.isEmpty()+",size:"+c.size());
        c.clear();
        System.out.println("isEmpty:"+c.isEmpty()+",size:"+c.size());
    }
    /**
     * 测试addAll和containAll方法
     */
    @Test
    public void testAddAllAndContainAll(){
        Collection<String> c1 = new ArrayList<String>();
        c1.add("java");
        c1.add("cpp");
        c1.add("php");
        c1.add("c#");
        c1.add("objective-c");

        System.out.println(c1);

        Collection<String> c2 = new HashSet<String>();

        c2.addAll(c1);
        System.out.println(c2);

        Collection<String> c3 = new ArrayList<String>();
        c3.add("java");
        c3.add("cpp");
        System.out.println(c1.containsAll(c3));
    }
}
