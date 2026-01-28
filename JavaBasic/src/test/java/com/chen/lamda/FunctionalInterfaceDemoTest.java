package com.chen.lamda;

import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.function.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FunctionalInterfaceDemo
 */
class FunctionalInterfaceDemoTest {

    @Test
    void testCustomCalculatorInterface() {
        FunctionalInterfaceDemo.Calculator addition = (a, b) -> a + b;
        FunctionalInterfaceDemo.Calculator subtraction = (a, b) -> a - b;
        FunctionalInterfaceDemo.Calculator multiplication = (a, b) -> a * b;
        FunctionalInterfaceDemo.Calculator division = (a, b) -> b != 0 ? a / b : 0;

        assertEquals(15, addition.calculate(10, 5));
        assertEquals(5, subtraction.calculate(10, 5));
        assertEquals(50, multiplication.calculate(10, 5));
        assertEquals(2, division.calculate(10, 5));
        assertEquals(0, division.calculate(10, 0)); // Division by zero
    }

    @Test
    void testCustomStringProcessorInterface() {
        FunctionalInterfaceDemo.StringProcessor toUpperCase = String::toUpperCase;
        FunctionalInterfaceDemo.StringProcessor toLowerCase = String::toLowerCase;
        FunctionalInterfaceDemo.StringProcessor reverse = s -> new StringBuilder(s).reverse().toString();

        assertEquals("HELLO", toUpperCase.process("hello"));
        assertEquals("world", toLowerCase.process("WORLD"));
        assertEquals("dlroW", reverse.process("World"));
    }

    @Test
    void testCustomValidatorInterface() {
        FunctionalInterfaceDemo.Validator<String> emailValidator =
            email -> email != null && email.contains("@");
        FunctionalInterfaceDemo.Validator<Integer> positiveValidator =
            num -> num != null && num > 0;

        assertTrue(emailValidator.validate("test@example.com"));
        assertFalse(emailValidator.validate("invalid"));
        assertFalse(emailValidator.validate(null));

        assertTrue(positiveValidator.validate(10));
        assertFalse(positiveValidator.validate(-5));
        assertFalse(positiveValidator.validate(null));
    }

    @Test
    void testCustomTriFunctionInterface() {
        FunctionalInterfaceDemo.TriFunction<Integer, Integer, Integer, Integer> sumOfThree =
            (a, b, c) -> a + b + c;
        FunctionalInterfaceDemo.TriFunction<String, String, String, String> concatThree =
            (a, b, c) -> a + b + c;

        assertEquals(6, sumOfThree.apply(1, 2, 3));
        assertEquals("ABC", concatThree.apply("A", "B", "C"));
    }

    @Test
    void testPredicate() {
        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;

        assertTrue(isEmpty.test(""));
        assertFalse(isEmpty.test("Hello"));
        assertTrue(isNotEmpty.test("Hello"));

        assertTrue(isEven.test(4));
        assertFalse(isEven.test(3));

        // Test and
        assertTrue(isEven.and(isPositive).test(4));
        assertFalse(isEven.and(isPositive).test(-4));

        // Test or
        assertTrue(isEven.or(isPositive).test(-4));
        assertTrue(isEven.or(isPositive).test(3));
        assertFalse(isEven.or(isPositive).test(-3));
    }

    @Test
    void testFunction() {
        Function<String, Integer> stringLength = String::length;
        Function<Integer, Integer> square = n -> n * n;
        Function<Integer, String> intToString = Object::toString;

        assertEquals(5, stringLength.apply("Hello"));
        assertEquals(25, square.apply(5));
        assertEquals("123", intToString.apply(123));

        // Test andThen
        Function<String, Integer> lengthSquared = stringLength.andThen(square);
        assertEquals(25, lengthSquared.apply("Hello")); // length=5, square=25

        // Test compose
        Function<Integer, String> squareToString = intToString.compose(square);
        assertEquals("25", squareToString.apply(5)); // square=25, toString="25"
    }

    @Test
    void testConsumer() {
        List<String> result = new ArrayList<>();

        Consumer<String> addToList = result::add;
        Consumer<String> addUpperCase = s -> result.add(s.toUpperCase());

        addToList.accept("hello");
        assertEquals(Arrays.asList("hello"), result);

        result.clear();
        addUpperCase.accept("world");
        assertEquals(Arrays.asList("WORLD"), result);

        // Test andThen
        result.clear();
        Consumer<String> chained = addToList.andThen(addUpperCase);
        chained.accept("test");
        assertEquals(Arrays.asList("test", "TEST"), result);
    }

