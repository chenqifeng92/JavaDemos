package com.chen.date;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestCalendar {
    /**
     * 测试getInstance方法
     */
    @Test
    public void testGetInstance(){
        Calendar c = Calendar.getInstance();

        //输出Calendar对象所属的实际类型
        System.out.println(c.getClass().getName());
        //getTime方法返回对应的Date对象
        System.out.println(c.getTime());
        //创建GregorianCalendar对象
        GregorianCalendar c1 = new GregorianCalendar(2122,Calendar.DECEMBER,25);

        System.out.println(c1.getTime());
    }

    /**
     * 测试set方法
     */
    @Test
    public void testSet(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2346);
        c.set(Calendar.MONTH,Calendar.DECEMBER);
        c.set(Calendar.DATE, 22);
        System.out.println(c.getTime());

        //设置超出日历的日期，会自动帮您计算实际日期
        c.set(Calendar.DATE,32);
        System.out.println(c.getTime());
    }

    /**
     * 测试get方法
     */
    @Test
    public void testGet(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2346);
        c.set(Calendar.MONTH,Calendar.DECEMBER);
        c.set(Calendar.DATE, 22);

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(dayOfWeek);//输出为1，表示周二，周日为每周的第一天
    }

    /**
     * 输出某一年各个月份的天数
     */
    @Test
    public void testActualMaximum(){
        int year = 2423;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DATE, 1);

        for(int month = Calendar.JANUARY;month<=Calendar.DECEMBER;month++){
            c.set(Calendar.MONTH,month);
            System.out.println(year+"年"+(month+1)+"月："+c.getActualMaximum(Calendar.DATE)+"天");
        }
    }
    /**
     * 输出一年后再减去三个月的日期
     * add(field,amount)
     */
    @Test
    public void testAdd(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);//加一年
        calendar.add(Calendar.MONTH, -3);//减三个月
        /*System.out.println("year: "+calendar.get(Calendar.YEAR));
        System.out.println("month: "+(calendar.get(Calendar.MONTH))+1);
        System.out.println("day: "+calendar.get(Calendar.DAY_OF_MONTH));*/

        Date d = new Date();
        d = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String calStr = sdf.format(d);
        System.out.println(calStr);
    }
    /**
     * 使Date表示的日期与Calendar表示的日期进行交换
     */
    @Test
    public void testSetTimeAndGetTime(){
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);//将Date转换为Calendar
        date = calendar.getTime();//将Calendar转换为Date
    }
}
