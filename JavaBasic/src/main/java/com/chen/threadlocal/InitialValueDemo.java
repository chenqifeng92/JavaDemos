package com.chen.threadlocal;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadLocal初始值设置演示
 * 展示如何通过不同方式为ThreadLocal设置初始值
 */
public class InitialValueDemo {

    // 方式1：通过继承ThreadLocal并重写initialValue()方法
    private static ThreadLocal<String> threadLocalWithInitial = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "默认初始值-" + Thread.currentThread().getName();
        }
    };
    
    // 方式2：使用Java 8的withInitial()方法（推荐）
    private static ThreadLocal<Integer> threadLocalWithLambda = 
        ThreadLocal.withInitial(() -> 0);
    
    // 方式3：创建一个线程安全的ID生成器
    private static final AtomicInteger nextId = new AtomicInteger(1000);
    private static ThreadLocal<Integer> threadId = 
        ThreadLocal.withInitial(() -> nextId.getAndIncrement());
    
    // 方式4：复杂对象的初始化
    private static ThreadLocal<User> userThreadLocal = 
        ThreadLocal.withInitial(() -> new User("匿名用户", 0));

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ThreadLocal初始值设置演示 ===\n");
        
        // 演示1：重写initialValue方法
        overrideInitialValueDemo();
        
        // 演示2：使用withInitial方法
        withInitialDemo();
        
        // 演示3：线程ID生成器
        threadIdGeneratorDemo();
        
        // 演示4：复杂对象初始化
        complexObjectInitDemo();
    }
    
    /**
     * 演示通过重写initialValue方法设置初始值
     */
    private static void overrideInitialValueDemo() {
        System.out.println("1. 重写initialValue方法演示：");
        
        // 不设置值，直接获取，会使用初始值
        System.out.println("主线程获取初始值: " + threadLocalWithInitial.get());
        
        // 创建新线程测试
        Thread thread = new Thread(() -> {
            // 每个线程都会有自己的初始值
            System.out.println(Thread.currentThread().getName() + " 获取初始值: " + 
                             threadLocalWithInitial.get());
            
            // 修改值
            threadLocalWithInitial.set("修改后的值");
            System.out.println(Thread.currentThread().getName() + " 修改后: " + 
                             threadLocalWithInitial.get());
            
            // 清理
            threadLocalWithInitial.remove();
            // remove后再次get会重新调用initialValue
            System.out.println(Thread.currentThread().getName() + " remove后再次获取: " + 
                             threadLocalWithInitial.get());
        }, "工作线程1");
        
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    /**
     * 演示使用withInitial方法设置初始值
     */
    private static void withInitialDemo() {
        System.out.println("2. withInitial方法演示：");
        
        Thread thread1 = new Thread(() -> {
            // 初始值为0
            System.out.println(Thread.currentThread().getName() + " 初始值: " + 
                             threadLocalWithLambda.get());
            
            // 累加操作
            for (int i = 0; i < 5; i++) {
                threadLocalWithLambda.set(threadLocalWithLambda.get() + 1);
            }
            
            System.out.println(Thread.currentThread().getName() + " 累加后: " + 
                             threadLocalWithLambda.get());
        }, "计算线程1");
        
        Thread thread2 = new Thread(() -> {
            // 每个线程都有独立的初始值0
            System.out.println(Thread.currentThread().getName() + " 初始值: " + 
                             threadLocalWithLambda.get());
            
            // 累加操作
            for (int i = 0; i < 10; i++) {
                threadLocalWithLambda.set(threadLocalWithLambda.get() + 1);
            }
            
            System.out.println(Thread.currentThread().getName() + " 累加后: " + 
                             threadLocalWithLambda.get());
        }, "计算线程2");
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    /**
     * 演示线程ID生成器
     */
    private static void threadIdGeneratorDemo() throws InterruptedException {
        System.out.println("3. 线程ID生成器演示：");
        
        // 创建多个线程，每个线程都会获得唯一的ID
        Thread[] threads = new Thread[5];
        
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                // 第一次调用get()时会分配唯一ID
                Integer id = threadId.get();
                System.out.println(Thread.currentThread().getName() + " 分配的ID: " + id);
                
                // 多次调用get()返回相同的ID
                for (int j = 0; j < 3; j++) {
                    System.out.println(Thread.currentThread().getName() + 
                                     " 第" + (j + 1) + "次获取ID: " + threadId.get());
                }
            }, "线程-" + i);
            
            threads[i].start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println();
    }
    
    /**
     * 演示复杂对象的初始化
     */
    private static void complexObjectInitDemo() {
        System.out.println("4. 复杂对象初始化演示：");
        
        Thread thread1 = new Thread(() -> {
            // 获取初始用户对象
            User user = userThreadLocal.get();
            System.out.println(Thread.currentThread().getName() + " 初始用户: " + user);
            
            // 修改用户信息
            user.setName("张三");
            user.setAge(25);
            System.out.println(Thread.currentThread().getName() + " 修改后: " + user);
            
            // 清理并重新获取
            userThreadLocal.remove();
            User newUser = userThreadLocal.get();
            System.out.println(Thread.currentThread().getName() + " remove后新用户: " + newUser);
        }, "用户线程1");
        
        Thread thread2 = new Thread(() -> {
            // 获取初始用户对象（与线程1独立）
            User user = userThreadLocal.get();
            System.out.println(Thread.currentThread().getName() + " 初始用户: " + user);
            
            // 修改用户信息
            user.setName("李四");
            user.setAge(30);
            System.out.println(Thread.currentThread().getName() + " 修改后: " + user);
        }, "用户线程2");
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 用户类，用于演示复杂对象的ThreadLocal
     */
    static class User {
        private String name;
        private int age;
        
        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int getAge() {
            return age;
        }
        
        public void setAge(int age) {
            this.age = age;
        }
        
        @Override
        public String toString() {
            return "User{name='" + name + "', age=" + age + "}";
        }
    }
}