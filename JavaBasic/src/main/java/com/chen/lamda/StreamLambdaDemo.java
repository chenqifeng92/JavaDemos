package com.chen.lamda;

import java.util.*;
import java.util.stream.*;

/**
 * Stream API with Lambda Expressions Demo
 * Demonstrates how to use lambdas with Java 8 Stream API
 */
public class StreamLambdaDemo {

    static class Person {
        private String name;
        private int age;
        private String city;
        private double salary;

        public Person(String name, int age, String city, double salary) {
            this.name = name;
            this.age = age;
            this.city = city;
            this.salary = salary;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
        public String getCity() { return city; }
        public double getSalary() { return salary; }

        @Override
        public String toString() {
            return String.format("Person{name='%s', age=%d, city='%s', salary=%.2f}",
                    name, age, city, salary);
        }
    }

    /**
     * Basic stream operations
     */
    public static void basicStreamOperations() {
        System.out.println("=== Basic Stream Operations ===");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // filter
        List<Integer> evenNumbers = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("Even numbers: " + evenNumbers);

        // map
        List<Integer> squares = numbers.stream()
                .map(n -> n * n)
                .collect(Collectors.toList());
        System.out.println("Squares: " + squares);

        // sorted
        List<Integer> sorted = numbers.stream()
                .sorted((a, b) -> b - a) // descending order
                .collect(Collectors.toList());
        System.out.println("Sorted descending: " + sorted);

        // distinct
        List<Integer> duplicates = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
        List<Integer> distinct = duplicates.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Distinct: " + distinct);

        // limit and skip
        List<Integer> limited = numbers.stream()
                .skip(3)
                .limit(5)
                .collect(Collectors.toList());
        System.out.println("Skip 3, limit 5: " + limited);
    }

    /**
     * Terminal operations
     */
    public static void terminalOperations() {
        System.out.println("\n=== Terminal Operations ===");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // forEach
        System.out.print("forEach: ");
        numbers.stream().forEach(n -> System.out.print(n + " "));
        System.out.println();

        // count
        long count = numbers.stream()
                .filter(n -> n > 5)
                .count();
        System.out.println("Count > 5: " + count);

        // anyMatch, allMatch, noneMatch
        boolean anyEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
        System.out.println("Any even? " + anyEven);
        System.out.println("All positive? " + allPositive);
        System.out.println("None negative? " + noneNegative);

        // findFirst, findAny
        Optional<Integer> first = numbers.stream()
                .filter(n -> n > 5)
                .findFirst();
        System.out.println("First > 5: " + first.orElse(null));

        // min, max
        Optional<Integer> min = numbers.stream().min(Integer::compareTo);
        Optional<Integer> max = numbers.stream().max(Integer::compareTo);
        System.out.println("Min: " + min.orElse(null));
        System.out.println("Max: " + max.orElse(null));
    }

    /**
     * Reduce operations
     */
    public static void reduceOperations() {
        System.out.println("\n=== Reduce Operations ===");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // Sum using reduce
        Optional<Integer> sum = numbers.stream()
                .reduce((a, b) -> a + b);
        System.out.println("Sum: " + sum.orElse(0));

        // Sum with initial value
        Integer sumWithInitial = numbers.stream()
                .reduce(0, (a, b) -> a + b);
        System.out.println("Sum with initial: " + sumWithInitial);

        // Product
        Integer product = numbers.stream()
                .reduce(1, (a, b) -> a * b);
        System.out.println("Product: " + product);

        // String concatenation
        List<String> words = Arrays.asList("Hello", "World", "Java", "Lambda");
        String concatenated = words.stream()
                .reduce("", (s1, s2) -> s1 + " " + s2);
        System.out.println("Concatenated:" + concatenated);
    }

    /**
     * Working with complex objects
     */
    public static void complexObjectOperations() {
        System.out.println("\n=== Complex Object Operations ===");

        List<Person> people = Arrays.asList(
                new Person("Alice", 25, "New York", 60000),
                new Person("Bob", 30, "London", 75000),
                new Person("Charlie", 35, "New York", 80000),
                new Person("David", 28, "Paris", 65000),
                new Person("Eve", 32, "London", 70000)
        );

        // Filter by age
        List<Person> over30 = people.stream()
                .filter(p -> p.getAge() > 30)
                .collect(Collectors.toList());
        System.out.println("People over 30: " + over30);

        // Map to names
        List<String> names = people.stream()
                .map(Person::getName)
                .collect(Collectors.toList());
        System.out.println("Names: " + names);

        // Sort by salary
        List<Person> sortedBySalary = people.stream()
                .sorted((p1, p2) -> Double.compare(p1.getSalary(), p2.getSalary()))
                .collect(Collectors.toList());
        System.out.println("Sorted by salary: " + sortedBySalary);

        // Average salary
        double avgSalary = people.stream()
                .mapToDouble(Person::getSalary)
                .average()
                .orElse(0.0);
        System.out.println("Average salary: " + avgSalary);

        // Total salary
        double totalSalary = people.stream()
                .mapToDouble(Person::getSalary)
                .sum();
        System.out.println("Total salary: " + totalSalary);
    }

