package com.chen.lamda;

import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.function.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BasicLambdaDemo
 */
class BasicLambdaDemoTest {

    @Test
    void testLambdaSyntax() {
        // No parameters lambda
        Runnable r = () -> System.out.println("Test");
        assertNotNull(r);

        // Single parameter
        Consumer<String> consumer = s -> s.toUpperCase();
        assertNotNull(consumer);

        // Multiple parameters
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        assertEquals(8, add.apply(5, 3));

        // Block body
        BiFunction<Integer, Integer, Integer> multiply = (a, b) -> {
            return a * b;
        };
        assertEquals(20, multiply.apply(4, 5));
    }

    @Test
    void testFilterNumbers() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Filter even numbers
        List<Integer> evenNumbers = BasicLambdaDemo.filterNumbers(numbers, n -> n % 2 == 0);
        assertEquals(Arrays.asList(2, 4, 6, 8, 10), evenNumbers);

        // Filter odd numbers
        List<Integer> oddNumbers = BasicLambdaDemo.filterNumbers(numbers, n -> n % 2 != 0);
        assertEquals(Arrays.asList(1, 3, 5, 7, 9), oddNumbers);

        // Filter numbers greater than 5
        List<Integer> greaterThan5 = BasicLambdaDemo.filterNumbers(numbers, n -> n > 5);
        assertEquals(Arrays.asList(6, 7, 8, 9, 10), greaterThan5);
    }

    @Test
    void testMap() {
        List<String> strings = Arrays.asList("1", "2", "3", "4", "5");

        // Map to integers
        List<Integer> integers = BasicLambdaDemo.map(strings, Integer::parseInt);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), integers);

        // Map to string length
        List<String> words = Arrays.asList("apple", "banana", "cherry");
        List<Integer> lengths = BasicLambdaDemo.map(words, String::length);
        assertEquals(Arrays.asList(5, 6, 6), lengths);

        // Map to uppercase
        List<String> upperCase = BasicLambdaDemo.map(words, String::toUpperCase);
        assertEquals(Arrays.asList("APPLE", "BANANA", "CHERRY"), upperCase);
    }

    @Test
    void testProcess() {
        List<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> result = new ArrayList<>();

        // Process with consumer that adds to result
        BasicLambdaDemo.process(items, item -> result.add(item.toUpperCase()));
        assertEquals(Arrays.asList("A", "B", "C"), result);
    }

    @Test
    void testGetOrDefault() {
        // Test supplier
        String result = BasicLambdaDemo.getOrDefault(() -> "default");
        assertEquals("default", result);

        Integer number = BasicLambdaDemo.getOrDefault(() -> 42);
        assertEquals(42, number);
    }

    @Test
    void testPredicateComposition() {
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;

        // Test AND
        Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);
        assertTrue(isEvenAndPositive.test(4));
        assertFalse(isEvenAndPositive.test(-4));
        assertFalse(isEvenAndPositive.test(3));

        // Test OR
        Predicate<Integer> isEvenOrPositive = isEven.or(isPositive);
        assertTrue(isEvenOrPositive.test(4));
        assertTrue(isEvenOrPositive.test(3));
        assertTrue(isEvenOrPositive.test(-4));
        assertFalse(isEvenOrPositive.test(-3));

        // Test NEGATE
        Predicate<Integer> isOdd = isEven.negate();
        assertTrue(isOdd.test(3));
        assertFalse(isOdd.test(4));
    }

    @Test
    void testFunctionComposition() {
        Function<Integer, Integer> multiplyBy2 = x -> x * 2;
        Function<Integer, Integer> add10 = x -> x + 10;

        // Test andThen
        Function<Integer, Integer> multiplyThenAdd = multiplyBy2.andThen(add10);
        assertEquals(20, multiplyThenAdd.apply(5)); // (5*2)+10 = 20

        // Test compose
        Function<Integer, Integer> addThenMultiply = multiplyBy2.compose(add10);
        assertEquals(30, addThenMultiply.apply(5)); // (5+10)*2 = 30
    }

    @Test
    void testConsumerChaining() {
        List<String> result = new ArrayList<>();

        Consumer<String> addToList = s -> result.add(s);
        Consumer<String> addUpperCase = s -> result.add(s.toUpperCase());
        Consumer<String> chained = addToList.andThen(addUpperCase);

        chained.accept("hello");
        assertEquals(Arrays.asList("hello", "HELLO"), result);
    }

    @Test
    void testBiFunction() {
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        BiFunction<String, String, String> concat = (s1, s2) -> s1 + s2;

        assertEquals(8, add.apply(5, 3));
        assertEquals("HelloWorld", concat.apply("Hello", "World"));
    }

    @Test
    void testCollectionOperations() {
        List<String> names = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve"));

        // Test sort with lambda
        names.sort((s1, s2) -> s1.compareTo(s2));
        assertEquals("Alice", names.get(0));
        assertEquals("Eve", names.get(4));

        // Test removeIf
        names.removeIf(name -> name.length() < 4);
        assertEquals(3, names.size());
        assertTrue(names.contains("Alice"));
        assertTrue(names.contains("Charlie"));
        assertTrue(names.contains("David"));
    }

    @Test
    void testVariableCapture() {
        final String prefix = "Hello, ";
        Function<String, String> greeter = name -> prefix + name;

        assertEquals("Hello, Alice", greeter.apply("Alice"));
        assertEquals("Hello, Bob", greeter.apply("Bob"));
    }
}