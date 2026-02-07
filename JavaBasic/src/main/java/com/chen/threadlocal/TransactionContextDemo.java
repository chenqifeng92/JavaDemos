package com.chen.threadlocal;

import java.util.UUID;

/**
 * 事务上下文管理示例
 * 模拟数据库事务在同一线程中的传播，展示ThreadLocal在事务管理中的典型应用。
 * 类似于Spring中的TransactionSynchronizationManager实现原理。
 */
public class TransactionContextDemo {

    /**
     * 事务信息
     */
    static class TransactionInfo {
        private String transactionId;
        private String dataSource;
        private int isolationLevel;
        private boolean readOnly;
        private long startTime;

        public TransactionInfo(String transactionId, String dataSource) {
            this.transactionId = transactionId;
            this.dataSource = dataSource;
            this.isolationLevel = 2; // TRANSACTION_READ_COMMITTED
            this.readOnly = false;
            this.startTime = System.currentTimeMillis();
        }

        public String getTransactionId() { return transactionId; }
        public String getDataSource() { return dataSource; }
        public int getIsolationLevel() { return isolationLevel; }
        public void setIsolationLevel(int isolationLevel) { this.isolationLevel = isolationLevel; }
        public boolean isReadOnly() { return readOnly; }
        public void setReadOnly(boolean readOnly) { this.readOnly = readOnly; }
        public long getStartTime() { return startTime; }

        @Override
        public String toString() {
            return "TransactionInfo{" +
                    "txId='" + transactionId + '\'' +
                    ", ds='" + dataSource + '\'' +
                    ", isolation=" + isolationLevel +
                    ", readOnly=" + readOnly +
                    '}';
        }
    }

    /**
     * 事务上下文管理器
     * 使用ThreadLocal保存当前线程的事务信息，模拟Spring的事务管理机制
     */
    static class TransactionManager {
        // 存储当前线程的事务信息
        private static final ThreadLocal<TransactionInfo> currentTransaction = new ThreadLocal<>();
        // 存储事务嵌套深度（用于支持嵌套事务/REQUIRES_NEW等传播行为）
        private static final ThreadLocal<Integer> transactionDepth = ThreadLocal.withInitial(() -> 0);

        /**
         * 开启事务
         */
        public static void beginTransaction(String dataSource) {
            TransactionInfo existing = currentTransaction.get();
            if (existing != null) {
                // 已有事务，增加嵌套深度
                int depth = transactionDepth.get() + 1;
                transactionDepth.set(depth);
                System.out.println("  [事务管理器] 加入已有事务，嵌套深度: " + depth +
                        "，事务ID: " + existing.getTransactionId());
                return;
            }

            // 创建新事务
            String txId = UUID.randomUUID().toString().substring(0, 8);
            TransactionInfo txInfo = new TransactionInfo(txId, dataSource);
            currentTransaction.set(txInfo);
            transactionDepth.set(1);
            System.out.println("  [事务管理器] 开启新事务: " + txInfo);
        }

        /**
         * 提交事务
         */
        public static void commit() {
            TransactionInfo txInfo = currentTransaction.get();
            if (txInfo == null) {
                System.out.println("  [事务管理器] 错误：没有活跃的事务可以提交");
                return;
            }

            int depth = transactionDepth.get();
            if (depth > 1) {
                // 嵌套事务，只减少深度
                transactionDepth.set(depth - 1);
                System.out.println("  [事务管理器] 嵌套事务提交（保存点释放），剩余深度: " + (depth - 1));
            } else {
                // 最外层事务，真正提交
                long duration = System.currentTimeMillis() - txInfo.getStartTime();
                System.out.println("  [事务管理器] 提交事务: " + txInfo.getTransactionId() +
                        "，耗时: " + duration + "ms");
                cleanup();
            }
        }

        /**
         * 回滚事务
         */
        public static void rollback() {
            TransactionInfo txInfo = currentTransaction.get();
            if (txInfo == null) {
                System.out.println("  [事务管理器] 错误：没有活跃的事务可以回滚");
                return;
            }
            System.out.println("  [事务管理器] 回滚事务: " + txInfo.getTransactionId());
            cleanup();
        }

        /**
         * 获取当前事务信息
         */
        public static TransactionInfo getCurrentTransaction() {
            return currentTransaction.get();
        }

