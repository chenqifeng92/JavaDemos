package com.chen.threadlocal;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 内存泄漏检测测试
 * 验证ThreadLocal在不同使用场景下的内存行为
 */
class MemoryLeakTest {

    /**
     * 模拟大对象
     */
    static class HeavyObject {
        @SuppressWarnings("unused")
        private byte[] data;
        private String name;

        public HeavyObject(String name, int sizeInKB) {
            this.name = name;
            this.data = new byte[sizeInKB * 1024];
        }

        public String getName() { return name; }
    }

    @Test
    void testProperCleanupNoLeak() throws InterruptedException {
        // 正确清理ThreadLocal不会导致泄漏
        ThreadLocal<HeavyObject> heavyLocal = new ThreadLocal<>();
        int taskCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(taskCount);
        AtomicInteger cleanupCount = new AtomicInteger(0);

        for (int i = 0; i < taskCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    heavyLocal.set(new HeavyObject("obj-" + index, 10)); // 10KB
                    // 模拟业务操作
                    assertNotNull(heavyLocal.get());
                } finally {
                    heavyLocal.remove(); // 正确清理
                    cleanupCount.incrementAndGet();
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(taskCount, cleanupCount.get(), "所有任务都应该执行了清理");

        // 建议GC，验证对象可以被回收
        System.gc();
        Thread.sleep(100);
    }

    @Test
    void testRemoveAfterSetNull() throws InterruptedException {
        // 验证set(null)与remove()的区别
        ThreadLocal<String> local = new ThreadLocal<>();

        // set(null): Entry的value变为null，但Entry本身仍存在于ThreadLocalMap中
        local.set("value");
        local.set(null);
        assertNull(local.get(), "set(null)后get应返回null");

        // remove(): 从ThreadLocalMap中移除整个Entry
        local.set("value");
        local.remove();
        assertNull(local.get(), "remove()后get应返回null");

        // 对于withInitial的ThreadLocal，行为不同
        ThreadLocal<Integer> withInitial = ThreadLocal.withInitial(() -> 42);
        withInitial.set(null);
        assertNull(withInitial.get(), "set(null)后get应返回null而不是初始值");

        withInitial.remove();
        assertEquals(42, withInitial.get(), "remove()后get应返回初始值");
    }

    @Test
    void testThreadPoolWithRemovePattern() throws InterruptedException {
        // 验证线程池中正确使用remove的模式
        ThreadLocal<HeavyObject> local = new ThreadLocal<>();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        int rounds = 50;
        CountDownLatch latch = new CountDownLatch(rounds);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < rounds; i++) {
            final int round = i;
            executor.submit(() -> {
                try {
                    // 确保开始时没有前一任务的残留数据
                    assertNull(local.get(), "第" + round + "轮开始时应没有残留数据");

                    local.set(new HeavyObject("round-" + round, 5));
                    assertNotNull(local.get());
                    assertEquals("round-" + round, local.get().getName());

                    successCount.incrementAndGet();
                } finally {
                    local.remove();
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        assertEquals(rounds, successCount.get(), "所有轮次都应成功");
    }

    @Test
    void testStaticThreadLocalBestPractice() throws InterruptedException {
        // 验证static final ThreadLocal的最佳实践
        // static保证只有一个ThreadLocal实例，减少ThreadLocalMap中的Entry数量
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            new Thread(() -> {
                try {
                    StaticThreadLocalHolder.setValue("thread-" + index);
                    Thread.sleep(10);

                    String value = StaticThreadLocalHolder.getValue();
                    if (("thread-" + index).equals(value)) {
                        successCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    StaticThreadLocalHolder.clear();
                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        assertEquals(threadCount, successCount.get());
    }

    /**
     * 最佳实践：ThreadLocal声明为private static final
     */
    static class StaticThreadLocalHolder {
        private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

        public static void setValue(String value) {
            HOLDER.set(value);
        }

        public static String getValue() {
            return HOLDER.get();
        }

        public static void clear() {
            HOLDER.remove();
        }
    }

    @Test
    void testMultipleThreadLocalCleanup() throws InterruptedException {
        // 测试多个ThreadLocal都需要清理的场景
        ThreadLocal<String> local1 = new ThreadLocal<>();
        ThreadLocal<Integer> local2 = new ThreadLocal<>();
        ThreadLocal<Double> local3 = new ThreadLocal<>();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        int taskCount = 20;
        CountDownLatch latch = new CountDownLatch(taskCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < taskCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    // 验证三个ThreadLocal都没有前一任务的数据
                    boolean clean = local1.get() == null
                            && local2.get() == null
                            && local3.get() == null;

                    local1.set("str-" + index);
                    local2.set(index);
                    local3.set(index * 1.0);

                    if (clean) {
                        successCount.incrementAndGet();
                    }
                } finally {
                    // 必须清理所有ThreadLocal
                    local1.remove();
                    local2.remove();
                    local3.remove();
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        assertEquals(taskCount, successCount.get(), "所有任务开始时都应该是干净的");
    }
}
