package com.chen.util;

import com.chen.util.ObjectNullValueInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.StringJoiner;

class ObjectNullValueInitializerTest {

//    @Test
//    @DisplayName("fillDefaultValues测试：当对象字段存在时，设置所有字段为null")
//    void testFillDefaultValues_WhenObjectHasFields_SetsAllFieldsToNull() {
//        TestBean testBean = new TestBean();
//        testBean.setName("test");
//        testBean.setAge(10);
//        testBean.setActive(true);
//
//        ObjectNullValueInitializer.fillDefaultValues(testBean);
//
//        assertNull(testBean.getName());
//        assertNull(testBean.getAge());
//        assertNull(testBean.getActive());
//    }

    @Test
    @DisplayName("fillDefaultValues测试：当对象为null时，抛出NullPointerException")
    void testFillDefaultValues_WhenObjectIsNull_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            ObjectNullValueInitializer.fillDefaultValues(null);
        });
    }

    @Test
    @DisplayName("splitSpaceStrToArr测试：正常输入")
    void testSplitSpaceStrToArr_WithValidInput_ReturnsCorrectArray() {
        String input = "Sales Marketing Tech";
        String[] expected = {"Sales", "Marketing", "Tech"};

        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr(input);

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("splitSpaceStrToArr测试：空输入")
    void testSplitSpaceStrToArr_WithEmptyInput_ReturnsEmptyArray() {
        String input = "";
        String[] expected = {};

        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr(input);

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("splitSpaceStrToArr测试：null输入")
    void testSplitSpaceStrToArr_WithNullInput_ReturnsEmptyArray() {
        String input = null;
        String[] expected = {};

        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr(input);

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("splitSpaceStrToArr测试：包含方括号的输入")
    void testSplitSpaceStrToArr_WithBracketInput_ReturnsCorrectArray() {
        String input = "[Sales Marketing Tech]";
        String[] expected = {"Sales", "Marketing", "Tech"};

        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr(input);

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("splitSpaceStrToArr测试：特殊字符串输入")
    void testSplitSpaceStrToArr_WithSpecialStringInput_ReturnsEmptyArray() {
        String input = "null";
        String[] expected = {};

        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr(input);

        assertArrayEquals(expected, result);

        input = "[]";
        result = ObjectNullValueInitializer.splitSpaceStrToArr(input);
        assertArrayEquals(expected, result);
    }
}

class TestBean {
    private String name;
    private Integer age;
    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}