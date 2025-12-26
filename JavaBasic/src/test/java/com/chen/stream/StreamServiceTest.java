package com.chen.stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StreamService 的单元测试用例
 */
@DisplayName("StreamService 功能测试")
public class StreamServiceTest {

    private StreamService streamService;
    private List<Person> personList;

    @BeforeEach
    public void setUp() {
        streamService = new StreamService();
        personList = Arrays.asList(
                new Person("Alice", 25, "New York"),
                new Person("Bob", 30, "London"),
                new Person("Charlie", 25, "New York"),
                new Person("David", 35, "London"),
                new Person("Eve", 28, "Paris")
        );
    }

    @Test
    @DisplayName("filterEvens: 过滤偶数")
    public void testFilterEvens() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> result = streamService.filterEvens(numbers);
        assertEquals(Arrays.asList(2, 4, 6), result);
    }

    @Test
    @DisplayName("toUpperCase: 转换为大写")
    public void testToUpperCase() {
        List<String> names = Arrays.asList("alice", "bob");
        List<String> result = streamService.toUpperCase(names);
        assertEquals(Arrays.asList("ALICE", "BOB"), result);
    }

    @Test
    @DisplayName("flattenList: 扁平化列表")
    public void testFlattenList() {
        List<List<String>> nested = Arrays.asList(
                Arrays.asList("a", "b"),
                Arrays.asList("c", "d")
        );
        List<String> result = streamService.flattenList(nested);
        assertEquals(Arrays.asList("a", "b", "c", "d"), result);
    }

    @Test
    @DisplayName("sortList: 列表排序")
    public void testSortList() {
        List<Integer> numbers = Arrays.asList(5, 2, 8);
        
        // 升序
        List<Integer> ascending = streamService.sortList(numbers, true);
        assertEquals(Arrays.asList(2, 5, 8), ascending);
        
        // 降序
        List<Integer> descending = streamService.sortList(numbers, false);
        assertEquals(Arrays.asList(8, 5, 2), descending);
    }

    @Test
    @DisplayName("getDistinctList: 去重")
    public void testGetDistinctList() {
        List<Integer> numbers = Arrays.asList(1, 2, 2, 3);
        List<Integer> result = streamService.getDistinctList(numbers);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    @DisplayName("getLimitSkip: 截断和跳过")
    public void testGetLimitSkip() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> result = streamService.getLimitSkip(numbers, 3, 5);
        // Skip 5 -> 6, 7, 8, 9, 10
        // Limit 3 -> 6, 7, 8
        assertEquals(Arrays.asList(6, 7, 8), result);
    }

    @Test
    @DisplayName("sum: 求和")
    public void testSum() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Integer result = streamService.sum(numbers);
        assertEquals(Integer.valueOf(15), result);
    }

    @Test
    @DisplayName("areAllEven: 检查是否全为偶数")
    public void testAreAllEven() {
        assertTrue(streamService.areAllEven(Arrays.asList(2, 4, 6)));
        assertFalse(streamService.areAllEven(Arrays.asList(2, 3, 6)));
    }

    @Test
    @DisplayName("findFirst: 查找第一个")
    public void testFindFirst() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        Optional<Integer> first = streamService.findFirst(numbers);
        assertTrue(first.isPresent());
        assertEquals(Integer.valueOf(1), first.get());
    }

    @Test
    @DisplayName("findAny: 查找任意一个")
    public void testFindAny() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        Optional<Integer> any = streamService.findAny(numbers);
        assertTrue(any.isPresent());
    }

    @Test
    @DisplayName("getNames: 获取名字列表")
    public void testGetNames() {
        List<String> names = streamService.getNames(personList);
        assertEquals(5, names.size());
        assertTrue(names.contains("Alice"));
        assertTrue(names.contains("David"));
    }

    @Test
    @DisplayName("getNamesAsLinkedList: 获取名字列表(LinkedList)")
    public void testGetNamesAsLinkedList() {
        LinkedList<String> names = streamService.getNamesAsLinkedList(personList);
        assertEquals(5, names.size());
        assertTrue(names instanceof LinkedList);
    }

    @Test
    @DisplayName("getCities: 获取城市集合")
    public void testGetCities() {
        Set<String> cities = streamService.getCities(personList);
        assertEquals(3, cities.size());
        assertTrue(cities.contains("New York"));
        assertTrue(cities.contains("London"));
        assertTrue(cities.contains("Paris"));
    }

    @Test
    @DisplayName("joinNames: 拼接名字")
    public void testJoinNames() {
        String result = streamService.joinNames(personList);
        assertEquals("Alice, Bob, Charlie, David, Eve", result);
    }

    @Test
    @DisplayName("groupByCity: 按城市分组")
    public void testGroupByCity() {
        Map<String, List<Person>> result = streamService.groupByCity(personList);
        assertEquals(2, result.get("New York").size());
        assertEquals(2, result.get("London").size());
        assertEquals(1, result.get("Paris").size());
    }

    @Test
    @DisplayName("getNamesByCity: 按城市分组获取名字")
    public void testGetNamesByCity() {
        Map<String, List<String>> result = streamService.getNamesByCity(personList);
        List<String> nyNames = result.get("New York");
        assertTrue(nyNames.contains("Alice"));
        assertTrue(nyNames.contains("Charlie"));
        assertFalse(nyNames.contains("Bob"));
    }

    @Test
    @DisplayName("partitionByAge: 按年龄分区")
    public void testPartitionByAge() {
        Map<Boolean, List<Person>> result = streamService.partitionByAge(personList, 30);
        
        // 大于 30 岁的 (David 35)
        assertEquals(1, result.get(true).size());
        assertEquals("David", result.get(true).get(0).getName());
        
        // 小于等于 30 岁的
        assertEquals(4, result.get(false).size());
    }

    @Test
    @DisplayName("getAgeStatistics: 年龄统计")
    public void testGetAgeStatistics() {
        IntSummaryStatistics stats = streamService.getAgeStatistics(personList);
        assertEquals(5, stats.getCount());
        assertEquals(25, stats.getMin());
        assertEquals(35, stats.getMax());
        assertEquals(143, stats.getSum());
    }

    @Test
    @DisplayName("getAverageAge: 平均年龄")
    public void testGetAverageAge() {
        double avg = streamService.getAverageAge(personList);
        assertEquals(28.6, avg, 0.01);
    }

    @Test
    @DisplayName("getWordFrequency: 词频统计")
    public void testGetWordFrequency() {
        String text = "Java is fun and Java is powerful";
        Map<String, Long> result = streamService.getWordFrequency(text);
        
        assertEquals(Long.valueOf(2), result.get("java"));
        assertEquals(Long.valueOf(2), result.get("is"));
        assertEquals(Long.valueOf(1), result.get("fun"));
    }

    @Test
    @DisplayName("countEvensParallel: 并行流计数")
    public void testCountEvensParallel() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            numbers.add(i);
        }
        long count = streamService.countEvensParallel(numbers);
        assertEquals(500, count);
    }
    
    @Test
    @DisplayName("generateEvens: 生成偶数序列")
    public void testGenerateEvens() {
        List<Integer> evens = streamService.generateEvens(5);
        assertEquals(Arrays.asList(0, 2, 4, 6, 8), evens);
    }

    @Test
    @DisplayName("debugStream: 调试流")
    public void testDebugStream() {
        List<String> input = Arrays.asList("one", "two", "three", "four");
        List<String> result = streamService.debugStream(input);
        assertEquals(Arrays.asList("THREE", "FOUR"), result);
    }
}
