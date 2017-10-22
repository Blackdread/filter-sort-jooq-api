package org.blackdread.filtersortjooqapi.filter.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

class FilterParserTest {

    @Test
    void ofIntIsSingleton() {
        final FilterParser<Integer> filterParser1 = FilterParser.ofInt();
        final FilterParser<Integer> filterParser2 = FilterParser.ofInt();
        Assertions.assertNotNull(filterParser1);
        Assertions.assertSame(filterParser1, filterParser2);
    }

    @Test
    void ofDateIsSingleton() {
        final FilterParser<LocalDate> filterParser1 = FilterParser.ofDate();
        final FilterParser<LocalDate> filterParser2 = FilterParser.ofDate();
        Assertions.assertNotNull(filterParser1);
        Assertions.assertSame(filterParser1, filterParser2);
    }

    @Test
    void ofTimeIsSingleton() {
        final FilterParser<LocalTime> filterParser1 = FilterParser.ofTime();
        final FilterParser<LocalTime> filterParser2 = FilterParser.ofTime();
        Assertions.assertNotNull(filterParser1);
        Assertions.assertSame(filterParser1, filterParser2);
    }

    @Test
    void ofDateTimeIsSingleton() {
        final FilterParser<LocalDateTime> filterParser1 = FilterParser.ofDateTime();
        final FilterParser<LocalDateTime> filterParser2 = FilterParser.ofDateTime();
        Assertions.assertNotNull(filterParser1);
        Assertions.assertSame(filterParser1, filterParser2);
    }

    @Test
    void ofIdentityIsSingleton() {
        final FilterParser<String> filterParser1 = FilterParser.ofIdentity();
        final FilterParser<String> filterParser2 = FilterParser.ofIdentity();
        Assertions.assertNotNull(filterParser1);
        Assertions.assertSame(filterParser1, filterParser2);
    }

    @Test
    void ofByteIsSingleton() {
        final FilterParser<Byte> filterParser1 = FilterParser.ofByte();
        final FilterParser<Byte> filterParser2 = FilterParser.ofByte();
        Assertions.assertNotNull(filterParser1);
        Assertions.assertSame(filterParser1, filterParser2);
    }

}
