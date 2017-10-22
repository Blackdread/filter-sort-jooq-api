package org.blackdread.filtersortjooqapi.filter.parser;

import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class FilterParserIntTest {

    static Stream<String> badStringValuesToParse() {
        return Stream.of("foo", "poiuytd", "Odwdw0", "O", "  5", "   6", "--4");
    }

    static Stream<Arguments> goodStringParsableAndResult() {
        return Stream.of(Arguments.of("5", 5), Arguments.of("-5", -5), Arguments.of("0", 0),
            Arguments.of("006", 6), Arguments.of("3132324", 3132324));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parse(final String argument, final int expected) {
        Assertions.assertEquals(expected, (int) FilterParser.ofInt().parse(argument));
    }

    @ParameterizedTest
    @MethodSource("badStringValuesToParse")
    void parseThrows(final String argument) {
        Assertions.assertThrows(NumberFormatException.class, () -> FilterParser.ofInt().parse(argument));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseWithApiException(final String argument, final int expected) {
        Assertions.assertEquals(expected, (int) FilterParser.ofInt().parseWithApiException(argument));
    }

    @ParameterizedTest
    @MethodSource("badStringValuesToParse")
    void parseWithApiExceptionThrows(final String argument) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterParser.ofInt().parseWithApiException(argument));
    }

    @Test
    void parseWithApiExceptionWithCustomParser() {
        Assertions.assertEquals(9, (int) FilterParser.ofInt().parseWithApiException("5", val -> 9));
        Assertions.assertEquals(9, (int) FilterParser.ofInt().parseWithApiException("-5", val -> 9));
        Assertions.assertEquals(9, (int) FilterParser.ofInt().parseWithApiException("tybnksd", val -> 9));
        Assertions.assertThrows(FilteringApiException.class,
            () -> FilterParser.ofInt().parseWithApiException("tybnksd", Integer::valueOf));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseOrDefault(final String argument, final int expected) {
        Assertions.assertEquals(expected, (int) FilterParser.ofInt().parseOrDefault(argument, -2));
    }

    @ParameterizedTest
    @MethodSource("badStringValuesToParse")
    void parseOrDefaultWithBadInput(final String argument) {
        Assertions.assertEquals(-2, (int) FilterParser.ofInt().parseOrDefault(argument, -2));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseOrDefaultWithSupplier(final String argument, final int expected) {
        Assertions.assertEquals(expected, (int) FilterParser.ofInt().parseOrDefault(argument, () -> -2));
    }

    @ParameterizedTest
    @MethodSource("badStringValuesToParse")
    void parseOrDefaultWithSupplierBadInput(final String argument) {
        Assertions.assertEquals(-2, (int) FilterParser.ofInt().parseOrDefault(argument, () -> -2));
    }
}
