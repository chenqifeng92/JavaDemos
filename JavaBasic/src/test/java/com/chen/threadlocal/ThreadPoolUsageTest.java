package com.chen.threadlocal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 线程池场景测试
 * 验证在线程池中使用ThreadLocal的各种场景和注意事项
 */
class ThreadPoolUsageTest {

    private final ThreadLocal<String> context = new ThreadLocal<>();
    private ExecutorService executor;

    @AfterEach
    void tearDown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        context.remove();
    }

    @Test
    void testThreadReuseInFixedPool() throws Exception {
        // 验证固定线程池中的线程复用
        executor = Executors.newFixedThreadPool(1);
        List<Long> threadIds = Collections.synchronizedList(new ArrayList<>());

        int taskCount = 5;
        CountDownLatch latch = new CountDownLatch(taskCount);

        for (int i = 0; i < taskCount; i++) {
            executor.submit(() -> {
                threadIds.add(Thread.currentThread().getId());
                latch.countDown();
            });
        }

        latch.await();

        // 所有任务应该在同一个线程上执行
        long firstId = threadIds.get(0);
        assertTrue(threadIds.stream().allMatch(id -> id == firstId),
                "固定大小为1的线程池中，所有任务应该在同一个线程执行");
    }

    @Test
    void testCorrectCleanupPattern() throws InterruptedException {
        // 测试正确的清理模式
        executor = Executors.newFixedThreadPool(2);
        int taskCount = 20;
        CountDownLatch latch = new CountDownLatch(taskCount);
        AtomicInteger cleanStartCount = new AtomicInteger(0);

        for (int i = 0; i < taskCount; i++) {
            final String taskValue = "task-" + i;
            executor.submit(() -> {
                try {
                    // 验证开始时是干净的
                    if (context.get() == null) {
                        cleanStartCount.incrementAndGet();
                    }

                    context.set(taskValue);
                    assertEquals(taskValue, context.get());
                } finally {
                    context.remove();
                    latch.countDown();
                }
            });
        }

        latch.await();
        assertEquals(taskCount, cleanStartCount.get(),
                "每个任务开始时ThreadLocal都应该是null");
    }

    @Test
    void testCachedThreadPoolBehavior() throws InterruptedException {
        // CachedThreadPool会动态创建和回收线程
        executor = Executors.newCachedThreadPool();
        int taskCount = 10;
        CountDownLatch latch = new CountDownLatch(taskCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < taskCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    context.set("cached-" + index);
                    Thread.sleep(10);
                    if (("cached-" + index).equals(context.get())) {
                        successCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    context.remove();
                    latch.countDown();
                }
            });
        }

        latch.await();
        assertEquals(taskCount, successCount.get());
    }

    @Test
    void testScheduledThreadPoolBehavior() throws InterruptedException {
        // 定时线程池中的ThreadLocal行为
        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);
        ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

        int repeatCount = 5;
        CountDownLatch latch = new CountDownLatch(repeatCount);
        List<Integer> values = Collections.synchronizedList(new ArrayList<>());

        // 注意：定时任务使用同一个线程，如果不清理ThreadLocal会累积
        for (int i = 0; i < repeatCount; i++) {
            scheduledExecutor.schedule(() -> {
                try {
                    // 正确做法：每次重新初始化
                    counter.set(0);
                    counter.set(counter.get() + 1);
                    values.add(counter.get());
                } finally {
                    counter.remove();
                    latch.countDown();
                }
            }, i * 50, TimeUnit.MILLISECONDS);
        }

        latch.await();
        scheduledExecutor.shutdown();

        // 每次都应该是1（因为每次都正确清理并重新设置）
        for (Integer value : values) {
            assertEquals(1, value, "每次定时任务应该从初始状态开始");
        }
    }

    @Test
    void testCallableWithThreadLocal() throws Exception {
        // 测试Callable + ThreadLocal
        executor = Executors.newFixedThreadPool(3);
        int taskCount = 10;
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < taskCount; i++) {
            final int index = i;
            futures.add(executor.submit(() -> {
                try {
                    context.set("callable-" + index);
                    Thread.sleep(20);
                    return context.get();
                } finally {
                    context.remove();
                }
            }));
        }

        for (int i = 0; i < taskCount; i++) {
            assertEquals("callable-" + i, futures.get(i).get(),
                    "Callable返回的值应该与设置的值一致");
        }
    }

    @Test
    void testInvokeAllWithThreadLocal() throws Exception {
        // 测试invokeAll批量提交
        executor = Executors.newFixedThreadPool(3);

        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int index = i;
            tasks.add(() -> {
                try {
                    context.set("invokeAll-" + index);
                    Thread.sleep(10);
                    return context.get();
                } finally {
                    context.remove();
                }
            });
        }

        List<Future<String>> results = executor.invokeAll(tasks);

        for (int i = 0; i < 5; i++) {
            assertEquals("invokeAll-" + i, results.get(i).get());
        }
    }

    @Test
    void testDecoratorPatternForAutoCleanup() throws InterruptedException {
        // 测试装饰器模式实现自动清理
        executor = Executors.newFixedThreadPool(2);
        int taskCount = 10;
        CountDownLatch latch = new CountDownLatch(taskCount);
        AtomicInteger cleanCount = new AtomicInteger(0);

        for (int i = 0; i < taskCount; i++) {
            final String value = "decorated-" + i;
            executor.submit(withCleanup(() -> {
                context.set(value);
                assertEquals(value, context.get());
                latch.countDown();
            }));
        }

        latch.await();

        // 验证所有清理都执行了（通过提交额外任务检查）
        CountDownLatch verifyLatch = new CountDownLatch(2);
        for (int i = 0; i < 2; i++) {
            executor.submit(() -> {
                if (context.get() == null) {
                    cleanCount.incrementAndGet();
                }
                verifyLatch.countDown();
            });
        }
        verifyLatch.await();
        assertEquals(2, cleanCount.get(), "装饰器应该自动清理ThreadLocal");
    }

    @Test
    void testSingleThreadExecutorConsistency() throws Exception {
        // 单线程执行器中的顺序保证
        executor = Executors.newSingleThreadExecutor();
        ThreadLocal<Integer> seqLocal = new ThreadLocal<>();
        int taskCount = 10;

        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < taskCount; i++) {
            final int expected = i;
            futures.add(executor.submit(() -> {
                try {
                    // 验证没有前一任务的数据
                    boolean isClean = seqLocal.get() == null;
                    seqLocal.set(expected);
                    return isClean && expected == seqLocal.get();
                } finally {
                    seqLocal.remove();
                }
            }));
        }

        for (Future<Boolean> future : futures) {
            assertTrue(future.get(), "每次任务开始时应是干净的，且设置的值正确");
        }
    }

    /**
     * 装饰器：自动清理ThreadLocal
     */
    private Runnable withCleanup(Runnable task) {
        return () -> {
            try {
                task.run();
            } finally {
                context.remove();
            }
        };
    }
}