    @Test
    void testSupplier() {
        Supplier<String> stringSupplier = () -> "Hello";
        Supplier<Integer> intSupplier = () -> 42;
        Supplier<List<String>> listSupplier = ArrayList::new;

        assertEquals("Hello", stringSupplier.get());
        assertEquals(42, intSupplier.get());
        assertNotNull(listSupplier.get());
        assertTrue(listSupplier.get() instanceof ArrayList);
    }

    @Test
    void testBiFunction() {
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        BiFunction<String, String, String> concat = (s1, s2) -> s1 + s2;
        BiFunction<Integer, Integer, Double> divide = (a, b) -> b != 0 ? (double) a / b : 0.0;

        assertEquals(8, add.apply(5, 3));
        assertEquals("HelloWorld", concat.apply("Hello", "World"));
        assertEquals(3.333, divide.apply(10, 3), 0.001);
        assertEquals(0.0, divide.apply(10, 0), 0.001);

        // Test andThen
        BiFunction<Integer, Integer, Integer> addThenSquare =
            add.andThen(n -> n * n);
        assertEquals(64, addThenSquare.apply(5, 3)); // (5+3)^2 = 64
    }

    @Test
    void testBiConsumer() {
        Map<String, Integer> map = new HashMap<>();
        BiConsumer<String, Integer> putToMap = map::put;

        putToMap.accept("one", 1);
        putToMap.accept("two", 2);

        assertEquals(2, map.size());
        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));

        // Test andThen
        List<String> log = new ArrayList<>();
        BiConsumer<String, Integer> logger = (k, v) -> log.add(k + "=" + v);
        BiConsumer<String, Integer> chained = putToMap.andThen(logger);

        chained.accept("three", 3);
        assertEquals(3, map.get("three"));
        assertTrue(log.contains("three=3"));
    }

    @Test
    void testBiPredicate() {
        BiPredicate<String, Integer> isLengthEqual = (s, len) -> s.length() == len;
        BiPredicate<Integer, Integer> isGreater = (a, b) -> a > b;

        assertTrue(isLengthEqual.test("Hello", 5));
        assertFalse(isLengthEqual.test("Hello", 4));

        assertTrue(isGreater.test(10, 5));
        assertFalse(isGreater.test(5, 10));

        // Test and
        BiPredicate<Integer, Integer> isGreaterAndEven =
            isGreater.and((a, b) -> a % 2 == 0);
        assertTrue(isGreaterAndEven.test(10, 5));
        assertFalse(isGreaterAndEven.test(9, 5));
    }

    @Test
    void testUnaryOperator() {
        UnaryOperator<Integer> square = n -> n * n;
        UnaryOperator<String> toUpper = String::toUpperCase;
        UnaryOperator<Integer> increment = n -> n + 1;

        assertEquals(25, square.apply(5));
        assertEquals("HELLO", toUpper.apply("hello"));
        assertEquals(6, increment.apply(5));

        // Test andThen
        // UnaryOperator extends Function, but andThen returns Function, not UnaryOperator
        // So we need to use Function<Integer, Integer> or cast it, but usually Function is enough
        Function<Integer, Integer> squareThenIncrement = square.andThen(increment);
        assertEquals(26, squareThenIncrement.apply(5)); // 5^2 + 1 = 26
    }

    @Test
    void testBinaryOperator() {
        BinaryOperator<Integer> max = (a, b) -> a > b ? a : b;
        BinaryOperator<Integer> min = (a, b) -> a < b ? a : b;
        BinaryOperator<String> concat = (s1, s2) -> s1 + s2;

        assertEquals(10, max.apply(5, 10));
        assertEquals(5, min.apply(5, 10));
        assertEquals("HelloWorld", concat.apply("Hello", "World"));

        // Test with BinaryOperator.maxBy and minBy
        BinaryOperator<Integer> maxBy = BinaryOperator.maxBy(Integer::compareTo);
        BinaryOperator<Integer> minBy = BinaryOperator.minBy(Integer::compareTo);

        assertEquals(10, maxBy.apply(5, 10));
        assertEquals(5, minBy.apply(5, 10));
    }

    @Test
    void testPredicateWithNull() {
        Predicate<String> isNull = Objects::isNull;
        Predicate<String> isNotNull = Objects::nonNull;

        assertTrue(isNull.test(null));
        assertFalse(isNull.test("Hello"));

        assertTrue(isNotNull.test("Hello"));
        assertFalse(isNotNull.test(null));
    }

    @Test
    void testFunctionIdentity() {
        Function<String, String> identity = Function.identity();

        assertEquals("Hello", identity.apply("Hello"));
        assertEquals("World", identity.apply("World"));
    }
}