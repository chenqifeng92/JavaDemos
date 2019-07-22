package com.chen.basic;

import org.junit.Test;

public class TestLoop {
    @Test
    public void whileLoop(){
        int x = 10;
        while (x<20){
            System.out.println("vaule of x: "+x+"\n");
            x++;
        }
    }

    @Test
    public void doWhileLoop(){
        int x = 10;
        do{
            System.out.println("value of x: "+x+"\n");
            x++;
        }while(x<20);
    }

    @Test
    public void forLoop(){
        for(int x=10;x<20;x++){
            System.out.println("value of x: "+x+"\n");
        }
    }

    @Test
    public void superForLoop(){
        int[] numbers = { 10, 20, 30, 40, 50 };
        for (int x : numbers) {
            System.out.print(x);
            System.out.print(",");
        }
        System.out.print("\n");
        String[] names = { "James", "Larry", "Tom", "Lacy" };
        for (String name : names) {
            System.out.print(name);
            System.out.print(",");
        }
    }

    @Test
    public void testBreak(){
        int[] numbers = {10,20,30,40,50};
        for(int x : numbers){
            if(x==30){
                break;
            }
            System.out.println(x+"\n");
        }
    }

    @Test
    public void testContinue(){
        int[] numbers = {10,20,30,40,50};
        for(int x : numbers){
            if(x==30){
                continue;
            }
            System.out.println(x+"\n");
        }
    }

}
