package com.chen.basic;

public class CountDigit {
    /**
     * 输入一个十位数以内的数字，判断位数
     * @param num
     * @return
     */
    public static int countDightByNum(int num){
        int result = 0;
        if(num==0){
            System.out.println("输入的是1位数");
        }else if(num!=0){
            while(num>0){
                num = num/10;
                result++;
            }
            System.out.println("输入的是"+result+"位数");
        }
        return result;
    }
    public static int countDightByString(int num){
        int result = 0;
        String nnum = num+"";////会产生两个String对象
        //String nnum = String.valueOf(num);//直接使用String类的静态方法，只产生一个对象
        result = nnum.length();
        System.out.println("输入的是"+result+"位数");
        return result;
    }

    /**
     *第一种方法：s=i+"";  //会产生两个String对象
     *第二种方法：s=String.valueOf(i); //直接使用String类的静态方法，只产生一个对象
     *第一种方法：i=Integer.parseInt(s);//直接使用静态方法，不会产生多余的对象，但会抛出异常
     *第二种方法：i=Integer.valueOf(s).intValue();//Integer.valueOf(s) 相当于 new Integer(Integer.parseInt(s))，也会抛异常，但会多产生一个对象
     */

    public static void main(String[] args){
        countDightByNum(99778);
        countDightByString(2233879);
    }
}
