package com.chen.threadlocal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * InheritableThreadLocal继承性测试
 * 验证父子线程之间的值传递行为
 */
class InheritableThreadLocalTest {

    private final InheritableThreadLocal<String> inheritableLocal = new InheritableThreadLocal<>();
    private final ThreadLocal<String> normalLocal = new ThreadLocal<>();

    @AfterEach
    void cleanup() {
        inheritableLocal.remove();
        normalLocal.remove();
    }

    @Test
    void testChildThreadInheritsValue() throws InterruptedException {
        // 子线程应该继承父线程的InheritableThreadLocal值
        inheritableLocal.set("父线程的值");

        CountDownLatch latch = new CountDownLatch(1);
        List<String> childValues = new ArrayList<>();

        Thread child = new Thread(() -> {
            childValues.add(inheritableLocal.get());
            latch.countDown();
        });
        child.start();
        latch.await();

        assertEquals("父线程的值", childValues.get(0));
    }

    @Test
    void testNormalThreadLocalNotInherited() throws InterruptedException {
        // 普通ThreadLocal的值不会被子线程继承
        normalLocal.set("父线程的值");

        CountDownLatch latch = new CountDownLatch(1);
        List<String> childValues = new ArrayList<>();

        Thread child = new Thread(() -> {
            childValues.add(String.valueOf(normalLocal.get()));
            latch.countDown();
        });
        child.start();
        latch.await();

        assertEquals("null", childValues.get(0));
    }

    @Test
    void testChildModificationDoesNotAffectParent() throws InterruptedException {
        // 子线程修改值不会影响父线程
        inheritableLocal.set("原始值");

        CountDownLatch latch = new CountDownLatch(1);

        Thread child = new Thread(() -> {
            inheritableLocal.set("子线程修改的值");
            latch.countDown();
        });
        child.start();
        latch.await();

        assertEquals("原始值", inheritableLocal.get(), "父线程的值不应受子线程影响");
    }

    @Test
    void testGrandChildInheritsFromChild() throws InterruptedException {
        // 孙线程应该继承子线程的值
        inheritableLocal.set("祖父值");

        CountDownLatch latch = new CountDownLatch(1);
        List<String> grandChildValues = new ArrayList<>();

        Thread child = new Thread(() -> {
            inheritableLocal.set("父值"); // 子线程修改值

            Thread grandChild = new Thread(() -> {
                grandChildValues.add(inheritableLocal.get());
                latch.countDown();
            });
            grandChild.start();
        });
        child.start();
        latch.await();

        assertEquals("父值", grandChildValues.get(0), "孙线程应该继承子线程修改后的值");
    }

    @Test
    void testChildValueTransformation() throws InterruptedException {
        // 测试childValue方法的值转换功能
        InheritableThreadLocal<Integer> transformLocal = new InheritableThreadLocal<Integer>() {
            @Override
            protected Integer childValue(Integer parentValue) {
                return parentValue * 2; // 子线程获得父线程值的2倍
            }
        };

        transformLocal.set(50);

        CountDownLatch latch = new CountDownLatch(1);
        List<Integer> childValues = new ArrayList<>();

        Thread child = new Thread(() -> {
            childValues.add(transformLocal.get());
            latch.countDown();
        });
        child.start();
        latch.await();

        assertEquals(100, childValues.get(0), "子线程应该获得父线程值的2倍");
        transformLocal.remove();
    }

    @Test
    void testInheritanceHappensAtCreationTime() throws InterruptedException {
        // 验证继承发生在线程创建时，而不是启动时
        inheritableLocal.set("创建时的值");

        // 先创建线程
        CountDownLatch latch = new CountDownLatch(1);
        List<String> childValues = new ArrayList<>();

        Thread child = new Thread(() -> {
            childValues.add(inheritableLocal.get());
            latch.countDown();
        });

        // 在启动前修改父线程的值
        inheritableLocal.set("启动前修改的值");

        // 启动线程
        child.start();
        latch.await();

        // 子线程应该看到创建时的值，而不是启动前修改的值
        assertEquals("创建时的值", childValues.get(0),
                "继承应该发生在线程创建时(new Thread)，而不是启动时(start)");
    }

    @Test
    void testMultipleChildThreadsGetIndependentCopies() throws InterruptedException {
        // 多个子线程获得的是独立的副本
        inheritableLocal.set("共享初始值");

        int childCount = 5;
        CountDownLatch latch = new CountDownLatch(childCount);
        List<String> results = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < childCount; i++) {
            final int index = i;
            new Thread(() -> {
                // 每个子线程都能读到继承的值
                String inherited = inheritableLocal.get();
                // 修改自己的值
                inheritableLocal.set("子线程" + index + "的值");
                // 记录修改后的值
                results.add(inheritableLocal.get());
                inheritableLocal.remove();
                latch.countDown();
            }).start();
        }

        latch.await();

        assertEquals(childCount, results.size());
        for (int i = 0; i < childCount; i++) {
            assertTrue(results.contains("子线程" + i + "的值"), "每个子线程应该有自己的独立副本");
        }
        // 父线程的值不受影响
        assertEquals("共享初始值", inheritableLocal.get());
    }

    @Test
    void testInheritableWithNullValue() throws InterruptedException {
        // 测试父线程值为null的继承行为
        inheritableLocal.set(null);

        CountDownLatch latch = new CountDownLatch(1);
        List<String> results = new ArrayList<>();

        Thread child = new Thread(() -> {
            results.add(String.valueOf(inheritableLocal.get()));
            latch.countDown();
        });
        child.start();
        latch.await();

        assertEquals("null", results.get(0), "null值也应该被继承");
    }

    @Test
    void testDeepInheritanceChain() throws InterruptedException {
        // 测试深层继承链
        InheritableThreadLocal<Integer> depthLocal = new InheritableThreadLocal<Integer>() {
            @Override
            protected Integer childValue(Integer parentValue) {
                return parentValue + 1; // 每层加1
            }
        };

        depthLocal.set(0); // 根线程: 0

        CountDownLatch latch = new CountDownLatch(1);
        List<Integer> deepValue = new ArrayList<>();

        // 创建3层嵌套线程
        Thread level1 = new Thread(() -> {
            // level1应该是1
            Thread level2 = new Thread(() -> {
                // level2应该是2
                Thread level3 = new Thread(() -> {
                    // level3应该是3
                    deepValue.add(depthLocal.get());
                    latch.countDown();
                });
                level3.start();
            });
            level2.start();
        });
        level1.start();
        latch.await();

        assertEquals(3, deepValue.get(0), "3层嵌套后值应该是3");
        depthLocal.remove();
    }
}
