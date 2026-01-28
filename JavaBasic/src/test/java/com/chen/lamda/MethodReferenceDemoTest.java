package com.chen.lamda;

import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MethodReferenceDemo
 */
class MethodReferenceDemoTest {

    @Test
    void testStaticMethodReference() {
        // Integer::parseInt
        Function<String, Integer> parser = Integer::parseInt;
        assertEquals(123, parser.apply("123"));
        assertEquals(456, parser.apply("456"));

        // Math::max
        BinaryOperator<Integer> max = Math::max;
        assertEquals(10, max.apply(5, 10));
        assertEquals(100, max.apply(100, 50));

        // Math::min
        BinaryOperator<Integer> min = Math::min;
        assertEquals(5, min.apply(5, 10));
        assertEquals(50, min.apply(100, 50));
    }

    @Test
    void testInstanceMethodReferenceOfParticularObject() {
        String text = "Hello World";

        // text::length
        Supplier<Integer> length = text::length;
        assertEquals(11, length.get());

        // text::toUpperCase
        Supplier<String> upper = text::toUpperCase;
        assertEquals("HELLO WORLD", upper.get());

        // text::toLowerCase
        Supplier<String> lower = text::toLowerCase;
        assertEquals("hello world", lower.get());

        MethodReferenceDemo.Person person = new MethodReferenceDemo.Person("Alice", 25);

        // person::getName
        Supplier<String> nameSupplier = person::getName;
        assertEquals("Alice", nameSupplier.get());

        // person::getAge
        Supplier<Integer> ageSupplier = person::getAge;
        assertEquals(25, ageSupplier.get());
    }

    @Test
    void testInstanceMethodReferenceOfArbitraryObject() {
        // String::length
        Function<String, Integer> length = String::length;
        assertEquals(5, length.apply("Hello"));
        assertEquals(11, length.apply("Hello World"));

        // String::toUpperCase
        Function<String, String> upper = String::toUpperCase;
        assertEquals("HELLO", upper.apply("hello"));

        // String::toLowerCase
        Function<String, String> lower = String::toLowerCase;
        assertEquals("world", lower.apply("WORLD"));

        // String::compareTo
        BiFunction<String, String, Integer> compareTo = String::compareTo;
        assertTrue(compareTo.apply("apple", "banana") < 0);
        assertTrue(compareTo.apply("banana", "apple") > 0);
        assertEquals(0, compareTo.apply("test", "test"));
    }

    @Test
    void testSortingWithMethodReference() {
        List<String> words = new ArrayList<>(Arrays.asList("cherry", "apple", "banana", "date"));

        // Sort using String::compareTo
        words.sort(String::compareTo);
        assertEquals(Arrays.asList("apple", "banana", "cherry", "date"), words);

        // Sort in reverse order
        words.sort(Comparator.reverseOrder());
        assertEquals(Arrays.asList("date", "cherry", "banana", "apple"), words);
    }

    @Test
    void testConstructorReference() {
        // ArrayList::new
        Supplier<List<String>> listSupplier = ArrayList::new;
        List<String> list = listSupplier.get();
        assertNotNull(list);
        assertTrue(list.isEmpty());

        // Person::new (no-arg constructor)
        Supplier<MethodReferenceDemo.Person> personSupplier = MethodReferenceDemo.Person::new;
        MethodReferenceDemo.Person person = personSupplier.get();
        assertNotNull(person);
        assertEquals("Unknown", person.getName());
        assertEquals(0, person.getAge());

        // Person::new (two-arg constructor)
        BiFunction<String, Integer, MethodReferenceDemo.Person> personFactory =
            MethodReferenceDemo.Person::new;
        MethodReferenceDemo.Person alice = personFactory.apply("Alice", 25);
        assertEquals("Alice", alice.getName());
        assertEquals(25, alice.getAge());

        MethodReferenceDemo.Person bob = personFactory.apply("Bob", 30);
        assertEquals("Bob", bob.getName());
        assertEquals(30, bob.getAge());
    }

    @Test
    void testArrayConstructorReference() {
        // String[]::new
        IntFunction<String[]> arrayCreator = String[]::new;
        String[] array = arrayCreator.apply(5);
        assertNotNull(array);
        assertEquals(5, array.length);

        // Using with stream
        List<String> words = Arrays.asList("apple", "banana", "cherry");
        String[] wordArray = words.stream().toArray(String[]::new);
        assertEquals(3, wordArray.length);
        assertEquals("apple", wordArray[0]);
        assertEquals("banana", wordArray[1]);
        assertEquals("cherry", wordArray[2]);

        // Integer[]::new
        IntFunction<Integer[]> intArrayCreator = Integer[]::new;
        Integer[] intArray = intArrayCreator.apply(10);
        assertEquals(10, intArray.length);
    }

    @Test
    void testMethodReferenceInStream() {
        List<String> words = Arrays.asList("apple", "banana", "cherry");

        // map with String::toUpperCase
        List<String> upper = words.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("APPLE", "BANANA", "CHERRY"), upper);

