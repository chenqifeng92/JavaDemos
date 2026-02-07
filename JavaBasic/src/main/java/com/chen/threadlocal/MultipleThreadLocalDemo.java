package com.chen.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多个ThreadLocal变量管理演示
 *
 * 在实际项目中，一个线程可能需要携带多种上下文信息：
 * 用户信息、请求信息、日志上下文、国际化信息等。
 * 本类展示如何优雅地管理多个ThreadLocal变量。
 */
public class MultipleThreadLocalDemo {

    // ========== 方式1：独立的ThreadLocal变量 ==========

    private static final ThreadLocal<String> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> requestId = new ThreadLocal<>();
    private static final ThreadLocal<String> locale = ThreadLocal.withInitial(() -> "zh_CN");
    private static final ThreadLocal<SimpleDateFormat> dateFormatter =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    // ========== 方式2：使用一个ThreadLocal存储Map（容器模式） ==========

    private static final ThreadLocal<Map<String, Object>> contextMap =
            ThreadLocal.withInitial(HashMap::new);

    // ========== 方式3：使用一个ThreadLocal存储上下文对象 ==========

    static class RequestContext {
        private String userId;
        private String requestId;
        private String locale;
        private long startTime;
        private Map<String, String> headers;

        public RequestContext() {
            this.headers = new HashMap<>();
            this.startTime = System.currentTimeMillis();
        }

        // Getter和Setter
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
        public String getLocale() { return locale; }
        public void setLocale(String locale) { this.locale = locale; }
        public long getStartTime() { return startTime; }
        public Map<String, String> getHeaders() { return headers; }
        public void addHeader(String key, String value) { this.headers.put(key, value); }

        @Override
        public String toString() {
            return "RequestContext{userId='" + userId + "', requestId='" + requestId +
                    "', locale='" + locale + "', headers=" + headers + '}';
        }
    }

    private static final ThreadLocal<RequestContext> requestContext = new ThreadLocal<>();

    /**
     * 上下文管理器 - 提供统一的上下文操作API
     */
    static class ContextManager {
        /**
         * 方式1：初始化多个独立的ThreadLocal
         */
        public static void initSeparateContexts(String uid, String reqId, String loc) {
            userId.set(uid);
            requestId.set(reqId);
            locale.set(loc);
        }

        /**
         * 方式1：清理多个独立的ThreadLocal
         */
        public static void clearSeparateContexts() {
            userId.remove();
            requestId.remove();
            locale.remove();
            dateFormatter.remove();
        }

        /**
         * 方式2：初始化Map容器上下文
         */
        public static void initMapContext(String uid, String reqId) {
            Map<String, Object> map = contextMap.get();
            map.put("userId", uid);
            map.put("requestId", reqId);
            map.put("timestamp", System.currentTimeMillis());
        }

        /**
         * 方式2：从Map容器获取值
         */
        @SuppressWarnings("unchecked")
        public static <T> T getFromMap(String key) {
            return (T) contextMap.get().get(key);
        }

        /**
         * 方式2：清理Map容器
         */
        public static void clearMapContext() {
            contextMap.remove();
        }

        /**
         * 方式3：初始化对象上下文
         */
        public static void initObjectContext(String uid, String reqId, String loc) {
            RequestContext ctx = new RequestContext();
            ctx.setUserId(uid);
            ctx.setRequestId(reqId);
            ctx.setLocale(loc);
            requestContext.set(ctx);
        }

        /**
         * 方式3：获取对象上下文
         */
        public static RequestContext getRequestContext() {
            return requestContext.get();
        }

        /**
         * 方式3：清理对象上下文
         */
        public static void clearObjectContext() {
            requestContext.remove();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 多个ThreadLocal变量管理演示 ===\n");

        // 演示1：多个独立ThreadLocal
        separateThreadLocalsDemo();

        // 演示2：Map容器模式
        mapContainerDemo();

        // 演示3：上下文对象模式
        contextObjectDemo();

        // 演示4：并发场景对比
        concurrentDemo();

        // 演示5：各方案对比总结
        comparisonSummary();
    }

