package com.chen.threadlocal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ThreadLocal基础功能测试
 * 验证ThreadLocal的核心API：set、get、remove、withInitial
 */
class BasicThreadLocalTest {

    private final ThreadLocal<String> threadLocal = new ThreadLocal<>();
    private final ThreadLocal<Integer> threadLocalWithInitial = ThreadLocal.withInitial(() -> 42);

    @AfterEach
    void cleanup() {
        threadLocal.remove();
        threadLocalWithInitial.remove();
    }

    @Test
    void testGetReturnsNullWithoutSet() {
        // 未设置值时，get返回null
        assertNull(threadLocal.get());
    }

    @Test
    void testSetAndGet() {
        // 设置并获取值
        threadLocal.set("hello");
        assertEquals("hello", threadLocal.get());
    }

    @Test
    void testOverwriteValue() {
        // 多次set覆盖值
        threadLocal.set("first");
        threadLocal.set("second");
        assertEquals("second", threadLocal.get());
    }

    @Test
    void testRemove() {
        // remove后get返回null
        threadLocal.set("hello");
        threadLocal.remove();
        assertNull(threadLocal.get());
    }

    @Test
    void testRemoveWithoutSet() {
        // 未set的情况下remove不会抛异常
        assertDoesNotThrow(() -> threadLocal.remove());
    }

    @Test
    void testWithInitialValue() {
        // withInitial设置的初始值
        assertEquals(42, threadLocalWithInitial.get());
    }

    @Test
    void testWithInitialAfterRemove() {
        // remove后再get会重新调用initialValue
        threadLocalWithInitial.set(100);
        assertEquals(100, threadLocalWithInitial.get());

        threadLocalWithInitial.remove();
        assertEquals(42, threadLocalWithInitial.get()); // 恢复初始值
    }

    @Test
    void testSetNullValue() {
        // 可以显式设置null
        threadLocalWithInitial.set(null);
        assertNull(threadLocalWithInitial.get()); // 返回null而不是初始值
    }

    @Test
    void testOverrideInitialValue() {
        // 自定义initialValue的ThreadLocal
        ThreadLocal<String> custom = new ThreadLocal<String>() {
            @Override
            protected String initialValue() {
                return "默认值-" + Thread.currentThread().getName();
            }
        };

        String value = custom.get();
        assertNotNull(value);
        assertTrue(value.startsWith("默认值-"));
        custom.remove();
    }

    @Test
    void testThreadIsolation() throws InterruptedException {
        // 验证不同线程之间的隔离性
        threadLocal.set("主线程的值");

        List<String> childValues = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        Thread child = new Thread(() -> {
            // 子线程应该看不到主线程的值
            childValues.add(String.valueOf(threadLocal.get()));
            latch.countDown();
        });
        child.start();
        latch.await();

        assertEquals("主线程的值", threadLocal.get());
        assertEquals("null", childValues.get(0));
    }

    @Test
    void testMultipleThreadsIndependent() throws InterruptedException {
        // 多个线程各自独立设置和读取
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<Boolean> results = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final String expected = "value-" + i;
            new Thread(() -> {
                threadLocal.set(expected);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (results) {
                    results.add(expected.equals(threadLocal.get()));
                }
                threadLocal.remove();
                latch.countDown();
            }).start();
        }

        latch.await();
        assertEquals(threadCount, results.size());
        assertTrue(results.stream().allMatch(Boolean::booleanValue), "所有线程都应该读到自己设置的值");
    }

    @Test
    void testConcurrentSetGet() throws Exception {
        // 高并发下的线程安全性
        int threadCount = 50;
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<Boolean> results = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int value = i;
            new Thread(() -> {
                try {
                    barrier.await(); // 所有线程同时开始
                    threadLocalWithInitial.set(value);
                    Thread.sleep(10);
                    synchronized (results) {
                        results.add(value == threadLocalWithInitial.get());
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                } finally {
                    threadLocalWithInitial.remove();
                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        assertEquals(threadCount, results.size());
        assertTrue(results.stream().allMatch(Boolean::booleanValue));
    }
}
