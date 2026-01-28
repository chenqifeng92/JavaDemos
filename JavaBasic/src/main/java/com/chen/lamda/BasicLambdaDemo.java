package com.chen.lamda;

import java.util.*;
import java.util.function.*;

/**
 * Basic Lambda Expressions Demo
 * Demonstrates basic lambda syntax and usage in Java 8
 */
public class BasicLambdaDemo {

    /**
     * Basic lambda syntax examples
     */
    public static void lambdaSyntaxExamples() {
        // 1. No parameters lambda
        Runnable r1 = () -> System.out.println("Hello Lambda!");
        r1.run();

        // 2. Single parameter lambda (parentheses optional)
        Consumer<String> printer = s -> System.out.println(s);
        printer.accept("Single parameter");

        // 3. Multiple parameters lambda
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println("Add: " + add.apply(5, 3));

        // 4. Lambda with block body
        BiFunction<Integer, Integer, Integer> multiply = (a, b) -> {
            int result = a * b;
            System.out.println("Multiplying " + a + " * " + b);
            return result;
        };
        System.out.println("Multiply: " + multiply.apply(4, 5));

        // 5. Lambda with explicit type declarations
        BiFunction<String, String, Integer> compare = (String s1, String s2) -> s1.compareTo(s2);
        System.out.println("Compare: " + compare.apply("apple", "banana"));
    }

    /**
     * Using lambdas with collections
     */
    public static void lambdaWithCollections() {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

        // forEach with lambda
        System.out.println("Names:");
        names.forEach(name -> System.out.println("  " + name));

        // Sort with lambda
        List<String> sortedNames = new ArrayList<>(names);
        sortedNames.sort((s1, s2) -> s1.compareTo(s2));
        System.out.println("Sorted: " + sortedNames);

        // removeIf with lambda
        List<String> filtered = new ArrayList<>(names);
        filtered.removeIf(name -> name.length() < 4);
        System.out.println("Filtered (length >= 4): " + filtered);
    }

    /**
     * Predicate examples
     */
    public static List<Integer> filterNumbers(List<Integer> numbers, Predicate<Integer> predicate) {
        List<Integer> result = new ArrayList<>();
        for (Integer num : numbers) {
            if (predicate.test(num)) {
                result.add(num);
            }
        }
        return result;
    }

    /**
     * Function examples
     */
    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        List<R> result = new ArrayList<>();
        for (T item : list) {
            result.add(mapper.apply(item));
        }
        return result;
    }

    /**
     * Consumer examples
     */
    public static <T> void process(List<T> list, Consumer<T> consumer) {
        for (T item : list) {
            consumer.accept(item);
        }
    }

    /**
     * Supplier examples
     */
    public static <T> T getOrDefault(Supplier<T> supplier) {
        return supplier.get();
    }

    /**
     * Lambda composition examples
     */
    public static void lambdaComposition() {
        // Predicate composition
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);

        System.out.println("Is 4 even and positive? " + isEvenAndPositive.test(4));
        System.out.println("Is -4 even and positive? " + isEvenAndPositive.test(-4));

        // Function composition
        Function<Integer, Integer> multiplyBy2 = x -> x * 2;
        Function<Integer, Integer> add10 = x -> x + 10;
        Function<Integer, Integer> multiplyThenAdd = multiplyBy2.andThen(add10);
        Function<Integer, Integer> addThenMultiply = multiplyBy2.compose(add10);

        System.out.println("Multiply 5 by 2 then add 10: " + multiplyThenAdd.apply(5)); // (5*2)+10 = 20
        System.out.println("Add 10 to 5 then multiply by 2: " + addThenMultiply.apply(5)); // (5+10)*2 = 30
    }

    /**
     * Capturing variables in lambda (effectively final)
     */
    public static void variableCapture() {
        String prefix = "Hello, ";
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        // Lambda captures the 'prefix' variable
        names.forEach(name -> System.out.println(prefix + name));

        // This would cause compilation error - captured variable must be effectively final
        // prefix = "Hi, ";
    }

    public static void main(String[] args) {
        System.out.println("=== Lambda Syntax Examples ===");
        lambdaSyntaxExamples();

        System.out.println("\n=== Lambda with Collections ===");
        lambdaWithCollections();

        System.out.println("\n=== Predicate Example ===");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> evenNumbers = filterNumbers(numbers, n -> n % 2 == 0);
        System.out.println("Even numbers: " + evenNumbers);

        System.out.println("\n=== Function Example ===");
        List<String> strings = Arrays.asList("1", "2", "3", "4", "5");
        List<Integer> integers = map(strings, Integer::parseInt);
        System.out.println("Parsed integers: " + integers);

        System.out.println("\n=== Consumer Example ===");
        List<String> items = Arrays.asList("Apple", "Banana", "Cherry");
        process(items, item -> System.out.println("Processing: " + item));

        System.out.println("\n=== Supplier Example ===");
        String defaultValue = getOrDefault(() -> "Default Value");
        System.out.println("Supplier result: " + defaultValue);

        System.out.println("\n=== Lambda Composition ===");
        lambdaComposition();

        System.out.println("\n=== Variable Capture ===");
        variableCapture();
    }
}