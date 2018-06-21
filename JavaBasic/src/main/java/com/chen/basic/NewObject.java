package com.chen.basic;

public class NewObject {
    public String strMyString;
    public int intMyNumber;
    public NewObject(String strMyString,int intMyNumber){
        this.strMyString = strMyString;
        this.intMyNumber = intMyNumber;
    }
    public static void main(String[] args){
        String str1 = "hode";
        String str2 = "hode";
        String str3 = new String("hode");
        String str4 = new String("hode");//true
        NewObject no1 = new NewObject("hode",1);
        NewObject no2 = new NewObject("hode",1);

        System.out.println("str1==str2 :"+(str1==str2));//true
        System.out.println("str1.equals(str2) : "+(str1.equals(str2)));//true
        System.out.println("str3==str4 :"+(str3==str4));//false
        System.out.println("str3.equals(str4) :"+(str3.equals(str4)));//true
        System.out.println("hello1==hello2 :"+(no1==no2));//false
        System.out.println("hello1.equals(hello2) :"+(no1.equals(no2)));//false
    }
}