        // map with String::length
        List<Integer> lengths = words.stream()
                .map(String::length)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(5, 6, 6), lengths);

        // forEach with System.out::println
        List<String> printed = new ArrayList<>();
        words.forEach(printed::add);
        assertEquals(words, printed);
    }

    @Test
    void testPersonMethodReferences() {
        List<MethodReferenceDemo.Person> people = Arrays.asList(
                new MethodReferenceDemo.Person("Charlie", 35),
                new MethodReferenceDemo.Person("Alice", 25),
                new MethodReferenceDemo.Person("Bob", 30)
        );

        // Sort by age using static method reference
        people.sort(MethodReferenceDemo.Person::compareByAge);
        assertEquals("Alice", people.get(0).getName());
        assertEquals("Bob", people.get(1).getName());
        assertEquals("Charlie", people.get(2).getName());

        // Map to names
        List<String> names = people.stream()
                .map(MethodReferenceDemo.Person::getName)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("Alice", "Bob", "Charlie"), names);

        // Map to ages
        List<Integer> ages = people.stream()
                .map(MethodReferenceDemo.Person::getAge)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(25, 30, 35), ages);
    }

    @Test
    void testSystemOutPrintlnReference() {
        // System.out::println
        Consumer<String> printer = System.out::println;
        assertNotNull(printer);

        List<String> messages = Arrays.asList("Hello", "World");
        // Should not throw exception
        messages.forEach(System.out::println);
    }

    @Test
    void testObjectMethodReferences() {
        // Objects::isNull
        Predicate<String> isNull = Objects::isNull;
        assertTrue(isNull.test(null));
        assertFalse(isNull.test("Hello"));

        // Objects::nonNull
        Predicate<String> nonNull = Objects::nonNull;
        assertTrue(nonNull.test("Hello"));
        assertFalse(nonNull.test(null));

        // Objects::requireNonNull
        Function<String, String> requireNonNull = Objects::requireNonNull;
        assertEquals("Hello", requireNonNull.apply("Hello"));
        assertThrows(NullPointerException.class, () -> requireNonNull.apply(null));
    }

    @Test
    void testComparingWithMethodReference() {
        List<MethodReferenceDemo.Person> people = Arrays.asList(
                new MethodReferenceDemo.Person("Charlie", 35),
                new MethodReferenceDemo.Person("Alice", 25),
                new MethodReferenceDemo.Person("Bob", 30)
        );

        // Sort by name using Comparator.comparing
        people.sort(Comparator.comparing(MethodReferenceDemo.Person::getName));
        assertEquals("Alice", people.get(0).getName());
        assertEquals("Bob", people.get(1).getName());
        assertEquals("Charlie", people.get(2).getName());

        // Sort by age
        people.sort(Comparator.comparing(MethodReferenceDemo.Person::getAge));
        assertEquals(25, people.get(0).getAge());
        assertEquals(30, people.get(1).getAge());
        assertEquals(35, people.get(2).getAge());
    }

    @Test
    void testCollectionMethodReferences() {
        List<String> list = new ArrayList<>();

        // list::add
        Consumer<String> adder = list::add;
        adder.accept("Hello");
        adder.accept("World");
        assertEquals(Arrays.asList("Hello", "World"), list);

        // list::contains
        Predicate<String> contains = list::contains;
        assertTrue(contains.test("Hello"));
        assertFalse(contains.test("Java"));

        // list::size
        Supplier<Integer> sizeSupplier = list::size;
        assertEquals(2, sizeSupplier.get());
    }

    @Test
    void testBooleanMethodReference() {
        List<Boolean> booleans = Arrays.asList(true, false, true);

        // Count true values
        long trueCount = booleans.stream()
                .filter(Boolean::booleanValue)
                .count();
        assertEquals(2, trueCount);

        // Boolean::valueOf
        Function<String, Boolean> valueOf = Boolean::valueOf;
        assertTrue(valueOf.apply("true"));
        assertFalse(valueOf.apply("false"));
    }

    @Test
    void testDoubleColonVsLambda() {
        // These should be equivalent
        Function<String, Integer> lambda = s -> Integer.parseInt(s);
        Function<String, Integer> methodRef = Integer::parseInt;

        assertEquals(lambda.apply("123"), methodRef.apply("123"));

        // These should be equivalent
        Consumer<String> lambdaPrinter = s -> System.out.println(s);
        Consumer<String> methodRefPrinter = System.out::println;

        // Both should work without exception
        lambdaPrinter.accept("test");
        methodRefPrinter.accept("test");
    }

    @Test
    void testChainedMethodReferences() {
        List<String> words = Arrays.asList("apple", "BANANA", "Cherry");

        List<String> result = words.stream()
                .map(String::toLowerCase)
                .map(String::trim)
                .sorted(String::compareTo)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList("apple", "banana", "cherry"), result);
    }
}