        /**
         * 检查是否有活跃事务
         */
        public static boolean isTransactionActive() {
            return currentTransaction.get() != null;
        }

        /**
         * 清理事务上下文（非常重要，防止内存泄漏）
         */
        private static void cleanup() {
            currentTransaction.remove();
            transactionDepth.remove();
        }
    }

    /**
     * 模拟Service层 - 用户服务
     */
    static class UserService {
        private OrderService orderService = new OrderService();

        public void createUserWithOrder(String username) {
            System.out.println("\n--- 创建用户并下单 ---");
            TransactionManager.beginTransaction("主数据源");

            try {
                // 模拟创建用户
                System.out.println("  [UserService] 创建用户: " + username +
                        "（事务ID: " + TransactionManager.getCurrentTransaction().getTransactionId() + "）");
                Thread.sleep(50);

                // 调用OrderService（在同一个事务中）
                orderService.createOrder(username, "商品A");

                // 提交事务
                TransactionManager.commit();
                System.out.println("  [UserService] 用户创建成功\n");
            } catch (Exception e) {
                TransactionManager.rollback();
                System.out.println("  [UserService] 用户创建失败，事务已回滚: " + e.getMessage() + "\n");
            }
        }
    }

    /**
     * 模拟Service层 - 订单服务
     */
    static class OrderService {
        public void createOrder(String username, String product) {
            // 加入已有事务
            TransactionManager.beginTransaction("主数据源");

            try {
                TransactionInfo tx = TransactionManager.getCurrentTransaction();
                System.out.println("  [OrderService] 为用户 " + username + " 创建订单: " + product +
                        "（事务ID: " + tx.getTransactionId() + "）");
                Thread.sleep(30);

                // 提交（嵌套事务只减少深度）
                TransactionManager.commit();
            } catch (InterruptedException e) {
                TransactionManager.rollback();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 事务上下文管理演示 ===\n");

        // 演示1：单线程事务传播
        singleThreadTransactionDemo();

        // 演示2：多线程事务隔离
        multiThreadTransactionDemo();

        // 演示3：事务回滚场景
        transactionRollbackDemo();
    }

    /**
     * 单线程事务传播演示
     */
    private static void singleThreadTransactionDemo() {
        System.out.println("1. 单线程事务传播：");
        UserService userService = new UserService();
        userService.createUserWithOrder("张三");
    }

    /**
     * 多线程事务隔离演示
     */
    private static void multiThreadTransactionDemo() throws InterruptedException {
        System.out.println("2. 多线程事务隔离（每个线程独立的事务）：");

        Thread t1 = new Thread(() -> {
            TransactionManager.beginTransaction("数据源A");
            try {
                System.out.println("  [线程1] 执行操作，事务: " +
                        TransactionManager.getCurrentTransaction());
                Thread.sleep(100);
                TransactionManager.commit();
            } catch (InterruptedException e) {
                TransactionManager.rollback();
            }
        }, "事务线程1");

        Thread t2 = new Thread(() -> {
            TransactionManager.beginTransaction("数据源B");
            try {
                System.out.println("  [线程2] 执行操作，事务: " +
                        TransactionManager.getCurrentTransaction());
                Thread.sleep(50);
                TransactionManager.commit();
            } catch (InterruptedException e) {
                TransactionManager.rollback();
            }
        }, "事务线程2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println();
    }

    /**
     * 事务回滚场景
     */
    private static void transactionRollbackDemo() {
        System.out.println("3. 事务回滚演示：");
        TransactionManager.beginTransaction("主数据源");

        try {
            System.out.println("  执行操作1...");
            System.out.println("  执行操作2...");
            // 模拟异常
            throw new RuntimeException("模拟数据库约束冲突");
        } catch (Exception e) {
            System.out.println("  捕获异常: " + e.getMessage());
            TransactionManager.rollback();
        }

        // 验证事务已清理
        System.out.println("  事务是否活跃: " + TransactionManager.isTransactionActive());
        System.out.println("\n总结：");
        System.out.println("- ThreadLocal保证每个线程持有独立的事务上下文");
        System.out.println("- 同一线程内的Service调用可以共享同一个事务");
        System.out.println("- 不同线程的事务完全隔离，互不影响");
        System.out.println("- 务必在finally中清理ThreadLocal，防止内存泄漏");
    }
}
