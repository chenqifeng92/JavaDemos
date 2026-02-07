package com.chen.threadlocal;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 线程安全性测试
 * 验证ThreadLocal在多线程环境下的隔离性和安全性
 */
class ThreadSafetyTest {

    @Test
    void testThreadLocalProvidesSafeSimpleDateFormat() throws InterruptedException {
        // ThreadLocal为每个线程提供独立的SimpleDateFormat实例，保证线程安全
        ThreadLocal<SimpleDateFormat> dateFormatLocal =
                ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

        int threadCount = 20;
        int iterationsPerThread = 50;
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger errorCount = new AtomicInteger(0);

        String testDateStr = "2024-06-15";

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < iterationsPerThread; j++) {
                        SimpleDateFormat sdf = dateFormatLocal.get();
                        Date date = sdf.parse(testDateStr);
                        String result = sdf.format(date);
                        if (!testDateStr.equals(result)) {
                            errorCount.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    dateFormatLocal.remove();
                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        assertEquals(0, errorCount.get(), "使用ThreadLocal的SimpleDateFormat不应产生任何错误");
    }

    @Test
    void testMutableObjectIsolation() throws InterruptedException {
        // 验证可变对象在ThreadLocal中的隔离性
        ThreadLocal<List<String>> listLocal = ThreadLocal.withInitial(ArrayList::new);

        int threadCount = 5;
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<Integer> sizes = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threadCount; i++) {
            final int count = i + 1; // 每个线程添加不同数量的元素
            new Thread(() -> {
                try {
                    List<String> list = listLocal.get();
                    for (int j = 0; j < count; j++) {
                        list.add("item-" + j);
                    }
                    sizes.add(list.size());
                } finally {
                    listLocal.remove();
                    latch.countDown();
                }
            }).start();
        }

        latch.await();

        // 每个线程的list应该有独立的大小
        assertEquals(threadCount, sizes.size());
        Collections.sort(sizes);
        for (int i = 0; i < threadCount; i++) {
            assertEquals(i + 1, sizes.get(i), "每个线程的列表大小应该是独立的");
        }
    }

    @Test
    void testConcurrentCounterIsolation() throws InterruptedException {
        // 验证并发计数器的隔离性
        ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

        int threadCount = 10;
        int incrementPerThread = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<Integer> finalValues = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < incrementPerThread; j++) {
                        counter.set(counter.get() + 1);
                    }
                    finalValues.add(counter.get());
                } finally {
                    counter.remove();
                    latch.countDown();
                }
            }).start();
        }

        latch.await();

        // 每个线程的计数器应该都是100（而不是共享计数器那样的竞争结果）
        assertEquals(threadCount, finalValues.size());
        for (Integer value : finalValues) {
            assertEquals(incrementPerThread, value, "每个线程的计数器应该独立计数到" + incrementPerThread);
        }
    }

    @Test
    void testNoDataLeakBetweenTasks() throws Exception {
        // 验证线程池中不会有数据泄漏（正确使用remove的情况下）
        ThreadLocal<String> context = new ThreadLocal<>();
        ExecutorService executor = Executors.newFixedThreadPool(1); // 单线程池确保复用

        CountDownLatch latch1 = new CountDownLatch(1);
        // 任务1：设置值并清理
        executor.submit(() -> {
            try {
                context.set("secret-data");
                assertEquals("secret-data", context.get());
            } finally {
                context.remove();
                latch1.countDown();
            }
        });
        latch1.await();

        CountDownLatch latch2 = new CountDownLatch(1);
        // 任务2：验证读不到任务1的值
        Future<String> future = executor.submit(() -> {
            try {
                latch2.countDown();
                return context.get();
            } finally {
                context.remove();
            }
        });
        latch2.await();

        assertNull(future.get(), "正确清理后，复用的线程不应该读到之前任务的数据");
        executor.shutdown();
    }

    @Test
    void testDataPollutionWithoutRemove() throws Exception {
        // 验证不清理ThreadLocal会导致数据污染
        ThreadLocal<String> context = new ThreadLocal<>();
        ExecutorService executor = Executors.newFixedThreadPool(1); // 单线程池

        CountDownLatch latch1 = new CountDownLatch(1);
        // 任务1：设置值但不清理
        executor.submit(() -> {
            context.set("polluted-data");
            latch1.countDown();
            // 故意不调用 context.remove()
        });
        latch1.await();

        CountDownLatch latch2 = new CountDownLatch(1);
        // 任务2：应该能读到泄漏的数据
        Future<String> future = executor.submit(() -> {
            try {
                latch2.countDown();
                return context.get();
            } finally {
                context.remove(); // 清理
            }
        });
        latch2.await();

        assertEquals("polluted-data", future.get(), "不清理ThreadLocal会导致数据污染");
        executor.shutdown();
    }

    @Test
    void testHighConcurrencyStress() throws InterruptedException {
        // 高并发压力测试
        ThreadLocal<Long> local = new ThreadLocal<>();
        int threadCount = 100;
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    barrier.await(); // 同时开始
                    long id = Thread.currentThread().getId();
                    local.set(id);

                    // 执行100次读操作，确保值不变
                    for (int j = 0; j < 100; j++) {
                        if (id != local.get()) {
                            failCount.incrementAndGet();
                            break;
                        }
                        Thread.yield(); // 增加线程切换概率
                    }
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    local.remove();
                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        assertEquals(0, failCount.get(), "高并发下ThreadLocal的值应保持线程隔离");
    }
}
