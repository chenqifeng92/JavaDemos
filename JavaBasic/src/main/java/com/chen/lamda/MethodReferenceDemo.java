package com.chen.lamda;

import java.util.*;
import java.util.stream.Collectors;
import java.util.function.*;

/**
 * Method Reference Demo
 * Demonstrates different types of method references in Java 8
 */
public class MethodReferenceDemo {

    static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public Person() {
            this("Unknown", 0);
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public void printInfo() {
            System.out.println("Person: " + name + ", Age: " + age);
        }

        public static int compareByAge(Person p1, Person p2) {
            return Integer.compare(p1.age, p2.age);
        }

        public int compareByName(Person other) {
            return this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return String.format("Person{name='%s', age=%d}", name, age);
        }
    }

    /**
     * 1. Reference to a static method
     * Syntax: ClassName::staticMethodName
     */
    public static void staticMethodReference() {
        System.out.println("=== Static Method Reference ===");

        // Lambda: x -> Integer.parseInt(x)
        // Method reference: Integer::parseInt
        Function<String, Integer> parser1 = x -> Integer.parseInt(x);
        Function<String, Integer> parser2 = Integer::parseInt;
        System.out.println("Parse '123': " + parser2.apply("123"));

        // Lambda: (x, y) -> Math.max(x, y)
        // Method reference: Math::max
        BinaryOperator<Integer> max1 = (x, y) -> Math.max(x, y);
        BinaryOperator<Integer> max2 = Math::max;
        System.out.println("Max of 5 and 10: " + max2.apply(5, 10));

        // Lambda: str -> System.out.println(str)
        // Method reference: System.out::println
        Consumer<String> printer1 = str -> System.out.println(str);
        Consumer<String> printer2 = System.out::println;
        printer2.accept("Hello Method Reference!");

        // Sorting with static method reference
        List<Person> people = Arrays.asList(
                new Person("Alice", 30),
                new Person("Bob", 25),
                new Person("Charlie", 35)
        );
        people.sort(Person::compareByAge);
        System.out.println("Sorted by age: " + people);
    }

    /**
     * 2. Reference to an instance method of a particular object
     * Syntax: instance::instanceMethodName
     */
    public static void instanceMethodReferenceOfParticularObject() {
        System.out.println("\n=== Instance Method Reference (Particular Object) ===");

        String text = "Hello World";

        // Lambda: () -> text.length()
        // Method reference: text::length
        Supplier<Integer> lengthSupplier1 = () -> text.length();
        Supplier<Integer> lengthSupplier2 = text::length;
        System.out.println("Length: " + lengthSupplier2.get());

        // Lambda: () -> text.toUpperCase()
        // Method reference: text::toUpperCase
        Supplier<String> upperCase1 = () -> text.toUpperCase();
        Supplier<String> upperCase2 = text::toUpperCase;
        System.out.println("Upper case: " + upperCase2.get());

        Person person = new Person("Alice", 25);
        // Lambda: () -> person.getName()
        // Method reference: person::getName
        Supplier<String> nameSupplier1 = () -> person.getName();
        Supplier<String> nameSupplier2 = person::getName;
        System.out.println("Name: " + nameSupplier2.get());

        // Lambda: () -> person.printInfo()
        // Method reference: person::printInfo
        Runnable printInfo1 = () -> person.printInfo();
        Runnable printInfo2 = person::printInfo;
        printInfo2.run();
    }

