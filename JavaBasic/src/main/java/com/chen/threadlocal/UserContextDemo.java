package com.chen.threadlocal;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 用户上下文管理演示
 * 模拟Web应用中的用户信息在不同层之间的传递
 * 在实际应用中，ThreadLocal常用于存储用户认证信息、请求上下文等
 */
public class UserContextDemo {

    /**
     * 用户上下文持有者
     * 模拟Spring Security的SecurityContextHolder
     */
    static class UserContextHolder {
        private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();
        
        public static void setContext(UserContext context) {
            userContext.set(context);
        }
        
        public static UserContext getContext() {
            return userContext.get();
        }
        
        public static void clearContext() {
            userContext.remove();
        }
        
        /**
         * 获取当前用户ID
         */
        public static Long getCurrentUserId() {
            UserContext context = getContext();
            return context != null ? context.getUserId() : null;
        }
        
        /**
         * 获取当前用户名
         */
        public static String getCurrentUsername() {
            UserContext context = getContext();
            return context != null ? context.getUsername() : null;
        }
    }
    
    /**
     * 用户上下文信息
     */
    static class UserContext {
        private Long userId;
        private String username;
        private String role;
        private String token;
        
        public UserContext(Long userId, String username, String role, String token) {
            this.userId = userId;
            this.username = username;
            this.role = role;
            this.token = token;
        }
        
        // Getter和Setter方法
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        
        @Override
        public String toString() {
            return "UserContext{" +
                    "userId=" + userId +
                    ", username='" + username + '\'' +
                    ", role='" + role + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }
    
    /**
     * 模拟Controller层
     */
    static class UserController {
        private UserService userService = new UserService();
        
        public void handleRequest(String username, String token) {
            System.out.println("[Controller] 处理用户请求: " + username);
            
            // 模拟认证过程，设置用户上下文
            UserContext context = authenticateUser(username, token);
            UserContextHolder.setContext(context);
            
            try {
                // 调用Service层
                userService.performUserAction();
            } finally {
                // 重要：请求处理完成后清理ThreadLocal
                UserContextHolder.clearContext();
                System.out.println("[Controller] 清理用户上下文\n");
            }
        }
        
        private UserContext authenticateUser(String username, String token) {
            // 模拟认证过程
            Long userId = Math.abs(new Random().nextLong() % 10000);
            String role = username.contains("admin") ? "ADMIN" : "USER";
            return new UserContext(userId, username, role, token);
        }
    }
    
    /**
     * 模拟Service层
     */
    static class UserService {
        private UserDao userDao = new UserDao();
        private AuditService auditService = new AuditService();
        
        public void performUserAction() {
            // 从ThreadLocal获取当前用户信息
            UserContext context = UserContextHolder.getContext();
            System.out.println("[Service] 当前用户: " + context.getUsername() + 
                             ", 角色: " + context.getRole());
            
            // 执行业务逻辑
            if ("ADMIN".equals(context.getRole())) {
                System.out.println("[Service] 执行管理员操作");
                userDao.adminOperation();
            } else {
                System.out.println("[Service] 执行普通用户操作");
                userDao.userOperation();
            }
            
            // 记录审计日志
            auditService.logUserAction("用户操作");
        }
    }
    
    /**
     * 模拟DAO层
     */
    static class UserDao {
        public void userOperation() {
            // 在DAO层也能获取到用户信息
            Long userId = UserContextHolder.getCurrentUserId();
            String username = UserContextHolder.getCurrentUsername();
            System.out.println("[DAO] 为用户 " + username + "(ID: " + userId + ") 执行数据库操作");
        }
        
        public void adminOperation() {
            Long userId = UserContextHolder.getCurrentUserId();
            System.out.println("[DAO] 为管理员(ID: " + userId + ") 执行特权数据库操作");
        }
    }
    
    /**
     * 审计服务
     */
    static class AuditService {
        public void logUserAction(String action) {
            UserContext context = UserContextHolder.getContext();
            if (context != null) {
                System.out.println("[Audit] 记录审计日志 - 用户: " + context.getUsername() + 
                                 ", 操作: " + action + ", 时间: " + System.currentTimeMillis());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 用户上下文管理演示 ===\n");
        
        // 演示1：单线程场景
        singleThreadDemo();
        
        // 演示2：多线程并发场景
        multiThreadDemo();
        
        // 演示3：线程池场景的注意事项
        threadPoolCautionDemo();
    }
    
    /**
     * 单线程场景演示
     */
    private static void singleThreadDemo() {
        System.out.println("1. 单线程场景演示：");
        
        UserController controller = new UserController();
        
        // 模拟处理用户请求
        controller.handleRequest("user001", "token_123");
        controller.handleRequest("admin001", "token_456");
    }
    
    /**
     * 多线程并发场景演示
     */
    private static void multiThreadDemo() throws InterruptedException {
        System.out.println("2. 多线程并发场景演示：");
        
        UserController controller = new UserController();
        CountDownLatch latch = new CountDownLatch(5);
        
        // 模拟多个用户并发请求
        for (int i = 1; i <= 5; i++) {
            final int userId = i;
            Thread thread = new Thread(() -> {
                String username = (userId % 2 == 0) ? "admin" + userId : "user" + userId;
                controller.handleRequest(username, "token_" + userId);
                latch.countDown();
            }, "请求线程-" + i);
            thread.start();
            
            // 稍微错开启动时间
            Thread.sleep(100);
        }
        
        latch.await();
        System.out.println("所有请求处理完成\n");
    }
    
    /**
     * 线程池场景的注意事项演示
     */
    private static void threadPoolCautionDemo() {
        System.out.println("3. 线程池场景注意事项：");
        System.out.println("在线程池中使用ThreadLocal需要特别注意：");
        System.out.println("- 线程池中的线程会被复用");
        System.out.println("- 必须在任务开始时设置ThreadLocal");
        System.out.println("- 必须在任务结束时清理ThreadLocal");
        System.out.println("- 否则可能导致数据混乱或内存泄漏");
        System.out.println("\n示例代码结构：");
        System.out.println("try {");
        System.out.println("    UserContextHolder.setContext(context);");
        System.out.println("    // 执行业务逻辑");
        System.out.println("} finally {");
        System.out.println("    UserContextHolder.clearContext(); // 必须清理！");
        System.out.println("}");
    }
}