    /**
     * 方式1：多个独立ThreadLocal变量
     */
    private static void separateThreadLocalsDemo() {
        System.out.println("1. 多个独立ThreadLocal变量：");

        try {
            ContextManager.initSeparateContexts("user_001", "req_abc123", "en_US");

            String uid = userId.get();
            String reqId = requestId.get();
            String loc = locale.get();
            String formattedDate = dateFormatter.get().format(new Date());

            System.out.println("  userId=" + uid);
            System.out.println("  requestId=" + reqId);
            System.out.println("  locale=" + loc);
            System.out.println("  当前时间=" + formattedDate);
        } finally {
            ContextManager.clearSeparateContexts();
            System.out.println("  已清理所有独立ThreadLocal\n");
        }
    }

    /**
     * 方式2：Map容器模式
     */
    private static void mapContainerDemo() {
        System.out.println("2. Map容器模式：");

        try {
            ContextManager.initMapContext("user_002", "req_def456");

            String uid = ContextManager.getFromMap("userId");
            String reqId = ContextManager.getFromMap("requestId");
            Long timestamp = ContextManager.getFromMap("timestamp");

            System.out.println("  userId=" + uid);
            System.out.println("  requestId=" + reqId);
            System.out.println("  timestamp=" + timestamp);

            // 可以动态添加新字段
            contextMap.get().put("extraInfo", "动态添加的信息");
            System.out.println("  extraInfo=" + ContextManager.getFromMap("extraInfo"));
        } finally {
            ContextManager.clearMapContext();
            System.out.println("  已清理Map容器\n");
        }
    }

    /**
     * 方式3：上下文对象模式
     */
    private static void contextObjectDemo() {
        System.out.println("3. 上下文对象模式（推荐）：");

        try {
            ContextManager.initObjectContext("user_003", "req_ghi789", "ja_JP");

            RequestContext ctx = ContextManager.getRequestContext();
            ctx.addHeader("Content-Type", "application/json");
            ctx.addHeader("Authorization", "Bearer token123");

            System.out.println("  完整上下文: " + ctx);
            System.out.println("  耗时: " + (System.currentTimeMillis() - ctx.getStartTime()) + "ms");
        } finally {
            ContextManager.clearObjectContext();
            System.out.println("  已清理上下文对象\n");
        }
    }

    /**
     * 并发场景演示
     */
    private static void concurrentDemo() throws InterruptedException {
        System.out.println("4. 并发场景下的多ThreadLocal管理：");

        ExecutorService executor = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);

        String[] users = {"Alice", "Bob", "Charlie"};
        String[] locales = {"zh_CN", "en_US", "ja_JP"};

        for (int i = 0; i < 3; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    // 初始化上下文
                    ContextManager.initObjectContext(
                            users[index], "req_" + index, locales[index]);

                    RequestContext ctx = ContextManager.getRequestContext();
                    System.out.println("  [" + Thread.currentThread().getName() + "] " + ctx);

                    Thread.sleep(50);

                    // 验证隔离性
                    RequestContext verify = ContextManager.getRequestContext();
                    System.out.println("  [" + Thread.currentThread().getName() +
                            "] 验证userId=" + verify.getUserId());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    ContextManager.clearObjectContext();
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        System.out.println();
    }

    /**
     * 方案对比总结
     */
    private static void comparisonSummary() {
        System.out.println("5. 三种方案对比：\n");

        System.out.println("  方式1 - 多个独立ThreadLocal：");
        System.out.println("    优点：类型安全，编译期检查");
        System.out.println("    缺点：变量多时管理麻烦，容易遗漏清理\n");

        System.out.println("  方式2 - Map容器模式：");
        System.out.println("    优点：灵活，可动态添加字段，只需清理一个ThreadLocal");
        System.out.println("    缺点：类型不安全，需要强制转型，key可能拼写错误\n");

        System.out.println("  方式3 - 上下文对象模式（推荐）：");
        System.out.println("    优点：类型安全，只需管理一个ThreadLocal，结构清晰");
        System.out.println("    缺点：需要定义上下文类，字段变更需要修改类\n");

        System.out.println("  建议：优先选择方式3（上下文对象模式），");
        System.out.println("  在需要高度灵活性时选择方式2（Map容器模式）。");
    }
}
