package com.chen.util;

import org.junit.Test;

public class PrimeNumber {
    /**
     * 打印一定范围以内质数
     */
    @Test
    public void PrimeNumberPrint(){
        int i,j;
        for(i=1;i<=1000;i++){
            if(i==1 || i==2){
                System.out.print(i+",");
                continue;
            }
            for(j=2;j<i;j++){
                if(i%j==0)
                    break;
                if(j==i-1)
                    System.out.print(i+",");
            }
        }
    }

    /**
     * 打印质数塔
     */
    @Test
    public void PrimeNumberTower(){
        int h = 1;//代表行号
        int num = 0;//代表每行中质数个数
        for(int i=1;i<=1000;i++){
            int count = 0;//当前元素能被整除的个数
            for (int x = 1; x <= i; x++) {
                if (i % x == 0) {
                    count++;
                }
            }
            if (count == 2) {
                System.out.print(i + "\t");
                num++;

                if (h == num) {
                    System.out.println();
                    h++;
                    num = 0;
                }
            }
        }
    }
}
