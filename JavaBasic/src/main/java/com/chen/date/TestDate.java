package com.chen.date;

import org.junit.Test;
import java.util.Date;

public class TestDate {
    /**
     * 测试getTime方法
     */
    @Test
    public void testGetTime(){
        Date date = new Date();
        System.out.println(date);

        //1970年1月1日零时距此刻的毫秒数
        long time = date.getTime();
        System.out.println(time);
    }

    @Test
    public void testSetTime(){
        Date date = new Date();

        //输出当天此时此刻的日期和时间
        System.out.println(date);

        long time = date.getTime();
        //增加一天所经历的毫秒数
        System.out.println(time);
        time+=60*60*24*1000;
        date.setTime(time);
        //输出明天此时此刻的日期和时间
        System.out.println(date);
    }
}
