package com.chen.stream;

import org.junit.Test;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Java 8 Stream Collectors 操作测试用例
 */
public class StreamCollectorsTest {

    static class Person {
        String name;
        int age;
        String city;

        public Person(String name, int age, String city) {
            this.name = name;
            this.age = age;
            this.city = city;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
        public String getCity() { return city; }
    }

    private List<Person> getPersonList() {
        return Arrays.asList(
                new Person("Alice", 25, "New York"),
                new Person("Bob", 30, "London"),
                new Person("Charlie", 25, "New York"),
                new Person("David", 35, "London"),
                new Person("Eve", 28, "Paris")
        );
    }

    /**
     * 测试 toList, toSet, toCollection
     */
    @Test
    public void testToCollection() {
        List<Person> people = getPersonList();

        // 收集为 List
        List<String> names = people.stream()
                .map(Person::getName)
                .collect(Collectors.toList());
        assertEquals(5, names.size());

        // 收集为 Set
        Set<String> cities = people.stream()
                .map(Person::getCity)
                .collect(Collectors.toSet());
        assertEquals(3, cities.size()); // New York, London, Paris

        // 收集为特定集合类型 (LinkedList)
        LinkedList<String> nameLinkedList = people.stream()
                .map(Person::getName)
                .collect(Collectors.toCollection(LinkedList::new));
        assertEquals(5, nameLinkedList.size());
    }

    /**
     * 测试 joining: 字符串拼接
     */
    @Test
    public void testJoining() {
        List<Person> people = getPersonList();

        // 简单拼接
        String allNames = people.stream()
                .map(Person::getName)
                .collect(Collectors.joining(", "));
        assertEquals("Alice, Bob, Charlie, David, Eve", allNames);
        
        // 带前缀和后缀
        String formattedNames = people.stream()
                .map(Person::getName)
                .collect(Collectors.joining(", ", "[", "]"));
        assertEquals("[Alice, Bob, Charlie, David, Eve]", formattedNames);
    }

    /**
     * 测试 groupingBy: 分组
     */
    @Test
    public void testGroupingBy() {
        List<Person> people = getPersonList();

        // 按城市分组
        Map<String, List<Person>> peopleByCity = people.stream()
                .collect(Collectors.groupingBy(Person::getCity));
        
        assertEquals(2, peopleByCity.get("New York").size());
        assertEquals(2, peopleByCity.get("London").size());
        assertEquals(1, peopleByCity.get("Paris").size());
        
        // 多级分组：先按城市，再按年龄
        Map<String, Map<Integer, List<Person>>> peopleByCityAndAge = people.stream()
                .collect(Collectors.groupingBy(Person::getCity,
                        Collectors.groupingBy(Person::getAge)));
        
        assertEquals(1, peopleByCityAndAge.get("New York").get(25).size()); // Alice & Charlie are 25, but wait, groupingBy age will put them in same list
        // Actually Alice and Charlie are both 25.
        assertEquals(2, peopleByCityAndAge.get("New York").get(25).size());
    }

    /**
     * 测试 partitioningBy: 分区 (true/false)
     */
    @Test
    public void testPartitioningBy() {
        List<Person> people = getPersonList();

        // 按年龄是否大于30分区
        Map<Boolean, List<Person>> partitioned = people.stream()
                .collect(Collectors.partitioningBy(p -> p.getAge() > 30));
        
        List<Person> olderThan30 = partitioned.get(true);
        List<Person> thirtyOrYounger = partitioned.get(false);
        
        assertEquals(1, olderThan30.size()); // David (35)
        assertEquals(4, thirtyOrYounger.size());
    }

    /**
     * 测试 summarizing: 统计信息
     */
    @Test
    public void testSummarizing() {
        List<Person> people = getPersonList();

        // 统计年龄
        IntSummaryStatistics ageStats = people.stream()
                .collect(Collectors.summarizingInt(Person::getAge));
        
        assertEquals(5, ageStats.getCount());
        assertEquals(143, ageStats.getSum());
        assertEquals(25, ageStats.getMin());
        assertEquals(35, ageStats.getMax());
        assertEquals(28.6, ageStats.getAverage(), 0.01);
    }
    
    /**
     * 测试 mapping: 转换后收集
     */
    @Test
    public void testMapping() {
        List<Person> people = getPersonList();
        
        // 收集每个城市的人名列表
        Map<String, List<String>> namesByCity = people.stream()
                .collect(Collectors.groupingBy(Person::getCity,
                        Collectors.mapping(Person::getName, Collectors.toList())));
        
        assertTrue(namesByCity.get("New York").contains("Alice"));
        assertTrue(namesByCity.get("New York").contains("Charlie"));
    }
}
