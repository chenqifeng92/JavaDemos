package com.chen.lamda;

import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.stream.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StreamLambdaDemo
 */
class StreamLambdaDemoTest {

    @Test
    void testBasicStreamFilter() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<Integer> evenNumbers = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList(2, 4, 6, 8, 10), evenNumbers);
        assertEquals(5, evenNumbers.size());
    }

    @Test
    void testBasicStreamMap() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        List<Integer> squares = numbers.stream()
                .map(n -> n * n)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList(1, 4, 9, 16, 25), squares);
    }

    @Test
    void testStreamSorted() {
        List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9, 3);

        List<Integer> ascending = numbers.stream()
                .sorted()
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(1, 2, 3, 5, 8, 9), ascending);

        List<Integer> descending = numbers.stream()
                .sorted((a, b) -> b - a)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(9, 8, 5, 3, 2, 1), descending);
    }

    @Test
    void testStreamDistinct() {
        List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);

        List<Integer> distinct = numbers.stream()
                .distinct()
                .collect(Collectors.toList());

        assertEquals(Arrays.asList(1, 2, 3, 4), distinct);
        assertEquals(4, distinct.size());
    }

    @Test
    void testStreamLimitAndSkip() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<Integer> limited = numbers.stream()
                .limit(5)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), limited);

        List<Integer> skipped = numbers.stream()
                .skip(5)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(6, 7, 8, 9, 10), skipped);

        List<Integer> skipAndLimit = numbers.stream()
                .skip(3)
                .limit(5)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(4, 5, 6, 7, 8), skipAndLimit);
    }

    @Test
    void testStreamCount() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        long count = numbers.stream()
                .filter(n -> n > 5)
                .count();

        assertEquals(5, count);
    }

    @Test
    void testStreamMatching() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        boolean anyEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        assertTrue(anyEven);

        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        assertTrue(allPositive);

        boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
        assertTrue(noneNegative);

        boolean allEven = numbers.stream().allMatch(n -> n % 2 == 0);
        assertFalse(allEven);
    }

    @Test
    void testStreamFindFirst() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        Optional<Integer> first = numbers.stream()
                .filter(n -> n > 3)
                .findFirst();

        assertTrue(first.isPresent());
        assertEquals(4, first.get());

        Optional<Integer> notFound = numbers.stream()
                .filter(n -> n > 10)
                .findFirst();

        assertFalse(notFound.isPresent());
    }

    @Test
    void testStreamMinMax() {
        List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9, 3);

        Optional<Integer> min = numbers.stream().min(Integer::compareTo);
        assertTrue(min.isPresent());
        assertEquals(1, min.get());

        Optional<Integer> max = numbers.stream().max(Integer::compareTo);
        assertTrue(max.isPresent());
        assertEquals(9, max.get());
    }

    @Test
    void testStreamReduce() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        Optional<Integer> sum = numbers.stream()
                .reduce((a, b) -> a + b);
        assertTrue(sum.isPresent());
        assertEquals(15, sum.get());

        Integer sumWithInitial = numbers.stream()
                .reduce(0, Integer::sum);
        assertEquals(15, sumWithInitial);

        Integer product = numbers.stream()
                .reduce(1, (a, b) -> a * b);
        assertEquals(120, product);
    }

    @Test
    void testPersonStreamOperations() {
        List<StreamLambdaDemo.Person> people = Arrays.asList(
                new StreamLambdaDemo.Person("Alice", 25, "New York", 60000),
                new StreamLambdaDemo.Person("Bob", 30, "London", 75000),
                new StreamLambdaDemo.Person("Charlie", 35, "New York", 80000)
        );

        // Filter by age
        List<StreamLambdaDemo.Person> over30 = people.stream()
                .filter(p -> p.getAge() > 30)
                .collect(Collectors.toList());
        assertEquals(1, over30.size());
        assertEquals("Charlie", over30.get(0).getName());

        // Map to names
        List<String> names = people.stream()
                .map(StreamLambdaDemo.Person::getName)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("Alice", "Bob", "Charlie"), names);

        // Average salary
        double avgSalary = people.stream()
                .mapToDouble(StreamLambdaDemo.Person::getSalary)
                .average()
                .orElse(0.0);
        assertEquals(71666.67, avgSalary, 0.01);

        // Total salary
        double totalSalary = people.stream()
                .mapToDouble(StreamLambdaDemo.Person::getSalary)
                .sum();
        assertEquals(215000, totalSalary, 0.01);
    }

    @Test
    void testStreamGroupingBy() {
        List<StreamLambdaDemo.Person> people = Arrays.asList(
                new StreamLambdaDemo.Person("Alice", 25, "New York", 60000),
                new StreamLambdaDemo.Person("Bob", 30, "London", 75000),
                new StreamLambdaDemo.Person("Charlie", 35, "New York", 80000),
                new StreamLambdaDemo.Person("David", 28, "London", 65000)
        );

        Map<String, List<StreamLambdaDemo.Person>> byCity = people.stream()
                .collect(Collectors.groupingBy(StreamLambdaDemo.Person::getCity));

        assertEquals(2, byCity.size());
        assertEquals(2, byCity.get("New York").size());
        assertEquals(2, byCity.get("London").size());

        // Count by city
        Map<String, Long> countByCity = people.stream()
                .collect(Collectors.groupingBy(
                        StreamLambdaDemo.Person::getCity,
                        Collectors.counting()
                ));

        assertEquals(2L, countByCity.get("New York"));
        assertEquals(2L, countByCity.get("London"));
    }

    @Test
    void testStreamPartitioningBy() {
        List<StreamLambdaDemo.Person> people = Arrays.asList(
                new StreamLambdaDemo.Person("Alice", 25, "New York", 60000),
                new StreamLambdaDemo.Person("Bob", 30, "London", 75000),
                new StreamLambdaDemo.Person("Charlie", 35, "New York", 80000)
        );

        Map<Boolean, List<StreamLambdaDemo.Person>> partitioned = people.stream()
                .collect(Collectors.partitioningBy(p -> p.getAge() > 30));

        assertEquals(1, partitioned.get(true).size());
        assertEquals("Charlie", partitioned.get(true).get(0).getName());

        assertEquals(2, partitioned.get(false).size());
    }

    @Test
    void testStreamFlatMap() {
        List<List<Integer>> listOfLists = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5),
                Arrays.asList(6, 7, 8)
        );

        List<Integer> flattened = listOfLists.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), flattened);

        // Test with strings
        List<String> sentences = Arrays.asList("Hello World", "Java Lambda");
        List<String> words = sentences.stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .collect(Collectors.toList());

        assertEquals(Arrays.asList("Hello", "World", "Java", "Lambda"), words);
    }

    @Test
    void testStreamCollectorsJoining() {
        List<String> words = Arrays.asList("apple", "banana", "cherry");

        String joined = words.stream()
                .collect(Collectors.joining(", "));
        assertEquals("apple, banana, cherry", joined);

        String joinedWithDelimiters = words.stream()
                .collect(Collectors.joining(", ", "[", "]"));
        assertEquals("[apple, banana, cherry]", joinedWithDelimiters);
    }

    @Test
    void testStreamCollectorsToMap() {
        List<String> words = Arrays.asList("apple", "banana", "cherry");

        Map<String, Integer> wordLengths = words.stream()
                .collect(Collectors.toMap(
                        w -> w,
                        String::length
                ));

        assertEquals(3, wordLengths.size());
        assertEquals(5, wordLengths.get("apple"));
        assertEquals(6, wordLengths.get("banana"));
        assertEquals(6, wordLengths.get("cherry"));
    }

    @Test
    void testStreamPeek() {
        List<String> result = new ArrayList<>();
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        List<Integer> processed = numbers.stream()
                .peek(n -> result.add("Processing: " + n))
                .map(n -> n * 2)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList(2, 4, 6, 8, 10), processed);
        assertEquals(5, result.size());
        assertEquals("Processing: 1", result.get(0));
    }

    @Test
    void testStreamIntStream() {
        // Range
        List<Integer> range = IntStream.range(1, 5)
                .boxed()
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(1, 2, 3, 4), range);

        // RangeClosed
        List<Integer> rangeClosed = IntStream.rangeClosed(1, 5)
                .boxed()
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), rangeClosed);

        // Sum
        int sum = IntStream.rangeClosed(1, 10).sum();
        assertEquals(55, sum);

        // Average
        double avg = IntStream.rangeClosed(1, 10).average().orElse(0.0);
        assertEquals(5.5, avg, 0.01);
    }

    @Test
    void testStreamChaining() {
        List<String> words = Arrays.asList("apple", "BANANA", "Cherry", "DATE");

        List<String> processed = words.stream()
                .map(String::toLowerCase)
                .filter(s -> s.length() > 4)
                .sorted()
                .collect(Collectors.toList());

        assertEquals(Arrays.asList("apple", "banana", "cherry"), processed);
    }

    @Test
    void testStreamEmptyList() {
        List<Integer> empty = new ArrayList<>();

        long count = empty.stream().count();
        assertEquals(0, count);

        Optional<Integer> first = empty.stream().findFirst();
        assertFalse(first.isPresent());

        List<Integer> filtered = empty.stream()
                .filter(n -> n > 0)
                .collect(Collectors.toList());
        assertTrue(filtered.isEmpty());
    }

    @Test
    void testStreamNullHandling() {
        List<String> words = Arrays.asList("apple", null, "banana", null, "cherry");

        List<String> nonNull = words.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList("apple", "banana", "cherry"), nonNull);
        assertEquals(3, nonNull.size());
    }
}