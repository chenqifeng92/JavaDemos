package com.chen.lamda;

import java.util.*;
import java.util.function.*;

/**
 * Functional Interface Demo
 * Demonstrates custom and built-in functional interfaces in Java 8
 */
public class FunctionalInterfaceDemo {

    // ========== Custom Functional Interfaces ==========

    @FunctionalInterface
    interface Calculator {
        int calculate(int a, int b);
    }

    @FunctionalInterface
    interface StringProcessor {
        String process(String input);
    }

    @FunctionalInterface
    interface Validator<T> {
        boolean validate(T value);
    }

    @FunctionalInterface
    interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    // ========== Custom Functional Interface Examples ==========

    public static void customFunctionalInterfaceExamples() {
        // Calculator interface
        Calculator addition = (a, b) -> a + b;
        Calculator subtraction = (a, b) -> a - b;
        Calculator multiplication = (a, b) -> a * b;
        Calculator division = (a, b) -> b != 0 ? a / b : 0;

        System.out.println("Addition: 10 + 5 = " + addition.calculate(10, 5));
        System.out.println("Subtraction: 10 - 5 = " + subtraction.calculate(10, 5));
        System.out.println("Multiplication: 10 * 5 = " + multiplication.calculate(10, 5));
        System.out.println("Division: 10 / 5 = " + division.calculate(10, 5));

        // StringProcessor interface
        StringProcessor toUpperCase = String::toUpperCase;
        StringProcessor toLowerCase = String::toLowerCase;
        StringProcessor reverse = s -> new StringBuilder(s).reverse().toString();

        String text = "Hello World";
        System.out.println("Original: " + text);
        System.out.println("Upper: " + toUpperCase.process(text));
        System.out.println("Lower: " + toLowerCase.process(text));
        System.out.println("Reverse: " + reverse.process(text));

        // Validator interface
        Validator<String> emailValidator = email -> email != null && email.contains("@");
        Validator<Integer> positiveValidator = num -> num != null && num > 0;

        System.out.println("Is 'test@example.com' valid email? " + emailValidator.validate("test@example.com"));
        System.out.println("Is 'invalid' valid email? " + emailValidator.validate("invalid"));
        System.out.println("Is 10 positive? " + positiveValidator.validate(10));
        System.out.println("Is -5 positive? " + positiveValidator.validate(-5));

        // TriFunction interface
        TriFunction<Integer, Integer, Integer, Integer> sumOfThree = (a, b, c) -> a + b + c;
        System.out.println("Sum of 1, 2, 3: " + sumOfThree.apply(1, 2, 3));
    }

    // ========== Built-in Functional Interfaces ==========

    /**
     * Predicate<T>: Represents a boolean-valued function of one argument
     */
    public static void predicateExamples() {
        System.out.println("\n=== Predicate Examples ===");

        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;

        System.out.println("Is '' empty? " + isEmpty.test(""));
        System.out.println("Is 'Hello' not empty? " + isNotEmpty.test("Hello"));
        System.out.println("Is 4 even? " + isEven.test(4));
        System.out.println("Is 4 even and positive? " + isEven.and(isPositive).test(4));
        System.out.println("Is -4 even or positive? " + isEven.or(isPositive).test(-4));
    }

    /**
     * Function<T, R>: Represents a function that accepts one argument and produces a result
     */
    public static void functionExamples() {
        System.out.println("\n=== Function Examples ===");

        Function<String, Integer> stringLength = String::length;
        Function<Integer, Integer> square = n -> n * n;
        Function<Integer, String> intToString = Object::toString;

        System.out.println("Length of 'Hello': " + stringLength.apply("Hello"));
        System.out.println("Square of 5: " + square.apply(5));
        System.out.println("Int 123 to String: " + intToString.apply(123));

        // Function composition
        Function<String, Integer> stringLengthSquared = stringLength.andThen(square);
        System.out.println("Length of 'Hello' squared: " + stringLengthSquared.apply("Hello"));
    }

