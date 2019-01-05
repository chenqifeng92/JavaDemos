package com.chen.algorithm;

import com.chen.file.FileName;

import java.util.List;

public class EssentialDemos {
    /**
     * 给定一个含有数字的集合，取出其中一些数字，使得其和等于1000
     */
    public void MonthlyInvoice(){
        FileName fileName = new FileName();
        List<Long> invoicesList = fileName.getInvoicesList("C:\\Users\\Chen\\Downloads\\综合报销发票");
        for (Long fn : invoicesList) {
            System.out.println(fn);
        }
    }
    public static void main(String[] args) {
        EssentialDemos essentialDemos = new EssentialDemos();
        essentialDemos.MonthlyInvoice();
    }
}
