package com.chen.threadlocal;

/**
 * ThreadLocal基本用法演示
 * ThreadLocal为每个线程提供了一个独立的变量副本，每个线程都可以独立地改变自己的副本，
 * 而不会影响其他线程所对应的副本。
 */
public class BasicThreadLocalDemo {

    // 创建一个ThreadLocal变量，用于存储每个线程的专属数据
    private static ThreadLocal<String> threadLocalString = new ThreadLocal<>();
    
    // 创建一个ThreadLocal变量，存储Integer类型
    private static ThreadLocal<Integer> threadLocalInteger = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ThreadLocal基本用法演示 ===\n");
        
        // 演示1：基本的get和set操作
        basicGetSetDemo();
        
        // 演示2：多线程环境下的隔离性
        multiThreadIsolationDemo();
        
        // 演示3：remove方法的使用
        removeMethodDemo();
    }
    
    /**
     * 演示ThreadLocal的基本get和set操作
     */
    private static void basicGetSetDemo() {
        System.out.println("1. 基本get和set操作演示：");
        
        // 在主线程中设置值
        threadLocalString.set("主线程的值");
        threadLocalInteger.set(100);
        
        System.out.println("主线程 - String值: " + threadLocalString.get());
        System.out.println("主线程 - Integer值: " + threadLocalInteger.get());
        
        // 清理ThreadLocal
        threadLocalString.remove();
        threadLocalInteger.remove();
        
        System.out.println("调用remove()后 - String值: " + threadLocalString.get()); // null
        System.out.println("调用remove()后 - Integer值: " + threadLocalInteger.get()); // null
        System.out.println();
    }
    
    /**
     * 演示多线程环境下ThreadLocal的隔离性
     */
    private static void multiThreadIsolationDemo() throws InterruptedException {
        System.out.println("2. 多线程隔离性演示：");
        
        // 创建并启动三个线程
        Thread thread1 = new Thread(new Worker("线程1", "值A", 1000));
        Thread thread2 = new Thread(new Worker("线程2", "值B", 2000));
        Thread thread3 = new Thread(new Worker("线程3", "值C", 3000));
        
        thread1.start();
        thread2.start();
        thread3.start();
        
        // 等待所有线程执行完成
        thread1.join();
        thread2.join();
        thread3.join();
        
        System.out.println();
    }
    
    /**
     * 演示remove方法的重要性
     */
    private static void removeMethodDemo() {
        System.out.println("3. remove方法的重要性：");
        
        Thread thread = new Thread(() -> {
            try {
                // 设置ThreadLocal值
                threadLocalString.set("线程专属数据");
                System.out.println(Thread.currentThread().getName() + " 设置值: " + threadLocalString.get());
                
                // 模拟业务处理
                Thread.sleep(100);
                
                // 重要：使用完毕后要remove，防止内存泄漏
                System.out.println(Thread.currentThread().getName() + " 准备清理ThreadLocal");
                threadLocalString.remove();
                System.out.println(Thread.currentThread().getName() + " 清理后的值: " + threadLocalString.get());
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "演示线程");
        
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 工作线程类，用于演示ThreadLocal的线程隔离性
     */
    static class Worker implements Runnable {
        private String threadName;
        private String value;
        private int sleepTime;
        
        public Worker(String threadName, String value, int sleepTime) {
            this.threadName = threadName;
            this.value = value;
            this.sleepTime = sleepTime;
        }
        
        @Override
        public void run() {
            try {
                // 每个线程设置自己的ThreadLocal值
                threadLocalString.set(value);
                threadLocalInteger.set(sleepTime);
                
                System.out.println(threadName + " 设置值: String=" + value + ", Integer=" + sleepTime);
                
                // 模拟业务处理，休眠一段时间
                Thread.sleep(sleepTime);
                
                // 再次读取值，验证每个线程的值是独立的
                System.out.println(threadName + " 读取值: String=" + threadLocalString.get() + 
                                 ", Integer=" + threadLocalInteger.get());
                
                // 清理ThreadLocal（重要！防止内存泄漏）
                threadLocalString.remove();
                threadLocalInteger.remove();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}