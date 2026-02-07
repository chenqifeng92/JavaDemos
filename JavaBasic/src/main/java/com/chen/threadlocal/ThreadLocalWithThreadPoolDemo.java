package com.chen.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池中使用ThreadLocal的注意事项演示
 *
 * 核心问题：线程池中的线程会被复用，导致：
 * 1. 前一个任务设置的ThreadLocal值可能被后续任务读到（数据污染）
 * 2. 不调用remove()会导致内存泄漏
 *
 * 本类演示问题场景和正确的解决方案
 */
public class ThreadLocalWithThreadPoolDemo {

    // 模拟用户ID上下文
    private static final ThreadLocal<String> userIdContext = new ThreadLocal<>();

    // 模拟请求计数器（每个线程独立计数）
    private static final ThreadLocal<AtomicInteger> requestCounter =
            ThreadLocal.withInitial(() -> new AtomicInteger(0));

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 线程池中使用ThreadLocal的注意事项 ===\n");

        // 演示1：不清理ThreadLocal导致的数据污染
        demonstrateDataPollution();

        // 演示2：正确的使用方式 - 装饰器模式
        demonstrateDecoratorPattern();

        // 演示3：正确的使用方式 - 显式清理
        demonstrateExplicitCleanup();
    }

    /**
     * 演示线程池中不清理ThreadLocal导致的数据污染
     */
    private static void demonstrateDataPollution() throws InterruptedException {
        System.out.println("1. 数据污染问题演示（错误用法）：");

        // 只有1个线程的线程池，确保线程复用
        ExecutorService executor = Executors.newFixedThreadPool(1);

        CountDownLatch latch1 = new CountDownLatch(1);
        // 第一个任务：设置用户ID但不清理
        executor.submit(() -> {
            userIdContext.set("User-张三");
            System.out.println("  任务1 设置userId=" + userIdContext.get() +
                    " [" + Thread.currentThread().getName() + "]");
            // 注意：没有调用 userIdContext.remove()!
            latch1.countDown();
        });
        latch1.await();

        CountDownLatch latch2 = new CountDownLatch(1);
        // 第二个任务：没有设置userId，但能读到前一个任务的值！
        executor.submit(() -> {
            String userId = userIdContext.get();
            System.out.println("  任务2 读取userId=" + userId +
                    " [" + Thread.currentThread().getName() + "]");
            if (userId != null) {
                System.out.println("  ⚠ 数据污染！任务2读到了任务1的用户数据！");
            }
            userIdContext.remove(); // 补救清理
            latch2.countDown();
        });
        latch2.await();

        executor.shutdown();
        System.out.println();
    }

    /**
     * 演示装饰器模式解决方案
     * 通过包装Runnable，在任务执行前后自动管理ThreadLocal
     */
    private static void demonstrateDecoratorPattern() throws InterruptedException {
        System.out.println("2. 装饰器模式解决方案：");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(4);

        for (int i = 1; i <= 4; i++) {
            final String userId = "User-" + i;

            // 使用装饰器包装任务
            Runnable wrappedTask = new ThreadLocalCleanupRunnable(() -> {
                try {
                    userIdContext.set(userId);
                    System.out.println("  任务" + userId + " 设置并读取userId=" +
                            userIdContext.get() + " [" + Thread.currentThread().getName() + "]");
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });

            executor.submit(wrappedTask);
        }

        latch.await();
        executor.shutdown();
        System.out.println();
    }

    /**
     * 演示显式清理方式
     */
    private static void demonstrateExplicitCleanup() throws InterruptedException {
        System.out.println("3. 显式清理方式（推荐的标准写法）：");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(4);

        for (int i = 1; i <= 4; i++) {
            final int taskId = i;
            executor.submit(() -> {
                // 标准模板：try-finally
                try {
                    // 1. 任务开始时设置ThreadLocal
                    userIdContext.set("User-" + taskId);
                    requestCounter.get().incrementAndGet();

                    // 2. 执行业务逻辑
                    processRequest(taskId);

                } finally {
                    // 3. 任务结束时清理所有ThreadLocal
                    userIdContext.remove();
                    requestCounter.remove();
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        System.out.println("\n总结：");
        System.out.println("- 线程池中的线程会被复用，ThreadLocal值不会自动清除");
        System.out.println("- 不清理ThreadLocal会导致数据污染和内存泄漏");
        System.out.println("- 解决方案1：在任务的finally块中调用remove()");
        System.out.println("- 解决方案2：使用装饰器模式自动清理");
        System.out.println("- 解决方案3：使用框架提供的拦截器/过滤器统一清理");
    }

    /**
     * 模拟业务处理
     */
    private static void processRequest(int taskId) {
        String userId = userIdContext.get();
        int count = requestCounter.get().get();
        System.out.println("  处理任务" + taskId + " userId=" + userId +
                " 请求计数=" + count +
                " [" + Thread.currentThread().getName() + "]");
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * ThreadLocal自动清理的Runnable装饰器
     * 在任务执行完毕后自动清理所有已知的ThreadLocal变量
     */
    static class ThreadLocalCleanupRunnable implements Runnable {
        private final Runnable delegate;

        public ThreadLocalCleanupRunnable(Runnable delegate) {
            this.delegate = delegate;
        }

        @Override
        public void run() {
            try {
                delegate.run();
            } finally {
                // 自动清理所有ThreadLocal
                userIdContext.remove();
                requestCounter.remove();
            }
        }
    }
}