    /**
     * 3. Reference to an instance method of an arbitrary object of a particular type
     * Syntax: ClassName::instanceMethodName
     */
    public static void instanceMethodReferenceOfArbitraryObject() {
        System.out.println("\n=== Instance Method Reference (Arbitrary Object) ===");

        // Lambda: str -> str.length()
        // Method reference: String::length
        Function<String, Integer> length1 = str -> str.length();
        Function<String, Integer> length2 = String::length;
        System.out.println("Length of 'Hello': " + length2.apply("Hello"));

        // Lambda: str -> str.toUpperCase()
        // Method reference: String::toUpperCase
        Function<String, String> upperCase1 = str -> str.toUpperCase();
        Function<String, String> upperCase2 = String::toUpperCase;
        System.out.println("Upper: " + upperCase2.apply("hello"));

        // Lambda: (s1, s2) -> s1.compareTo(s2)
        // Method reference: String::compareTo
        BiFunction<String, String, Integer> comparator1 = (s1, s2) -> s1.compareTo(s2);
        BiFunction<String, String, Integer> comparator2 = String::compareTo;
        System.out.println("Compare 'apple' to 'banana': " + comparator2.apply("apple", "banana"));

        // Sorting strings
        List<String> words = Arrays.asList("cherry", "apple", "banana", "date");
        words.sort(String::compareTo);
        System.out.println("Sorted words: " + words);

        // Using with streams
        List<String> names = Arrays.asList("alice", "bob", "charlie");
        names.stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    /**
     * 4. Reference to a constructor
     * Syntax: ClassName::new
     */
    public static void constructorReference() {
        System.out.println("\n=== Constructor Reference ===");

        // Lambda: () -> new ArrayList<>()
        // Constructor reference: ArrayList::new
        Supplier<List<String>> listSupplier1 = () -> new ArrayList<>();
        Supplier<List<String>> listSupplier2 = ArrayList::new;
        List<String> list = listSupplier2.get();
        System.out.println("New list: " + list);

        // Lambda: () -> new Person()
        // Constructor reference: Person::new
        Supplier<Person> personSupplier1 = () -> new Person();
        Supplier<Person> personSupplier2 = Person::new;
        Person person = personSupplier2.get();
        System.out.println("New person: " + person);

        // Lambda: (name, age) -> new Person(name, age)
        // Constructor reference: Person::new
        BiFunction<String, Integer, Person> personFactory1 = (name, age) -> new Person(name, age);
        BiFunction<String, Integer, Person> personFactory2 = Person::new;
        Person alice = personFactory2.apply("Alice", 25);
        System.out.println("Created person: " + alice);

        // Creating list of persons using constructor reference
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<Integer> ages = Arrays.asList(25, 30, 35);
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            people.add(personFactory2.apply(names.get(i), ages.get(i)));
        }
        System.out.println("People: " + people);
    }

    /**
     * Array constructor reference
     * Syntax: Type[]::new
     */
    public static void arrayConstructorReference() {
        System.out.println("\n=== Array Constructor Reference ===");

        // Lambda: size -> new String[size]
        // Array constructor reference: String[]::new
        IntFunction<String[]> arrayCreator1 = size -> new String[size];
        IntFunction<String[]> arrayCreator2 = String[]::new;
        String[] array = arrayCreator2.apply(5);
        System.out.println("Created array of length: " + array.length);

        // Using with streams
        List<String> words = Arrays.asList("apple", "banana", "cherry");
        String[] wordArray = words.stream().toArray(String[]::new);
        System.out.println("Array from stream: " + Arrays.toString(wordArray));

        // Person array
        IntFunction<Person[]> personArrayCreator = Person[]::new;
        Person[] personArray = personArrayCreator.apply(3);
        System.out.println("Person array length: " + personArray.length);
    }

    /**
     * Practical examples combining different method references
     */
    public static void practicalExamples() {
        System.out.println("\n=== Practical Examples ===");

        List<String> names = Arrays.asList("Alice", "bob", "CHARLIE", "David", "eve");

        // Chain of method references
        List<String> processed = names.stream()
                .map(String::toLowerCase)
                .map(String::toUpperCase)
                .sorted(String::compareTo)
                .collect(Collectors.toList());
        System.out.println("Processed: " + processed);

        // Multiple transformations
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Integer sum = numbers.stream()
                .map(Object::toString)
                .map(String::length)
                .reduce(0, Integer::sum);
        System.out.println("Sum of string lengths: " + sum);

        // Working with Person objects
        List<Person> people = Arrays.asList(
                new Person("Alice", 30),
                new Person("Bob", 25),
                new Person("Charlie", 35),
                new Person("David", 28)
        );

        // Get names sorted
        List<String> sortedNames = people.stream()
                .map(Person::getName)
                .sorted(String::compareTo)
                .collect(Collectors.toList());
        System.out.println("Sorted names: " + sortedNames);

        // Print all persons
        System.out.println("All persons:");
        people.forEach(Person::printInfo);
    }

    /**
     * Comparing lambdas vs method references
     */
    public static void lambdaVsMethodReference() {
        System.out.println("\n=== Lambda vs Method Reference ===");

        List<String> words = Arrays.asList("apple", "banana", "cherry");

        // Using lambda
        System.out.println("Using lambda:");
        words.stream()
                .map(s -> s.toUpperCase())
                .forEach(s -> System.out.println(s));

        // Using method reference (more concise)
        System.out.println("Using method reference:");
        words.stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    public static void main(String[] args) {
        staticMethodReference();
        instanceMethodReferenceOfParticularObject();
        instanceMethodReferenceOfArbitraryObject();
        constructorReference();
        arrayConstructorReference();
        practicalExamples();
        lambdaVsMethodReference();
    }
}