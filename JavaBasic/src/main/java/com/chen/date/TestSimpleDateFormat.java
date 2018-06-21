package com.chen.date;

import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestSimpleDateFormat {
    /**
     * 测试format方法
     */
    @Test
    public void testFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateStr = sdf.format(date);
        System.out.println(dateStr);
    }
    /**
     * 测试parse方法
     */
    @Test
    public void testParse() throws Exception{
        String str = "2236-1-22";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(str);
        System.out.println(date);
    }
}
