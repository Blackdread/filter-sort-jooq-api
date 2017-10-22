package org.blackdread.filtersortjooqapi.filter.parser;

import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

class FilterMultipleValueParserDateTimeTest {


    static Stream<String> badStringValuesToParse() {
        return Stream.of("", "2017-04-04", "2017-04-04 12:12:12", "2017-04-04T12:OO:O0", "2017", "O", "  5", "   6", "--4", "bla,asdasd,9287,dadas,-5,--3,998");
    }

    static Stream<Arguments> goodStringParsableAndResult() {
        return Stream.of(
            Arguments.of("2017-04-04T12:12:12", Collections.singletonList(LocalDateTime.of(2017, 4, 4, 12, 12, 12))),
            Arguments.of("2017-04-04T12:12:12,2017-05-04T12:17:12,2017-12-25T12:15", Arrays.asList(
                LocalDateTime.of(2017, 4, 4, 12, 12, 12),
                LocalDateTime.of(2017, 5, 4, 12, 17, 12),
                LocalDateTime.of(2017, 12, 25, 12, 15)
            ))
        );
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parse(final String argument, final List<LocalDateTime> expected) {
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofDateTime().parse(argument));
        Assertions.assertEquals(expected.size(), FilterMultipleValueParser.ofDateTime().parse(argument).size());
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofDateTime().parse(argument, ","));
    }

    @ParameterizedTest
    @MethodSource("badStringValuesToParse")
    void parseThrows(final String argument) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDateTime().parse(argument));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDateTime().parse(argument, ","));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseWithExpectedElements(final String argument, final List<LocalDateTime> expected) {
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofDateTime().parse(argument, expected.size()));
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofDateTime().parse(argument, ",", expected.size()));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseWithExpectedElementsThrows2(final String argument, final List<LocalDateTime> expected) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDateTime().parse(argument, 929292));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDateTime().parse(argument, ",", 929292));
    }

    @ParameterizedTest
    @MethodSource("badStringValuesToParse")
    void parseWithExpectedElementsThrows(final String argument) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDateTime().parse(argument, 929292));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDateTime().parse(argument, ",", 929292));
    }

    @Test
    void noValuesThrows() {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDateTime().parse(""));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDateTime().parse("", 3));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDateTime().parse("", "::"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDateTime().parse("", "--", 3));
    }

}
