package org.blackdread.filtersortjooqapi.filter.parser;

import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FilterParserByteTest {

    @Test
    void parse() {
        Assertions.assertEquals(10, (byte) FilterParser.ofByte().parse("10"));
        Assertions.assertEquals(0x0F, (byte) FilterParser.ofByte().parse("15"));
        Assertions.assertEquals(127, (byte) FilterParser.ofByte().parse("127"));
        Assertions.assertEquals(0, (byte) FilterParser.ofByte().parse("0"));
        Assertions.assertEquals(1, (byte) FilterParser.ofByte().parse("1"));
    }

    @Test
    void parseThrows() {
        Assertions.assertThrows(NumberFormatException.class, () -> FilterParser.ofByte().parse("0xFF"));
        Assertions.assertThrows(NumberFormatException.class, () -> FilterParser.ofByte().parse("0xF"));
        Assertions.assertThrows(NumberFormatException.class, () -> FilterParser.ofByte().parse("AFA"));
        Assertions.assertThrows(NumberFormatException.class, () -> FilterParser.ofByte().parse("Odwdw0"));
        Assertions.assertThrows(NumberFormatException.class, () -> FilterParser.ofByte().parse("OxFFFF"));
        Assertions.assertThrows(NumberFormatException.class, () -> FilterParser.ofByte().parse(" 5   "));
    }

    @Test
    void parseWithApiException() {
        Assertions.assertEquals(5, (byte) FilterParser.ofByte().parseWithApiException("5"));
        Assertions.assertEquals(-5, (byte) FilterParser.ofByte().parseWithApiException("-5"));
        Assertions.assertEquals(0, (byte) FilterParser.ofByte().parseWithApiException("0"));
        Assertions.assertEquals(66, (byte) FilterParser.ofByte().parseWithApiException("066"));
        Assertions.assertEquals(1, (byte) FilterParser.ofByte().parseWithApiException("000001"));
    }

    @Test
    void parseWithApiExceptionThrows() {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofByte().parseWithApiException("AFA"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofByte().parseWithApiException("FBC"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofByte().parseWithApiException("O"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofByte().parseWithApiException(" 5   "));
    }

    @Test
    void parseWithApiExceptionWithCustomParser() {
        Assertions.assertEquals(9, (byte) FilterParser.ofByte().parseWithApiException("5", val -> (byte) 0x9));
        Assertions.assertEquals(9, (byte) FilterParser.ofByte().parseWithApiException("-5", val -> (byte) 0x9));
        Assertions.assertEquals(9, (byte) FilterParser.ofByte().parseWithApiException("tybnksd", val -> (byte) 0x9));
        Assertions.assertThrows(FilteringApiException.class,
            () -> FilterParser.ofByte().parseWithApiException("tybnksd", Byte::valueOf));
    }

    @Test
    void parseOrDefault() {
        final byte result = FilterParser.ofByte().parseOrDefault("5", (byte) 0xA);
        Assertions.assertEquals(5, result);

        final byte result2 = FilterParser.ofByte().parseOrDefault("55", (byte) 0xA);
        Assertions.assertEquals(55, result2);

        final byte result3 = FilterParser.ofByte().parseOrDefault("-47", (byte) 0xA);
        Assertions.assertEquals(-47, result3);
    }

    @Test
    void parseOrDefaultWithBadInput() {
        final byte result = FilterParser.ofByte().parseOrDefault("--5", (byte) 0xA);
        Assertions.assertEquals((byte) 0xA, result);

        final byte result2 = FilterParser.ofByte().parseOrDefault("F", (byte) 0xA);
        Assertions.assertEquals((byte) 0xA, result2);

        // Might use trim later by default
        final byte result3 = FilterParser.ofByte().parseOrDefault("    8     ", (byte) 0xA);
        Assertions.assertEquals((byte) 0xA, result3);
    }

    @Test
    void parseOrDefaultWithSupplier() {
        final byte result = FilterParser.ofByte().parseOrDefault("5", () -> (byte) 0xA);
        Assertions.assertEquals(5, result);

        final byte result2 = FilterParser.ofByte().parseOrDefault("-12", () -> (byte) 0xA);
        Assertions.assertEquals(-12, result2);

        final byte result3 = FilterParser.ofByte().parseOrDefault("0000", () -> (byte) 0xA);
        Assertions.assertEquals(0, result3);
    }

    @Test
    void parseOrDefaultWithSupplierBadInput() {
        final byte result = FilterParser.ofByte().parseOrDefault("--5", () -> (byte) 0xA);
        Assertions.assertEquals((byte) 0xA, result);

        final byte result2 = FilterParser.ofByte().parseOrDefault("F", () -> (byte) 0xA);
        Assertions.assertEquals((byte) 0xA, result2);

        final byte result3 = FilterParser.ofByte().parseOrDefault("   8   ", () -> (byte) 0xA);
        Assertions.assertEquals((byte) 0xA, result3);
    }
}
