package com.chen.thread;

public class ImplementsRunnable implements Runnable{ // 实现Runnable接口，作为线程的实现类
    private String name ;       // 表示线程的名称
    public ImplementsRunnable(String name){
        this.name = name ;      // 通过构造方法配置name属性
    }
    public void run(){  // 覆写run()方法，作为线程 的操作主体
        for(int i=0;i<10;i++){
            System.out.println(name + "运行，i = " + i) ;
        }
    }

    public static void main(String[] args) {
        ImplementsRunnable ir1 = new ImplementsRunnable("实现Runnable接口的线程A") ;    // 实例化对象
        ImplementsRunnable ir2 = new ImplementsRunnable("实现Runnable接口的线程B") ;    // 实例化对象
        Thread t1 = new Thread(ir1) ;       // 实例化Thread类对象
        Thread t2 = new Thread(ir2) ;       // 实例化Thread类对象
        t1.start() ;   // 调用线程主体
        t2.start() ;   // 调用线程主体
    }
}
