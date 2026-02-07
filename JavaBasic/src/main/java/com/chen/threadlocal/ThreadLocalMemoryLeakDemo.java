package com.chen.threadlocal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;

/**
 * ThreadLocal内存泄漏演示及预防
 *
 * 内存泄漏原因分析：
 * 1. ThreadLocalMap的Entry继承自WeakReference<ThreadLocal<?>>，key是弱引用
 * 2. 当ThreadLocal变量被设为null后，key会被GC回收，但value仍然强引用存在
 * 3. 如果线程长时间运行（如线程池），这些value不会被回收，导致内存泄漏
 *
 * 预防措施：
 * - 使用完毕后务必调用remove()方法
 * - 将ThreadLocal声明为private static final（避免重复创建）
 * - 在try-finally块中确保清理
 */
public class ThreadLocalMemoryLeakDemo {

    /**
     * 模拟一个占用较大内存的对象
     */
    static class LargeObject {
        private String name;
        // 模拟占用内存（约1KB）
        private byte[] data = new byte[1024];

        public LargeObject(String name) {
            this.name = name;
        }

        public String getName() { return name; }

        @Override
        public String toString() {
            return "LargeObject{name='" + name + "', size=" + data.length + "B}";
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ThreadLocal内存泄漏演示 ===\n");

        // 演示1：展示内存泄漏场景
        demonstrateMemoryLeakScenario();

        // 演示2：正确使用方式
        demonstrateCorrectUsage();

        // 演示3：最佳实践总结
        bestPracticesSummary();
    }

    /**
     * 演示内存泄漏场景
     * 在线程池中使用ThreadLocal但不remove，会导致数据残留
     */
    private static void demonstrateMemoryLeakScenario() throws InterruptedException {
        System.out.println("1. 内存泄漏场景演示（线程池 + 不调用remove）：");

        // 使用固定大小的线程池（线程会被复用）
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(4);

        // 错误用法：不调用remove
        for (int i = 0; i < 4; i++) {
            final int taskId = i;
            executor.submit(() -> {
                // 每次任务都创建新的ThreadLocal（模拟不规范的用法）
                ThreadLocal<LargeObject> badThreadLocal = new ThreadLocal<>();
                try {
                    LargeObject obj = new LargeObject("Task-" + taskId);
                    badThreadLocal.set(obj);

                    System.out.println("  任务" + taskId + " [" + Thread.currentThread().getName() +
                            "] 设置值: " + badThreadLocal.get());

                    Thread.sleep(50);

                    // 注意：这里没有调用 badThreadLocal.remove()!
                    // 即使badThreadLocal局部变量被回收（key变为null），
                    // ThreadLocalMap中的value仍然存在，直到线程结束或被清理
                    System.out.println("  任务" + taskId + " [" + Thread.currentThread().getName() +
                            "] 完成（未调用remove!）");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        System.out.println("  ⚠ 线程池中的线程不会销毁，ThreadLocal的value一直残留在内存中\n");
    }

    /**
     * 演示正确的使用方式
     */
    private static void demonstrateCorrectUsage() throws InterruptedException {
        System.out.println("2. 正确使用方式（使用try-finally确保remove）：");

        // 最佳实践：将ThreadLocal声明为static final
        final ThreadLocal<LargeObject> threadLocal = new ThreadLocal<>();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(4);

        for (int i = 0; i < 4; i++) {
            final int taskId = i;
            executor.submit(() -> {
                // 正确用法：try-finally确保remove
                try {
                    LargeObject obj = new LargeObject("Task-" + taskId);
                    threadLocal.set(obj);

                    System.out.println("  任务" + taskId + " [" + Thread.currentThread().getName() +
                            "] 设置值: " + threadLocal.get());

                    Thread.sleep(50);

                    System.out.println("  任务" + taskId + " [" + Thread.currentThread().getName() +
                            "] 处理完成");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // 关键：在finally中调用remove
                    threadLocal.remove();
                    System.out.println("  任务" + taskId + " [" + Thread.currentThread().getName() +
                            "] 已清理ThreadLocal");
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        System.out.println();
    }

    /**
     * 最佳实践总结
     */
    private static void bestPracticesSummary() {
        System.out.println("3. ThreadLocal内存泄漏防范最佳实践：\n");

        System.out.println("  [原因分析]");
        System.out.println("  - ThreadLocalMap的key是WeakReference<ThreadLocal<?>>");
        System.out.println("  - 当ThreadLocal变量没有强引用时，key会被GC回收变为null");
        System.out.println("  - 但value是强引用，不会被回收");
        System.out.println("  - 线程池中的线程不会销毁，ThreadLocalMap一直存在");
        System.out.println("  - 因此value永远不会被回收 => 内存泄漏\n");

        System.out.println("  [预防措施]");
        System.out.println("  1. 使用后必须调用remove()，推荐在try-finally中调用");
        System.out.println("  2. 将ThreadLocal声明为private static final");
        System.out.println("     - static保证只有一个实例（减少Entry数量）");
        System.out.println("     - final防止被重新赋值（避免key变弱引用后被回收）");
        System.out.println("  3. 避免在ThreadLocal中存储大对象");
        System.out.println("  4. 避免在线程池中使用局部ThreadLocal变量\n");

        System.out.println("  [代码模板]");
        System.out.println("  private static final ThreadLocal<YourType> CONTEXT = new ThreadLocal<>();\n");
        System.out.println("  public void doSomething() {");
        System.out.println("      try {");
        System.out.println("          CONTEXT.set(value);");
        System.out.println("          // 业务逻辑");
        System.out.println("      } finally {");
        System.out.println("          CONTEXT.remove(); // 必须清理!");
        System.out.println("      }");
        System.out.println("  }");
    }
}
