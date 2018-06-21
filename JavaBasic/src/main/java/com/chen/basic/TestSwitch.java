package com.chen.basic;

/**
 * 1.从switch语句开始，看switch后面括号内的表达式，然后进行下一步。
 * 2.找到case里面那个取值与表达式满足，就执行哪一个case，然后break跳出程序。如果所有的case都不满足表达式，那么就运行下一步。
 * 3.当case 都不满足时，执行default语句，输出结果。
 */
public class TestSwitch {
    public static void main(String args[]) {
        char c = 'E';        //char类型字符
        switch (c) {
            default:
                System.out.println("打印默认值");
                //break;
            case 'a':
                System.out.println("a");
                //break;
            case 'b':
                System.out.println('b');
                //break;
            case 'c':
                System.out.println('c');
                //break;
            case 'd':
                System.out.println("d");
                //break;

        }
    }
/**
 * 4.在java中如果switch的case语句中少写了break;这个关键字，在编译的时候并没有报错
 * 5.但是在执行的时候会一直执行所有case条件下的语句并不是去判断，所以会一直执行直到遇到break关键字跳出或者一直执行到defaut语句。
 * 6.在java中switch后的表达式的类型只能为以下几种：byte、short、char、int（Java1.6），在java1.7后支持了对string的判断。
 */
}