package com.chen.stream;

import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * StreamService 的单元测试用例
 */
public class StreamServiceTest {

    private StreamService streamService;
    private List<Person> personList;

    @Before
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
    public void testFilterEvens() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> result = streamService.filterEvens(numbers);
        assertEquals(Arrays.asList(2, 4, 6), result);
    }

    @Test
    public void testToUpperCase() {
        List<String> names = Arrays.asList("alice", "bob");
        List<String> result = streamService.toUpperCase(names);
        assertEquals(Arrays.asList("ALICE", "BOB"), result);
    }

    @Test
    public void testFlattenList() {
        List<List<String>> nested = Arrays.asList(
                Arrays.asList("a", "b"),
                Arrays.asList("c", "d")
        );
        List<String> result = streamService.flattenList(nested);
        assertEquals(Arrays.asList("a", "b", "c", "d"), result);
    }

    @Test
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
    public void testGetDistinctList() {
        List<Integer> numbers = Arrays.asList(1, 2, 2, 3);
        List<Integer> result = streamService.getDistinctList(numbers);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testGetLimitSkip() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> result = streamService.getLimitSkip(numbers, 3, 5);
        // Skip 5 -> 6, 7, 8, 9, 10
        // Limit 3 -> 6, 7, 8
        assertEquals(Arrays.asList(6, 7, 8), result);
    }

    @Test
    public void testSum() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Integer result = streamService.sum(numbers);
        assertEquals(Integer.valueOf(15), result);
    }

    @Test
    public void testAreAllEven() {
        assertTrue(streamService.areAllEven(Arrays.asList(2, 4, 6)));
        assertFalse(streamService.areAllEven(Arrays.asList(2, 3, 6)));
    }

    @Test
    public void testFindFirst() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        Optional<Integer> first = streamService.findFirst(numbers);
        assertTrue(first.isPresent());
        assertEquals(Integer.valueOf(1), first.get());
    }

    @Test
    public void testFindAny() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        Optional<Integer> any = streamService.findAny(numbers);
        assertTrue(any.isPresent());
    }

    @Test
    public void testGetNames() {
        List<String> names = streamService.getNames(personList);
        assertEquals(5, names.size());
        assertTrue(names.contains("Alice"));
        assertTrue(names.contains("David"));
    }

    @Test
    public void testGetNamesAsLinkedList() {
        LinkedList<String> names = streamService.getNamesAsLinkedList(personList);
        assertEquals(5, names.size());
        assertTrue(names instanceof LinkedList);
    }

    @Test
    public void testGetCities() {
        Set<String> cities = streamService.getCities(personList);
        assertEquals(3, cities.size());
        assertTrue(cities.contains("New York"));
        assertTrue(cities.contains("London"));
        assertTrue(cities.contains("Paris"));
    }

    @Test
    public void testJoinNames() {
        String result = streamService.joinNames(personList);
        assertEquals("Alice, Bob, Charlie, David, Eve", result);
    }

    @Test
    public void testGroupByCity() {
        Map<String, List<Person>> result = streamService.groupByCity(personList);
        assertEquals(2, result.get("New York").size());
        assertEquals(2, result.get("London").size());
        assertEquals(1, result.get("Paris").size());
    }

    @Test
    public void testGetNamesByCity() {
        Map<String, List<String>> result = streamService.getNamesByCity(personList);
        List<String> nyNames = result.get("New York");
        assertTrue(nyNames.contains("Alice"));
        assertTrue(nyNames.contains("Charlie"));
        assertFalse(nyNames.contains("Bob"));
    }

    @Test
    public void testPartitionByAge() {
        Map<Boolean, List<Person>> result = streamService.partitionByAge(personList, 30);
        
        // 大于 30 岁的 (David 35)
        assertEquals(1, result.get(true).size());
        assertEquals("David", result.get(true).get(0).getName());
        
        // 小于等于 30 岁的
        assertEquals(4, result.get(false).size());
    }

    @Test
    public void testGetAgeStatistics() {
        IntSummaryStatistics stats = streamService.getAgeStatistics(personList);
        assertEquals(5, stats.getCount());
        assertEquals(25, stats.getMin());
        assertEquals(35, stats.getMax());
        assertEquals(143, stats.getSum());
    }

    @Test
    public void testGetAverageAge() {
        double avg = streamService.getAverageAge(personList);
        assertEquals(28.6, avg, 0.01);
    }

    @Test
    public void testGetWordFrequency() {
        String text = "Java is fun and Java is powerful";
        Map<String, Long> result = streamService.getWordFrequency(text);
        
        assertEquals(Long.valueOf(2), result.get("java"));
        assertEquals(Long.valueOf(2), result.get("is"));
        assertEquals(Long.valueOf(1), result.get("fun"));
    }

    @Test
    public void testCountEvensParallel() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            numbers.add(i);
        }
        long count = streamService.countEvensParallel(numbers);
        assertEquals(500, count);
    }
    
    @Test
    public void testGenerateEvens() {
        List<Integer> evens = streamService.generateEvens(5);
        assertEquals(Arrays.asList(0, 2, 4, 6, 8), evens);
    }

    @Test
    public void testDebugStream() {
        List<String> input = Arrays.asList("one", "two", "three", "four");
        List<String> result = streamService.debugStream(input);
        assertEquals(Arrays.asList("THREE", "FOUR"), result);
    }
}
