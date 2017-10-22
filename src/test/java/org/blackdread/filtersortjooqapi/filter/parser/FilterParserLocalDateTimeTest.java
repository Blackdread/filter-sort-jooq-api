package org.blackdread.filtersortjooqapi.filter.parser;

import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

class FilterParserLocalDateTimeTest {

    private static final LocalDateTime now = LocalDateTime.now();
    private static final LocalDateTime date1 = LocalDateTime.of(2017, 10, 20, 14, 46);
    private static final LocalDateTime date2 = LocalDateTime.of(2017, 10, 25, 10, 30, 45);
    private static final LocalDateTime date3 = LocalDateTime.of(2017, 10, 25, 10, 30, 45, 1233);

    private static final String correctFormatDate1 = "2017-10-20T14:46";
    private static final String correctFormatDate2 = "2017-10-25T10:30:45";
    private static final String correctFormatDate3 = "2017-10-25T10:30:45.000001233";

    @Test
    void parse() {
        Assertions.assertEquals(date1, FilterParser.ofDateTime().parse(correctFormatDate1));
        Assertions.assertEquals(date2, FilterParser.ofDateTime().parse(correctFormatDate2));
        Assertions.assertEquals(date3, FilterParser.ofDateTime().parse(correctFormatDate3));

        // Later could add more default "correct" datetime values but very minimal -> still keep ISO
//        Assertions.assertEquals(date1, FilterParser.ofDateTime().parse("2017-10-20 14:46"));
//        Assertions.assertEquals(date2, FilterParser.ofDateTime().parse("2017-10-25 10:30:45"));
    }

    @Test
    void parseThrows() {
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofDateTime().parse("2017-04-06"));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofDateTime().parse("poiuytd"));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofDateTime().parse("Odwdw0"));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofDateTime().parse("O"));
        Assertions.assertThrows(DateTimeParseException.class, () -> FilterParser.ofDateTime().parse(" 5   "));
    }

    @Test
    void parseWithApiException() {
        Assertions.assertEquals(date1, FilterParser.ofDateTime().parseWithApiException(correctFormatDate1));
        Assertions.assertEquals(date2, FilterParser.ofDateTime().parseWithApiException(correctFormatDate2));
        Assertions.assertEquals(date3, FilterParser.ofDateTime().parseWithApiException(correctFormatDate3));

        // Later could add more default "correct" datetime values but very minimal -> still keep ISO
//        Assertions.assertEquals(date1, FilterParser.ofDateTime().parseWithApiException("2017-10-20 14:46"));
//        Assertions.assertEquals(date2, FilterParser.ofDateTime().parseWithApiException("2017-10-25 10:30:45"));
    }

    @Test
    void parseWithApiExceptionThrows() {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofDateTime().parseWithApiException("2017-04-06"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofDateTime().parseWithApiException("poiuytd"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofDateTime().parseWithApiException("Odwdw0"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofDateTime().parseWithApiException("O"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofDateTime().parseWithApiException(" 5   "));
    }

    @Test
    void parseWithApiExceptionWithCustomParser() {
        Assertions.assertEquals(date1, FilterParser.ofDateTime().parseWithApiException("5", val -> date1));
        Assertions.assertEquals(date2, FilterParser.ofDateTime().parseWithApiException("-5", val -> date2));
        Assertions.assertEquals(date3, FilterParser.ofDateTime().parseWithApiException("-5", val -> date3));
    }

    @Test
    void parseOrDefault() {
        final LocalDateTime result = FilterParser.ofDateTime().parseOrDefault(correctFormatDate1, date1);
        Assertions.assertEquals(date1, result);

        final LocalDateTime result2 = FilterParser.ofDateTime().parseOrDefault(correctFormatDate2, date2);
        Assertions.assertEquals(date2, result2);
    }

    @Test
    void parseOrDefaultWithBadInput() {
        final LocalDateTime result = FilterParser.ofDateTime().parseOrDefault("2017-04-06", now);
        Assertions.assertEquals(now, result);

        final LocalDateTime result2 = FilterParser.ofDateTime().parseOrDefault("2017-04-O6    12:02:92", now);
        Assertions.assertEquals(now, result2);

        final LocalDateTime result3 = FilterParser.ofDateTime().parseOrDefault("12:00:00", now);
        Assertions.assertEquals(now, result3);
    }

    @Test
    void parseOrDefaultWithSupplier() {
        final LocalDateTime result = FilterParser.ofDateTime().parseOrDefault(correctFormatDate1, () -> now);
        Assertions.assertEquals(date1, result);

        final LocalDateTime result2 = FilterParser.ofDateTime().parseOrDefault(correctFormatDate2, () -> now);
        Assertions.assertEquals(date2, result2);
    }

    @Test
    void parseOrDefaultWithSupplierBadInput() {
        final LocalDateTime result = FilterParser.ofDateTime().parseOrDefault("2017-04-06", () -> now);
        Assertions.assertEquals(now, result);

        final LocalDateTime result2 = FilterParser.ofDateTime().parseOrDefault("2017-04-O6    12:02:92", () -> now);
        Assertions.assertEquals(now, result2);

        final LocalDateTime result3 = FilterParser.ofDateTime().parseOrDefault("   8   ", () -> now);
        Assertions.assertEquals(now, result3);
    }
}
