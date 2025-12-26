package com.chen.stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Java 8 Stream 高级特性测试用例
 */
@DisplayName("Stream 高级特性测试")
public class StreamAdvancedTest {

    /**
     * 测试无限流: iterate 和 generate
     */
    @Test
    @DisplayName("无限流: iterate 和 generate")
    public void testInfiniteStreams() {
        // iterate: 生成偶数序列 0, 2, 4, 6, 8
        List<Integer> evens = Stream.iterate(0, n -> n + 2)
                .limit(5)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(0, 2, 4, 6, 8), evens);

        // generate: 生成随机数
        List<Double> randoms = Stream.generate(Math::random)
                .limit(3)
                .collect(Collectors.toList());
        assertEquals(3, randoms.size());
    }

    /**
     * 测试数值流: IntStream, LongStream, DoubleStream
     * 避免装箱拆箱开销
     */
    @Test
    @DisplayName("数值流: IntStream")
    public void testPrimitiveStreams() {
        // range: [1, 5) -> 1, 2, 3, 4
        int sum = IntStream.range(1, 5).sum();
        assertEquals(10, sum);

        // rangeClosed: [1, 5] -> 1, 2, 3, 4, 5
        int sumClosed = IntStream.rangeClosed(1, 5).sum();
        assertEquals(15, sumClosed);
        
        // 统计操作
        IntSummaryStatistics stats = IntStream.rangeClosed(1, 5).summaryStatistics();
        assertEquals(15, stats.getSum());
        assertEquals(3.0, stats.getAverage(), 0.001);
    }

    /**
     * 测试流的构建方式
     */
    @Test
    @DisplayName("流的构建方式")
    public void testStreamConstruction() {
        // Stream.of
        Stream<String> stream = Stream.of("Java", "Python", "C++");
        assertEquals(3, stream.count());

        // Arrays.stream
        int[] numbers = {1, 2, 3};
        int sum = Arrays.stream(numbers).sum();
        assertEquals(6, sum);
        
        // Stream.builder
        Stream<Object> builtStream = Stream.builder()
                .add("One")
                .add("Two")
                .build();
        assertEquals(2, builtStream.count());
    }
    
    /**
     * 复杂场景：找出一段文本中出现频率最高的单词
     */
    @Test
    @DisplayName("复杂场景: 词频统计")
    public void testWordFrequency() {
        String text = "Java is fun and Java is powerful stream API is part of Java";
        
        Map<String, Long> frequency = Arrays.stream(text.split("\\s+"))
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        assertEquals(3L, frequency.get("java").longValue());
        // "is" 出现了3次: "Java is fun", "Java is powerful", "API is part"
        assertEquals(3L, frequency.get("is").longValue());
        assertEquals(1L, frequency.get("fun").longValue());
        
        // 找出频率最高的
        // 注意：如果有多个频率相同的最高词，max 可能会返回其中任意一个。
        // 在这个例子中，java 和 is 都是 3 次。
        // 为了测试稳定性，我们可以检查 max 的值是否为 3
        Optional<Map.Entry<String, Long>> mostFrequent = frequency.entrySet().stream()
                .max(Map.Entry.comparingByValue());
        
        assertTrue(mostFrequent.isPresent());
        assertEquals(3L, mostFrequent.get().getValue().longValue());
        // 这里的 key 可能是 java 也可能是 is，取决于具体的实现和顺序，所以断言 key 可能会不稳定
        // 但通常来说，我们期望它是 java 或者 is
        String mostFrequentWord = mostFrequent.get().getKey();
        assertTrue(mostFrequentWord.equals("java") || mostFrequentWord.equals("is"));
    }
    
    /**
     * 测试 peek: 调试流
     */
    @Test
    @DisplayName("peek: 调试流")
    public void testPeek() {
        List<String> result = Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());
        
        assertEquals(Arrays.asList("THREE", "FOUR"), result);
    }
}