    /**
     * Grouping and partitioning
     */
    public static void groupingAndPartitioning() {
        System.out.println("\n=== Grouping and Partitioning ===");

        List<Person> people = Arrays.asList(
                new Person("Alice", 25, "New York", 60000),
                new Person("Bob", 30, "London", 75000),
                new Person("Charlie", 35, "New York", 80000),
                new Person("David", 28, "Paris", 65000),
                new Person("Eve", 32, "London", 70000)
        );

        // Group by city
        Map<String, List<Person>> byCity = people.stream()
                .collect(Collectors.groupingBy(Person::getCity));
        System.out.println("Grouped by city: " + byCity);

        // Partition by age
        Map<Boolean, List<Person>> partitionByAge = people.stream()
                .collect(Collectors.partitioningBy(p -> p.getAge() > 30));
        System.out.println("Over 30: " + partitionByAge.get(true));
        System.out.println("30 or under: " + partitionByAge.get(false));

        // Count by city
        Map<String, Long> countByCity = people.stream()
                .collect(Collectors.groupingBy(Person::getCity, Collectors.counting()));
        System.out.println("Count by city: " + countByCity);

        // Average salary by city
        Map<String, Double> avgSalaryByCity = people.stream()
                .collect(Collectors.groupingBy(
                        Person::getCity,
                        Collectors.averagingDouble(Person::getSalary)
                ));
        System.out.println("Average salary by city: " + avgSalaryByCity);
    }

    /**
     * FlatMap examples
     */
    public static void flatMapExamples() {
        System.out.println("\n=== FlatMap Examples ===");

        List<List<Integer>> listOfLists = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5),
                Arrays.asList(6, 7, 8, 9)
        );

        // Flatten list of lists
        List<Integer> flattened = listOfLists.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println("Flattened: " + flattened);

        // Split strings and flatten
        List<String> sentences = Arrays.asList(
                "Hello World",
                "Java Lambda",
                "Stream API"
        );
        List<String> words = sentences.stream()
                .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
                .collect(Collectors.toList());
        System.out.println("Words: " + words);

        // Get unique characters
        String text = "Hello World";
        List<String> uniqueChars = text.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Unique characters: " + uniqueChars);
    }

    /**
     * Parallel streams
     */
    public static void parallelStreamExamples() {
        System.out.println("\n=== Parallel Stream Examples ===");

        List<Integer> numbers = IntStream.rangeClosed(1, 100)
                .boxed()
                .collect(Collectors.toList());

        // Sequential sum
        long startSeq = System.currentTimeMillis();
        int sumSeq = numbers.stream()
                .reduce(0, Integer::sum);
        long endSeq = System.currentTimeMillis();
        System.out.println("Sequential sum: " + sumSeq + " (Time: " + (endSeq - startSeq) + "ms)");

        // Parallel sum
        long startPar = System.currentTimeMillis();
        int sumPar = numbers.parallelStream()
                .reduce(0, Integer::sum);
        long endPar = System.currentTimeMillis();
        System.out.println("Parallel sum: " + sumPar + " (Time: " + (endPar - startPar) + "ms)");
    }

    /**
     * Collectors examples
     */
    public static void collectorsExamples() {
        System.out.println("\n=== Collectors Examples ===");

        List<String> words = Arrays.asList("apple", "banana", "cherry", "date", "elderberry");

        // toList
        List<String> list = words.stream().collect(Collectors.toList());
        System.out.println("toList: " + list);

        // toSet
        Set<String> set = words.stream().collect(Collectors.toSet());
        System.out.println("toSet: " + set);

        // joining
        String joined = words.stream().collect(Collectors.joining(", "));
        System.out.println("joined: " + joined);

        // joining with prefix and suffix
        String joinedWithDelimiters = words.stream()
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("joined with delimiters: " + joinedWithDelimiters);

        // toMap
        Map<String, Integer> wordLengths = words.stream()
                .collect(Collectors.toMap(
                        w -> w,
                        String::length
                ));
        System.out.println("toMap: " + wordLengths);
    }

    public static void main(String[] args) {
        basicStreamOperations();
        terminalOperations();
        reduceOperations();
        complexObjectOperations();
        groupingAndPartitioning();
        flatMapExamples();
        parallelStreamExamples();
        collectorsExamples();
    }
}