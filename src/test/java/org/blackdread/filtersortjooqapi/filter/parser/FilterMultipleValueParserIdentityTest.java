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

class FilterMultipleValueParserIdentityTest {

    static Stream<Arguments> goodStringParsableAndResult() {
        return Stream.of(
            Arguments.of("5", Collections.singletonList("5")),
            Arguments.of("5,999,-55,)(*&^%$,djoaihdouawd,][][poouia sdasd a ad", Arrays.asList("5,999,-55,)(*&^%$,djoaihdouawd,][][poouia sdasd a ad".split(",")))
        );
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parse(final String argument, final List<String> expected) {
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofIdentity().parse(argument));
        Assertions.assertEquals(expected.size(), FilterMultipleValueParser.ofIdentity().parse(argument).size());
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofIdentity().parse(argument, ","));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseWithExpectedElements(final String argument, final List<String> expected) {
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofIdentity().parse(argument, expected.size()));
        Assertions.assertEquals(expected, FilterMultipleValueParser.ofIdentity().parse(argument, ",", expected.size()));
    }

    @ParameterizedTest
    @MethodSource("goodStringParsableAndResult")
    void parseWithExpectedElementsThrows2(final String argument, final List<String> expected) {
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofIdentity().parse(argument, 929292));
        Assertions.assertThrows(FilteringApiException.class, () -> FilterMultipleValueParser.ofIdentity().parse(argument, ",", 929292));
    }

    @Test
    void noValuesDoesNotThrows() {
        FilterMultipleValueParser.ofIdentity().parse("");
        FilterMultipleValueParser.ofIdentity().parse("", 1);
        FilterMultipleValueParser.ofIdentity().parse("", ",");
        FilterMultipleValueParser.ofIdentity().parse("", ",", 1);
    }

}
