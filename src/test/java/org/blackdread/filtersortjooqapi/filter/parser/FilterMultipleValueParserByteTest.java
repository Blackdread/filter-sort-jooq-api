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

class FilterMultipleValueParserByteTest {


    static Stream<String> badStringValuesToParse() {
        return Stream.of("", "foo", "poiuytd", "Odwdw0", "129", "O", "  5", "   6", "--4", "bla,asdasd,9287,dadas,-5,--3,998");
    }

    static Stream<Arguments> goodStringParsableAndResult() {
        return Stream.of(
            Arguments.of("5", Collections.singletonList((byte) 5)),
            Arguments.of("5,127,-55", Arrays.asList((byte) 5, (byte) 127, (byte) -55))
        );
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parse(final String argument, final List<Byte> expected) {
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofByte().parse(argument));
        Assertions.assertEquals(expected.size(), FilterMultipleValueParser.ofByte().parse(argument).size());
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofByte().parse(argument, ","));
    }

    @ParameterizedTest
    @MethodSource("badStringValuesToParse")
    void parseThrows(final String argument) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofByte().parse(argument));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofByte().parse(argument, ","));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseWithExpectedElements(final String argument, final List<Byte> expected) {
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofByte().parse(argument, expected.size()));
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofByte().parse(argument, ",", expected.size()));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseWithExpectedElementsThrows2(final String argument, final List<Byte> expected) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofByte().parse(argument, 929292));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofByte().parse(argument, ",", 929292));
    }

    @ParameterizedTest
    @MethodSource("badStringValuesToParse")
    void parseWithExpectedElementsThrows(final String argument) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofByte().parse(argument, 929292));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofByte().parse(argument, ",", 929292));
    }

    @Test
    void noValuesThrows() {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofByte().parse(""));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofByte().parse("", 3));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofByte().parse("", ","));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofByte().parse("", ",", 3));
    }

}
