package org.blackdread.filtersortjooqapi.filter.parser;

import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

class FilterMultipleValueParserDateTest {


    static Stream<String> badStringValuesToParse() {
        return Stream.of("", "2017-04-04  ", "2017-04-04T12:12:12", "2017-04-04T12:OO:O0", "2017", "O", "  5", "   6", "--4", "bla,asdasd,9287,dadas,-5,--3,998");
    }

    static Stream<Arguments> goodStringParsableAndResult() {
        return Stream.of(
            Arguments.of("2017-04-04", Collections.singletonList(LocalDate.of(2017, 4, 4))),
            Arguments.of("2017-04-04,2017-05-04,2017-12-25", Arrays.asList(
                LocalDate.of(2017, 4, 4),
                LocalDate.of(2017, 5, 4),
                LocalDate.of(2017, 12, 25)
            ))
        );
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parse(final String argument, final List<LocalDate> expected) {
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofDate().parse(argument));
        Assertions.assertEquals(expected.size(), FilterMultipleValueParser.ofDate().parse(argument).size());
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofDate().parse(argument, ","));
    }

    @ParameterizedTest
    @MethodSource("badStringValuesToParse")
    void parseThrows(final String argument) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDate().parse(argument));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDate().parse(argument, ","));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseWithExpectedElements(final String argument, final List<LocalDate> expected) {
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofDate().parse(argument, expected.size()));
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofDate().parse(argument, ",", expected.size()));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseWithExpectedElementsThrows2(final String argument, final List<LocalDate> expected) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDate().parse(argument, 929292));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDate().parse(argument, ",", 929292));
    }

    @ParameterizedTest
    @MethodSource("badStringValuesToParse")
    void parseWithExpectedElementsThrows(final String argument) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDate().parse(argument, 929292));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDate().parse(argument, ",", 929292));
    }

    @Test
    void noValuesThrows() {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDate().parse(""));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDate().parse("", 3));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDate().parse("", "::"));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofDate().parse("", "--", 3));
    }

}
