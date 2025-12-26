package com.chen.stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Java 8 Parallel Stream 并行流测试用例
 */
@DisplayName("并行流 (Parallel Stream) 测试")
public class ParallelStreamTest {

    /**
     * 测试并行流的基本使用
     */
    @Test
    @DisplayName("并行流 vs 串行流性能对比")
    public void testParallelStream() {
        // 创建一个大的列表
        int size = 1000000;
        List<Integer> numbers = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            numbers.add(i);
        }

        // 串行流处理
        long startSerial = System.currentTimeMillis();
        long countSerial = numbers.stream()
                .filter(n -> n % 2 == 0)
                .count();
        long endSerial = System.currentTimeMillis();
        
        // 并行流处理
        long startParallel = System.currentTimeMillis();
        long countParallel = numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .count();
        long endParallel = System.currentTimeMillis();

        assertEquals(countSerial, countParallel);
        assertEquals(size / 2, countParallel);
        
        System.out.println("Serial time: " + (endSerial - startSerial) + "ms");
        System.out.println("Parallel time: " + (endParallel - startParallel) + "ms");
    }

    /**
     * 测试并行流的线程安全性问题 (副作用)
     */
    @Test
    @DisplayName("并行流的线程安全问题演示")
    public void testParallelStreamSideEffects() {
        // 这是一个反例，演示并行流修改共享变量可能导致的问题
        // 在实际开发中应避免这种写法
        List<Integer> numbers = IntStream.range(0, 1000).boxed().collect(Collectors.toList());
        List<Integer> serialResult = new ArrayList<>();
        
        // 串行流是安全的
        numbers.stream().forEach(serialResult::add);
        assertEquals(1000, serialResult.size());
        
        // 并行流直接操作非线程安全的集合是不安全的
        List<Integer> parallelResult = new ArrayList<>();
        // 使用同步列表或者并发集合可以解决，或者避免副作用
        List<Integer> synchronizedResult = Collections.synchronizedList(new ArrayList<>());
        
        numbers.parallelStream().forEach(synchronizedResult::add);
        
        assertEquals(1000, synchronizedResult.size());
    }
    
    /**
     * 测试并行排序
     */
    @Test
    @DisplayName("并行排序")
    public void testParallelSort() {
        int[] numbers = new int[10000];
        Random random = new Random();
        for(int i=0; i<numbers.length; i++) {
            numbers[i] = random.nextInt();
        }
        
        int[] numbersCopy = Arrays.copyOf(numbers, numbers.length);
        
        Arrays.parallelSort(numbers);
        Arrays.sort(numbersCopy);
        
        assertArrayEquals(numbersCopy, numbers);
    }
}
