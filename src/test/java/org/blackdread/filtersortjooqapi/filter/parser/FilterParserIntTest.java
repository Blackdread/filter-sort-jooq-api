package org.blackdread.filtersortjooqapi.filter.parser;

import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FilterParserIntTest {

    @Test
    void parse() {
        Assertions.assertEquals(5, (int) FilterParser.ofInt().parse("5"));
        Assertions.assertEquals(-5, (int) FilterParser.ofInt().parse("-5"));
        Assertions.assertEquals(0, (int) FilterParser.ofInt().parse("0"));
        Assertions.assertEquals(66, (int) FilterParser.ofInt().parse("066"));
        Assertions.assertEquals(312313, (int) FilterParser.ofInt().parse("312313"));
    }

    @Test
    void parseThrows() {
        Assertions.assertThrows(NumberFormatException.class, () -> FilterParser.ofInt().parse("poiuytd"));
        Assertions.assertThrows(NumberFormatException.class, () -> FilterParser.ofInt().parse("Odwdw0"));
        Assertions.assertThrows(NumberFormatException.class, () -> FilterParser.ofInt().parse("O"));
        Assertions.assertThrows(NumberFormatException.class, () -> FilterParser.ofInt().parse(" 5   "));
    }

    @Test
    void parseWithApiException() {
        Assertions.assertEquals(5, (int) FilterParser.ofInt().parseWithApiException("5"));
        Assertions.assertEquals(-5, (int) FilterParser.ofInt().parseWithApiException("-5"));
        Assertions.assertEquals(0, (int) FilterParser.ofInt().parseWithApiException("0"));
        Assertions.assertEquals(66, (int) FilterParser.ofInt().parseWithApiException("066"));
        Assertions.assertEquals(312313, (int) FilterParser.ofInt().parseWithApiException("312313"));
    }

    @Test
    void parseWithApiExceptionThrows() {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofInt().parseWithApiException("poiuytd"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofInt().parseWithApiException("Odwdw0"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofInt().parseWithApiException("O"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofInt().parseWithApiException(" 5   "));
    }

    @Test
    void parseWithApiExceptionWithCustomParser() {
        Assertions.assertEquals(9, (int) FilterParser.ofInt().parseWithApiException("5", val -> 9));
        Assertions.assertEquals(9, (int) FilterParser.ofInt().parseWithApiException("-5", val -> 9));
        Assertions.assertEquals(9, (int) FilterParser.ofInt().parseWithApiException("tybnksd", val -> 9));
        Assertions.assertThrows(FilteringApiException.class,
            () -> FilterParser.ofInt().parseWithApiException("tybnksd", Integer::valueOf));
    }

    @Test
    void parseOrDefault() {
        final int result = FilterParser.ofInt().parseOrDefault("5", -2);
        Assertions.assertEquals(5, result);

        final int result2 = FilterParser.ofInt().parseOrDefault("58979", -2);
        Assertions.assertEquals(58979, result2);

        final int result3 = FilterParser.ofInt().parseOrDefault("-3258979", -2);
        Assertions.assertEquals(-3258979, result3);
    }

    @Test
    void parseOrDefaultWithBadInput() {
        final int result = FilterParser.ofInt().parseOrDefault("--5", -2);
        Assertions.assertEquals(-2, result);

        final int result2 = FilterParser.ofInt().parseOrDefault("uguyyt6", -2);
        Assertions.assertEquals(-2, result2);

        // Might use trim later by default
        final int result3 = FilterParser.ofInt().parseOrDefault("    8     ", -2);
        Assertions.assertEquals(-2, result3);
    }

    @Test
    void parseOrDefaultWithSupplier() {
        final int result = FilterParser.ofInt().parseOrDefault("5", () -> -2);
        Assertions.assertEquals(5, result);

        final int result2 = FilterParser.ofInt().parseOrDefault("58979", () -> -2);
        Assertions.assertEquals(58979, result2);

        final int result3 = FilterParser.ofInt().parseOrDefault("-3258979", () -> -2);
        Assertions.assertEquals(-3258979, result3);
    }

    @Test
    void parseOrDefaultWithSupplierBadInput() {
        final int result = FilterParser.ofInt().parseOrDefault("--5", () -> -2);
        Assertions.assertEquals(-2, result);

        final int result2 = FilterParser.ofInt().parseOrDefault("uguyyt6", () -> -2);
        Assertions.assertEquals(-2, result2);

        final int result3 = FilterParser.ofInt().parseOrDefault("   8   ", () -> -2);
        Assertions.assertEquals(-2, result3);
    }
}
