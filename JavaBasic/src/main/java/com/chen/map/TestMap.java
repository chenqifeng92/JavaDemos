package com.chen.map;

import java.util.HashMap;
import java.util.Map;
import com.chen.entity.Emp;
import org.junit.Before;
import org.junit.Test;




public class TestMap {
    private Map<Emp,String> employees = new HashMap<Emp,String>();
    @Before
    public void testPut(){
        //向map中添加元素
        employees.put(new Emp("张三",25,"男",5000.00),"张三");
        employees.put(new Emp("李四",21,"女",6000.00),"李四");
    }
    @Test
    public void testGet(){
        Emp zhangsan = new Emp("张三",25,"男",5000.00);
        String name = employees.get(zhangsan);
        System.out.println(name);
    }
    @Test
    public void testContainsKey(){
        Emp zhangsan = new Emp("张三",25,"男",5000.00);
        boolean has = employees.containsKey(zhangsan);
        System.out.println("是否有员工李四："+has);
    }
}

