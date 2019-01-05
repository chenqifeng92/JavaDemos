package com.chen.file;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileName {
    /**
     * 获取一个文件夹内所有文件夹名以及文件名（不包含子文件夹）
     * @param path
     */
    public void getFileDirectoryName(String path) {
        File file = new File(path);
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                String fileName = fileList[i].getName();
                System.out.println("文件：" + fileName);
            }
        }
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                String directoryName = fileList[i].getName();
                System.out.println("目录：" + directoryName);
            }
        }
    }

    /**
     * 将一个文件夹内所有文件转换为数字并存进一个集合
     * @param path
     * @return
     */
    public List<Long> getInvoicesList(String path) {
        File file = new File(path);
        File[] fileList = file.listFiles();
        List<Long> invoicesList = new LinkedList<Long>();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                String fileName = fileList[i].getName();
                if (fileName.matches(".*_.*")) {
                    invoicesList.add(Long.parseLong(fileName.substring(0, fileName.indexOf("_")).trim()));
                } else {
                    invoicesList.add(Long.parseLong(fileName.substring(0, fileName.indexOf(".")).trim()));
                }
            }
        }
        return invoicesList;
    }

    public static void main(String[] args) {
        FileName fn = new FileName();
        String path = "C:\\Users\\Chen\\Downloads\\综合报销发票";
        fn.getFileDirectoryName(path);
        //fn.getInvoicesList(path);
    }
}
