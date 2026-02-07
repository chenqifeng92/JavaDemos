package com.chen.threadlocal;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;

/**
 * 请求ID追踪演示
 * 模拟分布式系统中的请求链路追踪（类似MDC、Sleuth、Zipkin等的基本原理）。
 * 使用ThreadLocal在整个请求处理链路中传递TraceId，方便日志追踪和问题排查。
 */
public class RequestIdTrackerDemo {

    /**
     * 追踪上下文
     */
    static class TraceContext {
        private String traceId;    // 全链路追踪ID
        private String spanId;     // 当前跨度ID
        private String parentSpanId; // 父跨度ID
        private long startTime;

        public TraceContext(String traceId, String spanId, String parentSpanId) {
            this.traceId = traceId;
            this.spanId = spanId;
            this.parentSpanId = parentSpanId;
            this.startTime = System.currentTimeMillis();
        }

        public String getTraceId() { return traceId; }
        public String getSpanId() { return spanId; }
        public String getParentSpanId() { return parentSpanId; }
        public long getStartTime() { return startTime; }

        @Override
        public String toString() {
            return "[traceId=" + traceId + ", spanId=" + spanId + "]";
        }
    }

    /**
     * 追踪上下文持有者（类似MDC - Mapped Diagnostic Context）
     */
    static class TraceContextHolder {
        private static final ThreadLocal<TraceContext> contextHolder = new ThreadLocal<>();

        public static void setContext(TraceContext context) {
            contextHolder.set(context);
        }

        public static TraceContext getContext() {
            return contextHolder.get();
        }

        public static String getTraceId() {
            TraceContext ctx = contextHolder.get();
            return ctx != null ? ctx.getTraceId() : "N/A";
        }

        public static void clear() {
            contextHolder.remove();
        }

        /**
         * 创建新的追踪上下文（请求入口处调用）
         */
        public static TraceContext createNewTrace() {
            String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            String spanId = generateSpanId();
            TraceContext context = new TraceContext(traceId, spanId, null);
            contextHolder.set(context);
            return context;
        }

        /**
         * 创建子Span（调用下游服务时使用）
         */
        public static TraceContext createChildSpan() {
            TraceContext parent = contextHolder.get();
            if (parent == null) {
                return createNewTrace();
            }
            String newSpanId = generateSpanId();
            return new TraceContext(parent.getTraceId(), newSpanId, parent.getSpanId());
        }

        private static String generateSpanId() {
            return UUID.randomUUID().toString().substring(0, 8);
        }
    }

    /**
     * 模拟日志工具 - 自动从ThreadLocal获取traceId
     */
    static class TraceLogger {
        public static void info(String component, String message) {
            String traceId = TraceContextHolder.getTraceId();
            System.out.printf("  [%s] [traceId=%s] [%s] %s%n",
                    Thread.currentThread().getName(), traceId, component, message);
        }
    }

    // ========== 模拟请求处理链路 ==========

    /**
     * 模拟网关/过滤器 - 请求入口，创建TraceId
     */
    static class GatewayFilter {
        private ApiController controller = new ApiController();

        public void doFilter(String requestPath) {
            // 创建追踪上下文
            TraceContext context = TraceContextHolder.createNewTrace();
            TraceLogger.info("Gateway", "收到请求: " + requestPath + "，分配追踪ID: " + context);

            try {
                controller.handleRequest(requestPath);
            } finally {
                long duration = System.currentTimeMillis() - context.getStartTime();
                TraceLogger.info("Gateway", "请求处理完成，耗时: " + duration + "ms");
                // 清理ThreadLocal
                TraceContextHolder.clear();
            }
        }
    }

    /**
     * 模拟Controller层
     */
    static class ApiController {
        private BusinessService businessService = new BusinessService();

        public void handleRequest(String path) {
            TraceLogger.info("Controller", "处理路由: " + path);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            businessService.process(path);
        }
    }

    /**
     * 模拟Service层
     */
    static class BusinessService {
        private DataAccessLayer dal = new DataAccessLayer();
        private ExternalApiClient apiClient = new ExternalApiClient();

        public void process(String path) {
            TraceLogger.info("Service", "执行业务逻辑");

            // 调用数据访问层
            dal.queryData("SELECT * FROM users");

            // 调用外部API
            apiClient.callRemoteService("http://api.example.com/data");

            TraceLogger.info("Service", "业务逻辑处理完成");
        }
    }

    /**
     * 模拟数据访问层
     */
    static class DataAccessLayer {
        public void queryData(String sql) {
            TraceLogger.info("DAO", "执行SQL: " + sql);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            TraceLogger.info("DAO", "查询完成，返回5条记录");
        }
    }

    /**
     * 模拟外部API调用
     */
    static class ExternalApiClient {
        public void callRemoteService(String url) {
            // 调用外部服务时，创建子Span
            TraceContext childSpan = TraceContextHolder.createChildSpan();
            TraceLogger.info("HttpClient", "调用远程服务: " + url + "，子Span: " + childSpan.getSpanId());

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            TraceLogger.info("HttpClient", "远程服务响应成功");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 请求ID追踪演示 ===\n");

        // 演示1：单个请求的全链路追踪
        singleRequestDemo();

        // 演示2：多个并发请求，各自有独立的TraceId
        concurrentRequestsDemo();
    }

    /**
     * 单请求链路追踪演示
     */
    private static void singleRequestDemo() {
        System.out.println("1. 单请求全链路追踪：");
        GatewayFilter gateway = new GatewayFilter();
        gateway.doFilter("/api/users/list");
        System.out.println();
    }

    /**
     * 并发请求追踪演示
     */
    private static void concurrentRequestsDemo() throws InterruptedException {
        System.out.println("2. 多个并发请求（各自独立TraceId）：");

        GatewayFilter gateway = new GatewayFilter();
        String[] paths = {"/api/users/create", "/api/orders/list", "/api/products/search"};
        CountDownLatch latch = new CountDownLatch(paths.length);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (String path : paths) {
            executor.submit(() -> {
                try {
                    gateway.doFilter(path);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        System.out.println("\n总结：");
        System.out.println("- TraceId通过ThreadLocal在整个请求链路中自动传播");
        System.out.println("- 无需在每个方法参数中显式传递TraceId");
        System.out.println("- 不同请求（不同线程）的TraceId互不干扰");
        System.out.println("- 这是MDC、Sleuth等链路追踪框架的基本原理");
    }
}