    /**
     * Consumer<T>: Represents an operation that accepts a single input argument and returns no result
     */
    public static void consumerExamples() {
        System.out.println("\n=== Consumer Examples ===");

        Consumer<String> print = System.out::println;
        Consumer<String> printUpper = s -> System.out.println(s.toUpperCase());
        Consumer<List<String>> printList = list -> list.forEach(System.out::println);

        print.accept("Hello Consumer");
        printUpper.accept("hello world");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        printList.accept(names);

        // Consumer chaining
        Consumer<String> printAndUpperCase = print.andThen(printUpper);
        printAndUpperCase.accept("chaining");
    }

    /**
     * Supplier<T>: Represents a supplier of results
     */
    public static void supplierExamples() {
        System.out.println("\n=== Supplier Examples ===");

        Supplier<String> stringSupplier = () -> "Hello from Supplier";
        Supplier<Double> randomSupplier = Math::random;
        Supplier<List<String>> listSupplier = ArrayList::new;

        System.out.println(stringSupplier.get());
        System.out.println("Random number: " + randomSupplier.get());
        System.out.println("New list: " + listSupplier.get());
    }

    /**
     * BiFunction<T, U, R>: Represents a function that accepts two arguments and produces a result
     */
    public static void biFunctionExamples() {
        System.out.println("\n=== BiFunction Examples ===");

        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        BiFunction<String, String, String> concat = (s1, s2) -> s1 + s2;
        BiFunction<Integer, Integer, Double> divide = (a, b) -> b != 0 ? (double) a / b : 0.0;

        System.out.println("5 + 3 = " + add.apply(5, 3));
        System.out.println("'Hello' + ' World' = " + concat.apply("Hello", " World"));
        System.out.println("10 / 3 = " + divide.apply(10, 3));
    }

    /**
     * BiConsumer<T, U>: Represents an operation that accepts two input arguments and returns no result
     */
    public static void biConsumerExamples() {
        System.out.println("\n=== BiConsumer Examples ===");

        BiConsumer<String, Integer> printWithCount = (s, count) -> {
            for (int i = 0; i < count; i++) {
                System.out.println(s);
            }
        };

        BiConsumer<String, String> printPair = (s1, s2) ->
                System.out.println("First: " + s1 + ", Second: " + s2);

        printWithCount.accept("Hello", 3);
        printPair.accept("Alice", "Bob");
    }

    /**
     * BiPredicate<T, U>: Represents a predicate of two arguments
     */
    public static void biPredicateExamples() {
        System.out.println("\n=== BiPredicate Examples ===");

        BiPredicate<String, Integer> isLengthEqual = (s, len) -> s.length() == len;
        BiPredicate<Integer, Integer> isGreater = (a, b) -> a > b;

        System.out.println("Is 'Hello' length 5? " + isLengthEqual.test("Hello", 5));
        System.out.println("Is 10 > 5? " + isGreater.test(10, 5));
    }

    /**
     * UnaryOperator<T>: Represents an operation on a single operand that produces a result of the same type
     */
    public static void unaryOperatorExamples() {
        System.out.println("\n=== UnaryOperator Examples ===");

        UnaryOperator<Integer> square = n -> n * n;
        UnaryOperator<String> toUpper = String::toUpperCase;

        System.out.println("Square of 5: " + square.apply(5));
        System.out.println("Upper of 'hello': " + toUpper.apply("hello"));
    }

    /**
     * BinaryOperator<T>: Represents an operation upon two operands of the same type, producing a result of the same type
     */
    public static void binaryOperatorExamples() {
        System.out.println("\n=== BinaryOperator Examples ===");

        BinaryOperator<Integer> max = (a, b) -> a > b ? a : b;
        BinaryOperator<Integer> min = (a, b) -> a < b ? a : b;
        BinaryOperator<String> concat = (s1, s2) -> s1 + s2;

        System.out.println("Max of 5 and 10: " + max.apply(5, 10));
        System.out.println("Min of 5 and 10: " + min.apply(5, 10));
        System.out.println("Concat 'Hello' and ' World': " + concat.apply("Hello", " World"));
    }

    public static void main(String[] args) {
        System.out.println("=== Custom Functional Interface Examples ===");
        customFunctionalInterfaceExamples();

        predicateExamples();
        functionExamples();
        consumerExamples();
        supplierExamples();
        biFunctionExamples();
        biConsumerExamples();
        biPredicateExamples();
        unaryOperatorExamples();
        binaryOperatorExamples();
    }
}