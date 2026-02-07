package com.chen.threadlocal;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ThreadLocal性能对比测试
 * 对比ThreadLocal、synchronized、ConcurrentHashMap等不同方案的性能
 */
class PerformanceTest {

    private static final int THREAD_COUNT = 10;
    private static final int ITERATIONS = 10_000;

    @Test
    void testThreadLocalPerformance() throws InterruptedException {
        // 测试ThreadLocal的读写性能
        ThreadLocal<Long> local = ThreadLocal.withInitial(() -> 0L);
        long elapsed = measurePerformance(() -> {
            local.set(local.get() + 1);
        }, local::remove);

        System.out.println("ThreadLocal读写耗时: " + elapsed + "ms");
        assertTrue(elapsed < 10000, "ThreadLocal操作应该在合理时间内完成");
    }

    @Test
    void testSynchronizedPerformance() throws InterruptedException {
        // 测试synchronized的性能
        final long[] sharedValue = {0};
        final Object lock = new Object();

        long elapsed = measurePerformance(() -> {
            synchronized (lock) {
                sharedValue[0]++;
            }
        }, () -> {});

        System.out.println("synchronized读写耗时: " + elapsed + "ms");
        assertTrue(elapsed < 10000, "synchronized操作应该在合理时间内完成");
    }

    @Test
    void testAtomicLongPerformance() throws InterruptedException {
        // 测试AtomicLong的性能
        AtomicLong atomicValue = new AtomicLong(0);

        long elapsed = measurePerformance(atomicValue::incrementAndGet, () -> {});

        System.out.println("AtomicLong读写耗时: " + elapsed + "ms");
        assertTrue(elapsed < 10000, "AtomicLong操作应该在合理时间内完成");
    }

    @Test
    void testConcurrentHashMapPerformance() throws InterruptedException {
        // 测试ConcurrentHashMap的性能
        ConcurrentHashMap<Long, Long> map = new ConcurrentHashMap<>();

        long elapsed = measurePerformance(() -> {
            long threadId = Thread.currentThread().getId();
            Long current = map.getOrDefault(threadId, 0L);
            map.put(threadId, current + 1);
        }, () -> {
            map.remove(Thread.currentThread().getId());
        });

        System.out.println("ConcurrentHashMap读写耗时: " + elapsed + "ms");
        assertTrue(elapsed < 10000, "ConcurrentHashMap操作应该在合理时间内完成");
    }

    @Test
    void testPerformanceComparison() throws InterruptedException {
        // 综合性能对比
        System.out.println("\n=== 性能对比 (" + THREAD_COUNT + "线程 x " + ITERATIONS + "次迭代) ===");

        // 1. ThreadLocal
        ThreadLocal<Long> local = ThreadLocal.withInitial(() -> 0L);
        long threadLocalTime = measurePerformance(() -> {
            local.set(local.get() + 1);
        }, local::remove);

        // 2. synchronized
        final long[] syncValue = {0};
        final Object lock = new Object();
        long syncTime = measurePerformance(() -> {
            synchronized (lock) {
                syncValue[0]++;
            }
        }, () -> {});

        // 3. AtomicLong
        AtomicLong atomic = new AtomicLong(0);
        long atomicTime = measurePerformance(atomic::incrementAndGet, () -> {});

        System.out.println("ThreadLocal:       " + threadLocalTime + "ms");
        System.out.println("synchronized:      " + syncTime + "ms");
        System.out.println("AtomicLong:        " + atomicTime + "ms");

        // ThreadLocal在高并发读写场景下通常性能更好（因为无锁竞争）
        // 但不做严格断言，因为性能受运行环境影响
        System.out.println("\n说明: ThreadLocal因为没有锁竞争，在高并发场景下通常性能较好");
    }

    @Test
    void testThreadLocalCreationCost() {
        // 测试ThreadLocal创建的开销
        int count = 100_000;
        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            ThreadLocal<String> tl = new ThreadLocal<>();
            tl.set("value");
            tl.remove();
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("创建" + count + "个ThreadLocal耗时: " + elapsed + "ms");
        assertTrue(elapsed < 5000, "ThreadLocal创建不应该太慢");
    }

    /**
     * 通用性能测量工具方法
     */
    private long measurePerformance(Runnable operation, Runnable cleanup)
            throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                try {
                    barrier.await();
                    for (int j = 0; j < ITERATIONS; j++) {
                        operation.run();
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                } finally {
                    cleanup.run();
                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        return System.currentTimeMillis() - startTime;
    }
}
