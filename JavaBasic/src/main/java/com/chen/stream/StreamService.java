package com.chen.stream;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 提供各种 Stream 操作的服务类
 */
public class StreamService {

    /**
     * 过滤出偶数
     */
    public List<Integer> filterEvens(List<Integer> numbers) {
        if (numbers == null) return Collections.emptyList();
        return numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
    }

    /**
     * 将字符串列表转换为大写
     */
    public List<String> toUpperCase(List<String> names) {
        if (names == null) return Collections.emptyList();
        return names.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    /**
     * 扁平化嵌套列表
     */
    public List<String> flattenList(List<List<String>> nestedList) {
        if (nestedList == null) return Collections.emptyList();
        return nestedList.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * 对列表进行排序
     * @param ascending true 为升序，false 为降序
     */
    public List<Integer> sortList(List<Integer> numbers, boolean ascending) {
        if (numbers == null) return Collections.emptyList();
        Stream<Integer> stream = numbers.stream();
        if (ascending) {
            return stream.sorted().collect(Collectors.toList());
        } else {
            return stream.sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        }
    }

    /**
     * 获取去重后的列表
     */
    public List<Integer> getDistinctList(List<Integer> numbers) {
        if (numbers == null) return Collections.emptyList();
        return numbers.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 截断和跳过元素
     */
    public List<Integer> getLimitSkip(List<Integer> numbers, int limit, int skip) {
        if (numbers == null) return Collections.emptyList();
        return numbers.stream()
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 计算列表元素之和
     */
    public Integer sum(List<Integer> numbers) {
        if (numbers == null) return 0;
        return numbers.stream()
                .reduce(0, Integer::sum);
    }

    /**
     * 检查是否所有数字都是偶数
     */
    public boolean areAllEven(List<Integer> numbers) {
        if (numbers == null) return false;
        return numbers.stream().allMatch(n -> n % 2 == 0);
    }

    /**
     * 查找第一个元素
     */
    public Optional<Integer> findFirst(List<Integer> numbers) {
        if (numbers == null) return Optional.empty();
        return numbers.stream().findFirst();
    }

    /**
     * 查找任意一个元素
     */
    public Optional<Integer> findAny(List<Integer> numbers) {
        if (numbers == null) return Optional.empty();
        return numbers.stream().findAny();
    }

    /**
     * 获取名字列表
     */
    public List<String> getNames(List<Person> people) {
        if (people == null) return Collections.emptyList();
        return people.stream()
                .map(Person::getName)
                .collect(Collectors.toList());
    }

    /**
     * 获取名字列表 (LinkedList)
     */
    public LinkedList<String> getNamesAsLinkedList(List<Person> people) {
        if (people == null) return new LinkedList<>();
        return people.stream()
                .map(Person::getName)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * 获取城市集合
     */
    public Set<String> getCities(List<Person> people) {
        if (people == null) return Collections.emptySet();
        return people.stream()
                .map(Person::getCity)
                .collect(Collectors.toSet());
    }

    /**
     * 将名字拼接成字符串
     */
    public String joinNames(List<Person> people) {
        if (people == null) return "";
        return people.stream()
                .map(Person::getName)
                .collect(Collectors.joining(", "));
    }

    /**
     * 按城市分组
     */
    public Map<String, List<Person>> groupByCity(List<Person> people) {
        if (people == null) return Collections.emptyMap();
        return people.stream()
                .collect(Collectors.groupingBy(Person::getCity));
    }

    /**
     * 按城市分组，并只收集名字
     */
    public Map<String, List<String>> getNamesByCity(List<Person> people) {
        if (people == null) return Collections.emptyMap();
        return people.stream()
                .collect(Collectors.groupingBy(Person::getCity,
                        Collectors.mapping(Person::getName, Collectors.toList())));
    }

    /**
     * 按年龄是否大于指定值分区
     */
    public Map<Boolean, List<Person>> partitionByAge(List<Person> people, int ageThreshold) {
        if (people == null) return Collections.emptyMap();
        return people.stream()
                .collect(Collectors.partitioningBy(p -> p.getAge() > ageThreshold));
    }

    /**
     * 统计年龄信息
     */
    public IntSummaryStatistics getAgeStatistics(List<Person> people) {
        if (people == null) return new IntSummaryStatistics();
        return people.stream()
                .collect(Collectors.summarizingInt(Person::getAge));
    }

    /**
     * 计算平均年龄
     */
    public double getAverageAge(List<Person> people) {
        if (people == null) return 0.0;
        return people.stream()
                .mapToInt(Person::getAge)
                .average()
                .orElse(0.0);
    }

    /**
     * 统计文本中单词出现的频率
     */
    public Map<String, Long> getWordFrequency(String text) {
        if (text == null || text.isEmpty()) return Collections.emptyMap();
        return Arrays.stream(text.split("\\s+"))
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * 使用并行流计算偶数个数
     */
    public long countEvensParallel(List<Integer> numbers) {
        if (numbers == null) return 0;
        return numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .count();
    }
    
    /**
     * 生成指定范围的偶数列表
     */
    public List<Integer> generateEvens(int limit) {
        return Stream.iterate(0, n -> n + 2)
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 调试流 (peek 示例)
     */
    public List<String> debugStream(List<String> input) {
        if (input == null) return Collections.emptyList();
        return input.stream()
                .filter(s -> s.length() > 3)
                .peek(s -> System.out.println("Filtered: " + s))
                .map(String::toUpperCase)
                .peek(s -> System.out.println("Mapped: " + s))
                .collect(Collectors.toList());
    }
}
