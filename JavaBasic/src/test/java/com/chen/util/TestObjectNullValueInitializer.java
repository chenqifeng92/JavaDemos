package com.chen.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Optional;

public class TestObjectNullValueInitializer {
    private TestObject testObj;

    @BeforeEach
    void setUp() {
        testObj = new TestObject();
        testObj.stringField = "test";
        testObj.intValue = 123;
        testObj.boolValue = true;
    }

    @Test
    void testFillDefaultValues() throws IllegalAccessException {
        // 确保某些字段有值
        assertNotNull(testObj.stringField);
        assertTrue(testObj.intValue != 0);
        assertTrue(testObj.boolValue);

        // 调用方法
        ObjectNullValueInitializer.fillDefaultValues(testObj);

        // 检查所有字段是否都被设置为null
        assertNull(testObj.stringField);
        assertNull(testObj.intValue);
        assertNull(testObj.boolValue);
    }

    @Test
    void testSplitSpaceStrToArrWithValidInput() {
        String input = "Sales Marketing Tech";
        String[] expected = {"Sales", "Marketing", "Tech"};

        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testSplitSpaceStrToArrWithBracketInput() {
        String input = "[Sales Marketing Tech]";
        String[] expected = {"Sales", "Marketing", "Tech"};

        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testSplitSpaceStrToArrWithNullInput() {
        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr(null);
        assertTrue(result.length == 0);
    }

    @Test
    void testSplitSpaceStrToArrWithEmptyString() {
        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr("");
        assertTrue(result.length == 0);
    }

    @Test
    void testSplitSpaceStrToArrWithNullString() {
        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr("null");
        assertTrue(result.length == 0);
    }

    @Test
    void testSplitSpaceStrToArrWithBracketString() {
        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr("[]");
        assertTrue(result.length == 0);
    }

    private static class TestObject {
        String stringField;
        int intValue;
        boolean boolValue;

        public String getStringField() {
            return stringField;
        }

        public int getIntValue() {
            return intValue;
        }

        public boolean isBoolValue() {
            return boolValue;
        }
    }
}
