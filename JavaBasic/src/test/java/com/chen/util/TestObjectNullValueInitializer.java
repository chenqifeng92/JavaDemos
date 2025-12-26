package com.chen.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ObjectNullValueInitializer 工具类的单元测试
 */
@DisplayName("ObjectNullValueInitializer 工具类测试")
class TestObjectNullValueInitializer {

    /**
     * 测试 fillDefaultValues 方法：当对象字段为 String 类型时
     * 验证：字段值应保持不变或被正确处理（当前逻辑下，非空值保持不变，null 值保持 null）
     */
    @Test
    @DisplayName("fillDefaultValues: 正常对象处理")
    void testFillDefaultValues_WithTestBean() {
        // 准备测试数据
        StringBean bean = new StringBean();
        bean.setField1("value1");
        bean.setField2(null);

        // 执行测试方法
        ObjectNullValueInitializer.fillDefaultValues(bean);

        // 验证结果
        assertEquals("value1", bean.getField1(), "非空字段不应被修改");
        assertNull(bean.getField2(), "null 字段应保持为 null");
    }

    /**
     * 测试 fillDefaultValues 方法：当输入对象为 null 时
     * 验证：应抛出 NullPointerException
     */
    @Test
    @DisplayName("fillDefaultValues: 输入为 null 时抛出异常")
    void testFillDefaultValues_WhenObjectIsNull_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            ObjectNullValueInitializer.fillDefaultValues(null);
        }, "输入 null 时应抛出 NullPointerException");
    }

    /**
     * 测试 splitSpaceStrToArr 方法：正常空格分隔字符串
     * 验证：应正确分割为字符串数组
     */
    @Test
    @DisplayName("splitSpaceStrToArr: 正常空格分隔字符串")
    void testSplitSpaceStrToArr_WithValidInput() {
        String input = "Sales Marketing Tech";
        String[] expected = {"Sales", "Marketing", "Tech"};
        assertArrayEquals(expected, ObjectNullValueInitializer.splitSpaceStrToArr(input), "应按空格正确分割字符串");
    }

    /**
     * 测试 splitSpaceStrToArr 方法：带方括号的字符串
     * 验证：应去除方括号后正确分割
     */
    @Test
    @DisplayName("splitSpaceStrToArr: 带方括号的字符串")
    void testSplitSpaceStrToArr_WithBracketInput() {
        String input = "[Sales Marketing Tech]";
        String[] expected = {"Sales", "Marketing", "Tech"};
        assertArrayEquals(expected, ObjectNullValueInitializer.splitSpaceStrToArr(input), "应去除首尾方括号并分割");
    }

    /**
     * 测试 splitSpaceStrToArr 方法：各种无效输入
     * 验证：应返回空数组
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"[]", "null"})
    @DisplayName("splitSpaceStrToArr: 无效输入返回空数组")
    void testSplitSpaceStrToArr_WithInvalidInput(String input) {
        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr(input);
        assertNotNull(result, "结果不应为 null");
        assertEquals(0, result.length, "无效输入应返回空数组");
    }
    
    /**
     * 测试 splitSpaceStrToArr 方法：仅包含空格的字符串
     * 验证：应返回空数组（基于 split 的默认行为）
     */
    @Test
    @DisplayName("splitSpaceStrToArr: 仅包含空格")
    void testSplitSpaceStrToArr_WithOnlySpaces() {
        String input = " ";
        String[] result = ObjectNullValueInitializer.splitSpaceStrToArr(input);
        assertEquals(0, result.length, "仅包含空格的字符串 split 后应为空数组"); 
    }

    /**
     * 辅助测试类：仅包含 String 字段
     * 用于避免 ObjectNullValueInitializer 中强制类型转换导致的 ClassCastException
     */
    static class StringBean {
        private String field1;
        private String field2;

        public String getField1() { return field1; }
        public void setField1(String field1) { this.field1 = field1; }
        public String getField2() { return field2; }
        public void setField2(String field2) { this.field2 = field2; }
    }
}
