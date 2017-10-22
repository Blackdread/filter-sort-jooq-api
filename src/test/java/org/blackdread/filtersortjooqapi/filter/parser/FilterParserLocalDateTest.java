package org.blackdread.filtersortjooqapi.filter.parser;

import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

class FilterParserLocalDateTest {

    private static final LocalDate now = LocalDate.now();
    private static final LocalDate date1 = LocalDate.of(2017, 10, 25);
    private static final LocalDate date2 = LocalDate.of(2010, 12, 01);

    private static final String correctFormatDate1 = "2017-10-25";
    private static final String correctFormatDate2 = "2010-12-01";

    @Test
    void parse() {
        Assertions.assertEquals(date1, FilterParser.ofDate().parse(correctFormatDate1));
        Assertions.assertEquals(date2, FilterParser.ofDate().parse(correctFormatDate2));
    }

    @Test
    void parseThrows() {
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofDate().parse("2017-04-06  "));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofDate().parse("2017-04"));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofDate().parse("2017"));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofDate().parse("O"));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofDate().parse(" 5   "));
    }

    @Test
    void parseWithApiException() {
        Assertions.assertEquals(date1, FilterParser.ofDate().parseWithApiException(correctFormatDate1));
        Assertions.assertEquals(date2, FilterParser.ofDate().parseWithApiException(correctFormatDate2));
    }

    @Test
    void parseWithApiExceptionThrows() {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofDate().parseWithApiException("2017-04-06  "));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofDate().parseWithApiException("2017-04"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofDate().parseWithApiException("2017"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofDate().parseWithApiException("O"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofDate().parseWithApiException(" 5   "));
    }

    @Test
    void parseWithApiExceptionWithCustomParser() {
        Assertions.assertEquals(date1, FilterParser.ofDate().parseWithApiException("5", val -> date1));
        Assertions.assertEquals(date2, FilterParser.ofDate().parseWithApiException("-5", val -> date2));
    }

    @Test
    void parseOrDefault() {
        final LocalDate result = FilterParser.ofDate().parseOrDefault(correctFormatDate1, date1);
        Assertions.assertEquals(date1, result);

        final LocalDate result2 = FilterParser.ofDate().parseOrDefault(correctFormatDate2, date2);
        Assertions.assertEquals(date2, result2);
    }

    @Test
    void parseOrDefaultWithBadInput() {
        final LocalDate result = FilterParser.ofDate().parseOrDefault("2017-04-06    ", now);
        Assertions.assertEquals(now, result);

        final LocalDate result2 = FilterParser.ofDate().parseOrDefault("2017-04-O6T12:02:12", now);
        Assertions.assertEquals(now, result2);

        final LocalDate result3 = FilterParser.ofDate().parseOrDefault("12:00:00", now);
        Assertions.assertEquals(now, result3);
    }

    @Test
    void parseOrDefaultWithSupplier() {
        final LocalDate result = FilterParser.ofDate().parseOrDefault(correctFormatDate1, () -> now);
        Assertions.assertEquals(date1, result);

        final LocalDate result2 = FilterParser.ofDate().parseOrDefault(correctFormatDate2, () -> now);
        Assertions.assertEquals(date2, result2);
    }

    @Test
    void parseOrDefaultWithSupplierBadInput() {
        final LocalDate result = FilterParser.ofDate().parseOrDefault("2017-04-06     ", () -> now);
        Assertions.assertEquals(now, result);

        final LocalDate result2 = FilterParser.ofDate().parseOrDefault("2017-04-O6T12:02:12", () -> now);
        Assertions.assertEquals(now, result2);

        final LocalDate result3 = FilterParser.ofDate().parseOrDefault("12:00:00", () -> now);
        Assertions.assertEquals(now, result3);
    }
}
