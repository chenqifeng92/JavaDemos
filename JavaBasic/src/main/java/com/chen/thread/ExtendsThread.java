package com.chen.thread;

public class ExtendsThread extends Thread{  // 继承Thread类，作为线程的实现类
    private String name ;       // 表示线程的名称
    public ExtendsThread(String name){
        this.name = name ;      // 通过构造方法配置name属性
    }
    public void run(){  // 覆写run()方法，作为线程 的操作主体
        for(int i=0;i<10;i++){
            System.out.println(name + "运行，i = " + i) ;
        }
    }

    public static void main(String[] args) {
        ExtendsThread et1 = new ExtendsThread("继承Thread类的线程1") ;    // 实例化对象
        ExtendsThread et2 = new ExtendsThread("继承Thread类的线程2") ;    // 实例化对象
        et1.start() ;   // 调用线程主体
        et2.start() ;   // 调用线程主体
    }
}
