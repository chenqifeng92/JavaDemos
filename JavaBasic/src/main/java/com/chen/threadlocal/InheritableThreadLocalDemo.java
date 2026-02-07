package com.chen.threadlocal;

/**
 * InheritableThreadLocal演示
 * InheritableThreadLocal继承自ThreadLocal，它允许子线程继承父线程的ThreadLocal变量值。
 * 适用场景：需要在父子线程间传递上下文信息，如用户身份信息、请求追踪ID等。
 */
public class InheritableThreadLocalDemo {

    // 普通ThreadLocal：子线程无法继承父线程的值
    private static ThreadLocal<String> normalThreadLocal = new ThreadLocal<>();
    
    // InheritableThreadLocal：子线程可以继承父线程的值
    private static InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
    
    // 带有初始值的InheritableThreadLocal
    private static InheritableThreadLocal<Integer> inheritableWithInitial = new InheritableThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
        
        @Override
        protected Integer childValue(Integer parentValue) {
            // 可以在继承时对父线程的值进行处理
            return parentValue + 100;
        }
    };
    
    // 复杂对象的继承
    private static InheritableThreadLocal<UserContext> userContextThreadLocal = new InheritableThreadLocal<UserContext>() {
        @Override
        protected UserContext childValue(UserContext parentValue) {
            // 创建副本，避免父子线程共享同一个对象
            return new UserContext(parentValue.getUserId(), parentValue.getUserName(), "子线程-" + parentValue.getRole());
        }
    };

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== InheritableThreadLocal演示 ===\n");
        
        // 演示1：基本继承功能对比
        basicInheritanceDemo();
        
        // 演示2：childValue方法的使用
        childValueDemo();
        
        // 演示3：复杂对象的继承
        complexObjectInheritanceDemo();
        
        // 演示4：继承的时机和限制
        inheritanceTimingDemo();
    }
    
    /**
     * 演示ThreadLocal和InheritableThreadLocal的区别
     */
    private static void basicInheritanceDemo() throws InterruptedException {
        System.out.println("1. ThreadLocal vs InheritableThreadLocal对比：");
        
        // 父线程设置值
        normalThreadLocal.set("父线程的普通ThreadLocal值");
        inheritableThreadLocal.set("父线程的InheritableThreadLocal值");
        
        System.out.println("父线程设置的值：");
        System.out.println("  普通ThreadLocal: " + normalThreadLocal.get());
        System.out.println("  InheritableThreadLocal: " + inheritableThreadLocal.get());
        
        // 创建子线程
        Thread childThread = new Thread(() -> {
            System.out.println("\n子线程获取的值：");
            System.out.println("  普通ThreadLocal: " + normalThreadLocal.get()); // null
            System.out.println("  InheritableThreadLocal: " + inheritableThreadLocal.get()); // 继承父线程的值
            
            // 子线程修改值
            inheritableThreadLocal.set("子线程修改的值");
            System.out.println("\n子线程修改后：");
            System.out.println("  InheritableThreadLocal: " + inheritableThreadLocal.get());
        }, "子线程1");
        
        childThread.start();
        childThread.join();
        
        // 父线程再次读取
        System.out.println("\n父线程再次读取（不受子线程影响）：");
        System.out.println("  InheritableThreadLocal: " + inheritableThreadLocal.get());
        
        // 清理
        normalThreadLocal.remove();
        inheritableThreadLocal.remove();
        System.out.println();
    }
    
    /**
     * 演示childValue方法的使用
     */
    private static void childValueDemo() throws InterruptedException {
        System.out.println("2. childValue方法演示：");
        
        // 父线程设置值
        inheritableWithInitial.set(50);
        System.out.println("父线程设置的值: " + inheritableWithInitial.get());
        
        // 创建多个子线程
        Thread child1 = new Thread(() -> {
            // childValue会在继承时对值进行处理（+100）
            System.out.println(Thread.currentThread().getName() + " 继承并处理后的值: " + 
                             inheritableWithInitial.get());
            
            // 子线程可以继续修改
            inheritableWithInitial.set(inheritableWithInitial.get() + 10);
            System.out.println(Thread.currentThread().getName() + " 修改后: " + 
                             inheritableWithInitial.get());
        }, "子线程-A");
        
        Thread child2 = new Thread(() -> {
            // 每个子线程都会独立调用childValue
            System.out.println(Thread.currentThread().getName() + " 继承并处理后的值: " + 
                             inheritableWithInitial.get());
        }, "子线程-B");
        
        child1.start();
        child2.start();
        child1.join();
        child2.join();
        
        // 清理
        inheritableWithInitial.remove();
        System.out.println();
    }
    
    /**
     * 演示复杂对象的继承
     */
    private static void complexObjectInheritanceDemo() throws InterruptedException {
        System.out.println("3. 复杂对象继承演示：");
        
        // 父线程设置用户上下文
        UserContext parentContext = new UserContext(1001, "张三", "管理员");
        userContextThreadLocal.set(parentContext);
        System.out.println("父线程的用户上下文: " + userContextThreadLocal.get());
        
        // 创建子线程处理任务
        Thread taskThread = new Thread(() -> {
            UserContext childContext = userContextThreadLocal.get();
            System.out.println("子线程继承的用户上下文: " + childContext);
            
            // 子线程修改自己的副本，不影响父线程
            childContext.setRole("普通用户");
            System.out.println("子线程修改后: " + childContext);
            
            // 创建孙线程
            Thread grandChildThread = new Thread(() -> {
                UserContext grandChildContext = userContextThreadLocal.get();
                System.out.println("孙线程继承的用户上下文: " + grandChildContext);
            }, "孙线程");
            
            grandChildThread.start();
            try {
                grandChildThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "任务处理线程");
        
        taskThread.start();
        taskThread.join();
        
        // 父线程的值不受影响
        System.out.println("父线程最终的用户上下文: " + userContextThreadLocal.get());
        
        // 清理
        userContextThreadLocal.remove();
        System.out.println();
    }
    
    /**
     * 演示继承的时机和限制
     */
    private static void inheritanceTimingDemo() throws InterruptedException {
        System.out.println("4. 继承时机演示：");
        
        inheritableThreadLocal.set("初始值");
        System.out.println("父线程初始值: " + inheritableThreadLocal.get());
        
        // 在创建子线程前修改值
        Thread child1 = new Thread(() -> {
            System.out.println("子线程1继承的值: " + inheritableThreadLocal.get());
        }, "子线程1-创建前");
        
        // 修改父线程的值
        inheritableThreadLocal.set("修改后的值");
        System.out.println("父线程修改为: " + inheritableThreadLocal.get());
        
        // 创建第二个子线程
        Thread child2 = new Thread(() -> {
            System.out.println("子线程2继承的值: " + inheritableThreadLocal.get());
        }, "子线程2-创建后");
        
        // 启动线程
        child1.start();
        child2.start();
        child1.join();
        child2.join();
        
        // 再次修改父线程的值
        inheritableThreadLocal.set("再次修改的值");
        System.out.println("\n父线程再次修改为: " + inheritableThreadLocal.get());
        
        // 之前创建的子线程不会受到影响
        Thread child3 = new Thread(() -> {
            System.out.println("子线程3（新创建）继承的值: " + inheritableThreadLocal.get());
        }, "子线程3");
        
        child3.start();
        child3.join();
        
        // 清理
        inheritableThreadLocal.remove();
        
        System.out.println("\n重要说明：子线程继承父线程的ThreadLocal值发生在子线程创建时，");
        System.out.println("而不是子线程启动时。创建后父线程的修改不会影响已创建的子线程。");
    }
    
    /**
     * 用户上下文类
     */
    static class UserContext {
        private int userId;
        private String userName;
        private String role;
        
        public UserContext(int userId, String userName, String role) {
            this.userId = userId;
            this.userName = userName;
            this.role = role;
        }
        
        public int getUserId() {
            return userId;
        }
        
        public void setUserId(int userId) {
            this.userId = userId;
        }
        
        public String getUserName() {
            return userName;
        }
        
        public void setUserName(String userName) {
            this.userName = userName;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
        @Override
        public String toString() {
            return "UserContext{" +
                    "userId=" + userId +
                    ", userName='" + userName + '\'' +
                    ", role='" + role + '\'' +
                    '}';
        }
    }
}