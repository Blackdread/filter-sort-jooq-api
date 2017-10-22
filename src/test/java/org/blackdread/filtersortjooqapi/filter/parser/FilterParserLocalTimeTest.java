package org.blackdread.filtersortjooqapi.filter.parser;

import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

class FilterParserLocalTimeTest {

    private static final LocalTime now = LocalTime.now();
    private static final LocalTime time1 = LocalTime.of(14, 46);
    private static final LocalTime time2 = LocalTime.of(10, 30, 45);
    private static final LocalTime time3 = LocalTime.of(10, 30, 45, 1233);

    private static final String correctFormatTime1 = "14:46";
    private static final String correctFormatTime2 = "10:30:45";
    private static final String correctFormatTime3 = "10:30:45.000001233";

    @Test
    void parse() {
        Assertions.assertEquals(time1, FilterParser.ofTime().parse(correctFormatTime1));
        Assertions.assertEquals(time2, FilterParser.ofTime().parse(correctFormatTime2));
        Assertions.assertEquals(time3, FilterParser.ofTime().parse(correctFormatTime3));
    }

    @Test
    void parseThrows() {
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofTime().parse("12 43"));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofTime().parse("12:23 45"));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofTime().parse("Odwdw0"));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofTime().parse("O"));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofTime().parse("87"));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofTime().parse(" 5   "));
    }

    @Test
    void parseWithApiException() {
        Assertions.assertEquals(time1, FilterParser.ofTime().parseWithApiException(correctFormatTime1));
        Assertions.assertEquals(time2, FilterParser.ofTime().parseWithApiException(correctFormatTime2));
        Assertions.assertEquals(time3, FilterParser.ofTime().parseWithApiException(correctFormatTime3));
    }

    @Test
    void parseWithApiExceptionThrows() {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofTime().parseWithApiException("12 43"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofTime().parseWithApiException("12:23 45"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofTime().parseWithApiException("Odwdw0"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofTime().parseWithApiException("O"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofTime().parseWithApiException(" 5   "));
    }

    @Test
    void parseWithApiExceptionWithCustomParser() {
        Assertions.assertEquals(time1, FilterParser.ofTime().parseWithApiException("5", val -> time1));
        Assertions.assertEquals(time2, FilterParser.ofTime().parseWithApiException("-5", val -> time2));
        Assertions.assertEquals(time3, FilterParser.ofTime().parseWithApiException("-5", val -> time3));
    }

    @Test
    void parseOrDefault() {
        final LocalTime result = FilterParser.ofTime().parseOrDefault(correctFormatTime1, time1);
        Assertions.assertEquals(time1, result);

        final LocalTime result2 = FilterParser.ofTime().parseOrDefault(correctFormatTime2, time2);
        Assertions.assertEquals(time2, result2);
    }

    @Test
    void parseOrDefaultWithBadInput() {
        final LocalTime result = FilterParser.ofTime().parseOrDefault("12:02: 21", now);
        Assertions.assertEquals(now, result);

        final LocalTime result2 = FilterParser.ofTime().parseOrDefault(" 12:02:43  ", now);
        Assertions.assertEquals(now, result2);

        final LocalTime result3 = FilterParser.ofTime().parseOrDefault("26:00:00", now);
        Assertions.assertEquals(now, result3);
    }

    @Test
    void parseOrDefaultWithSupplier() {
        final LocalTime result = FilterParser.ofTime().parseOrDefault(correctFormatTime1, () -> now);
        Assertions.assertEquals(time1, result);

        final LocalTime result2 = FilterParser.ofTime().parseOrDefault(correctFormatTime2, () -> now);
        Assertions.assertEquals(time2, result2);
    }

    @Test
    void parseOrDefaultWithSupplierBadInput() {
        final LocalTime result = FilterParser.ofTime().parseOrDefault("34", () -> now);
        Assertions.assertEquals(now, result);

        final LocalTime result2 = FilterParser.ofTime().parseOrDefault("12:02:92", () -> now);
        Assertions.assertEquals(now, result2);

        final LocalTime result3 = FilterParser.ofTime().parseOrDefault("26:00:00", () -> now);
        Assertions.assertEquals(now, result3);
    }
}
