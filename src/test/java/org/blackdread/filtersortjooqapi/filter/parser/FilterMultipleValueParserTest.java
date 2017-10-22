package org.blackdread.filtersortjooqapi.filter.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

class FilterMultipleValueParserTest {

    @Test
    void ofIntIsSingleton() {
        final FilterMultipleValueParser<Integer> filterParser1 = FilterMultipleValueParser.ofInt();
        final FilterMultipleValueParser<Integer> filterParser2 = FilterMultipleValueParser.ofInt();
        Assertions.assertNotNull(filterParser1);
        Assertions.assertSame(filterParser1, filterParser2);
    }

    @Test
    void ofDateIsSingleton() {
        final FilterMultipleValueParser<LocalDate> filterParser1 = FilterMultipleValueParser.ofDate();
        final FilterMultipleValueParser<LocalDate> filterParser2 = FilterMultipleValueParser.ofDate();
        Assertions.assertNotNull(filterParser1);
        Assertions.assertSame(filterParser1, filterParser2);
    }

    @Test
    void ofTimeIsSingleton() {
        final FilterMultipleValueParser<LocalTime> filterParser1 = FilterMultipleValueParser.ofTime();
        final FilterMultipleValueParser<LocalTime> filterParser2 = FilterMultipleValueParser.ofTime();
        Assertions.assertNotNull(filterParser1);
        Assertions.assertSame(filterParser1, filterParser2);
    }

    @Test
    void ofDateTimeIsSingleton() {
        final FilterMultipleValueParser<LocalDateTime> filterParser1 = FilterMultipleValueParser.ofDateTime();
        final FilterMultipleValueParser<LocalDateTime> filterParser2 = FilterMultipleValueParser.ofDateTime();
        Assertions.assertNotNull(filterParser1);
        Assertions.assertSame(filterParser1, filterParser2);
    }

    @Test
    void ofIdentityIsSingleton() {
        final FilterMultipleValueParser<String> filterParser1 = FilterMultipleValueParser.ofIdentity();
        final FilterMultipleValueParser<String> filterParser2 = FilterMultipleValueParser.ofIdentity();
        Assertions.assertNotNull(filterParser1);
        Assertions.assertSame(filterParser1, filterParser2);
    }

    @Test
    void ofByteIsSingleton() {
        final FilterMultipleValueParser<Byte> filterParser1 = FilterMultipleValueParser.ofByte();
        final FilterMultipleValueParser<Byte> filterParser2 = FilterMultipleValueParser.ofByte();
        Assertions.assertNotNull(filterParser1);
        Assertions.assertSame(filterParser1, filterParser2);
    }

}
