package org.blackdread.filtersortjooqapi.filter.parser;

import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

class FilterMultipleValueParserIntTest {


    static Stream<String> badStringValuesToParse() {
        return Stream.of("", "foo", "poiuytd", "Odwdw0", "O", "  5", "   6", "--4", "bla,asdasd,9287,dadas,-5,--3,998");
    }

    static Stream<Arguments> goodStringParsableAndResult() {
        return Stream.of(
            Arguments.of("5", Collections.singletonList(5)),
            Arguments.of("5,999,-55", Arrays.asList(5, 999, -55))
        );
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parse(final String argument, final List<Integer> expected) {
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofInt().parse(argument));
        Assertions.assertEquals(expected.size(), FilterMultipleValueParser.ofInt().parse(argument).size());
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofInt().parse(argument, ","));
    }

    @ParameterizedTest
    @MethodSource("badStringValuesToParse")
    void parseThrows(final String argument) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofInt().parse(argument));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofInt().parse(argument, ","));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseWithExpectedElements(final String argument, final List<Integer> expected) {
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofInt().parse(argument, expected.size()));
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofInt().parse(argument, ",", expected.size()));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseWithExpectedElementsThrows2(final String argument, final List<Integer> expected) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofInt().parse(argument, 929292));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofInt().parse(argument, ",", 929292));
    }

    @ParameterizedTest
    @MethodSource("badStringValuesToParse")
    void parseWithExpectedElementsThrows(final String argument) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofInt().parse(argument, 929292));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofInt().parse(argument, ",", 929292));
    }

    @Test
    void noValuesThrows() {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofInt().parse(""));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofInt().parse("", 3));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofInt().parse("", ","));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofInt().parse("", ",", 3));
    }

}
