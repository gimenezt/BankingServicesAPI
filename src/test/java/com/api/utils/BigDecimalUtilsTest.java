package com.api.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalUtilsTest {

    @Test
    void testIsLessThan() {
        assertTrue(BigDecimalUtils.isLessThan(new BigDecimal("5"), new BigDecimal("10")));
        assertFalse(BigDecimalUtils.isLessThan(new BigDecimal("10"), new BigDecimal("5")));
        assertFalse(BigDecimalUtils.isLessThan(new BigDecimal("5"), new BigDecimal("5")));
    }

    @Test
    void testAdd() {
        BigDecimal result = BigDecimalUtils.add(new BigDecimal("10.5"), new BigDecimal("4.5"));
        assertEquals(new BigDecimal("15.0"), result);
    }

    @Test
    void testSubtract() {
        BigDecimal result = BigDecimalUtils.subtract(new BigDecimal("10"), new BigDecimal("4"));
        assertEquals(new BigDecimal("6"), result);
    }
}
