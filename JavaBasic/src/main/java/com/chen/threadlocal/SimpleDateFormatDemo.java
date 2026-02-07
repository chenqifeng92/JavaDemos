package com.chen.threadlocal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SimpleDateFormat线程安全问题解决方案演示
 * SimpleDateFormat不是线程安全的，在多线程环境下会出现问题
 * 使用ThreadLocal可以优雅地解决这个问题
 */
public class SimpleDateFormatDemo {

    // 问题演示：共享的SimpleDateFormat实例（非线程安全）
    private static SimpleDateFormat sharedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    // 解决方案1：使用synchronized（性能较差）
    private static SimpleDateFormat synchronizedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    // 解决方案2：使用ThreadLocal（推荐）
    private static ThreadLocal<SimpleDateFormat> threadLocalDateFormat = ThreadLocal.withInitial(
        () -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    );
    
    // 用于统计错误次数
    private static AtomicInteger errorCount = new AtomicInteger(0);
    private static AtomicInteger successCount = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== SimpleDateFormat线程安全问题演示 ===\n");
        
        // 演示1：展示线程不安全问题
        demonstrateThreadUnsafe();
        
        // 演示2：使用synchronized解决
        demonstrateSynchronizedSolution();
        
        // 演示3：使用ThreadLocal解决
        demonstrateThreadLocalSolution();
        
        // 演示4：性能对比
        performanceComparison();
    }
    
    /**
     * 演示SimpleDateFormat的线程不安全问题
     */
    private static void demonstrateThreadUnsafe() throws InterruptedException {
        System.out.println("1. SimpleDateFormat线程不安全问题演示：");
        System.out.println("（可能出现异常或解析错误）");
        
        // 重置计数器
        errorCount.set(0);
        successCount.set(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);
        
        String[] dates = {
            "2024-01-01 10:30:45",
            "2024-02-15 14:20:30",
            "2024-03-20 08:15:10",
            "2024-04-25 16:45:55",
            "2024-05-30 12:00:00"
        };
        
        for (int i = 0; i < 100; i++) {
            final String dateStr = dates[i % dates.length];
            executor.submit(() -> {
                try {
                    // 使用共享的SimpleDateFormat实例（非线程安全）
                    Date date = sharedDateFormat.parse(dateStr);
                    String formatted = sharedDateFormat.format(date);
                    
                    // 验证解析结果是否正确
                    if (!dateStr.equals(formatted)) {
                        System.out.println("  解析错误: 期望=" + dateStr + ", 实际=" + formatted);
                        errorCount.incrementAndGet();
                    } else {
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    System.out.println("  异常: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        
        System.out.println("  结果统计: 成功=" + successCount.get() + ", 错误=" + errorCount.get());
        System.out.println();
    }
    
    /**
     * 使用synchronized解决线程安全问题
     */
    private static void demonstrateSynchronizedSolution() throws InterruptedException {
        System.out.println("2. 使用synchronized解决方案：");
        
        // 重置计数器
        errorCount.set(0);
        successCount.set(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);
        
        String testDate = "2024-06-15 09:30:00";
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                try {
                    Date date;
                    String formatted;
                    
                    // 使用synchronized保证线程安全
                    synchronized (synchronizedDateFormat) {
                        date = synchronizedDateFormat.parse(testDate);
                        formatted = synchronizedDateFormat.format(date);
                    }
                    
                    if (testDate.equals(formatted)) {
                        successCount.incrementAndGet();
                    } else {
                        errorCount.incrementAndGet();
                    }
                } catch (ParseException e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("  结果统计: 成功=" + successCount.get() + ", 错误=" + errorCount.get());
        System.out.println("  执行时间: " + (endTime - startTime) + "ms");
        System.out.println();
    }
    
    /**
     * 使用ThreadLocal解决线程安全问题
     */
    private static void demonstrateThreadLocalSolution() throws InterruptedException {
        System.out.println("3. 使用ThreadLocal解决方案（推荐）：");
        
        // 重置计数器
        errorCount.set(0);
        successCount.set(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);
        
        String testDate = "2024-07-20 15:45:30";
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                try {
                    // 每个线程使用自己的SimpleDateFormat实例
                    SimpleDateFormat formatter = threadLocalDateFormat.get();
                    Date date = formatter.parse(testDate);
                    String formatted = formatter.format(date);
                    
                    if (testDate.equals(formatted)) {
                        successCount.incrementAndGet();
                    } else {
                        errorCount.incrementAndGet();
                    }
                } catch (ParseException e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("  结果统计: 成功=" + successCount.get() + ", 错误=" + errorCount.get());
        System.out.println("  执行时间: " + (endTime - startTime) + "ms");
        System.out.println();
    }
    
    /**
     * 性能对比测试
     */
    private static void performanceComparison() throws InterruptedException {
        System.out.println("4. 性能对比测试（1000次操作）：");
        
        int iterations = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        // 测试synchronized方式
        CountDownLatch latch1 = new CountDownLatch(iterations);
        long startTime1 = System.currentTimeMillis();
        
        for (int i = 0; i < iterations; i++) {
            executor.submit(() -> {
                try {
                    synchronized (synchronizedDateFormat) {
                        Date date = synchronizedDateFormat.parse("2024-08-01 10:00:00");
                        synchronizedDateFormat.format(date);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    latch1.countDown();
                }
            });
        }
        
        latch1.await();
        long syncTime = System.currentTimeMillis() - startTime1;
        
        // 测试ThreadLocal方式
        CountDownLatch latch2 = new CountDownLatch(iterations);
        long startTime2 = System.currentTimeMillis();
        
        for (int i = 0; i < iterations; i++) {
            executor.submit(() -> {
                try {
                    SimpleDateFormat formatter = threadLocalDateFormat.get();
                    Date date = formatter.parse("2024-08-01 10:00:00");
                    formatter.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    latch2.countDown();
                }
            });
        }
        
        latch2.await();
        long threadLocalTime = System.currentTimeMillis() - startTime2;
        
        executor.shutdown();
        
        System.out.println("  synchronized方式耗时: " + syncTime + "ms");
        System.out.println("  ThreadLocal方式耗时: " + threadLocalTime + "ms");
        System.out.println("  性能提升: " + 
                         String.format("%.2f", (double)syncTime/threadLocalTime) + "倍");
        
        System.out.println("\n总结：");
        System.out.println("- SimpleDateFormat不是线程安全的，共享实例会导致错误");
        System.out.println("- synchronized可以解决问题，但性能较差");
        System.out.println("- ThreadLocal为每个线程提供独立实例，既安全又高效");
        System.out.println("- 注意：使用ThreadLocal后要在合适的时机调用remove()避免内存泄漏");
    }
}