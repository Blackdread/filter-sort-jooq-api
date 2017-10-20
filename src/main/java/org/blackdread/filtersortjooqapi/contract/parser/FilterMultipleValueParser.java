package org.blackdread.filtersortjooqapi.contract.parser;


import org.blackdread.filtersortjooqapi.contract.exception.FilteringApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by Yoann CAPLAIN on 2017/8/28.
 */
@FunctionalInterface
public interface FilterMultipleValueParser<R> {

    /**
     * @param value Contains at least one value, many are split with a token
     * @param token Token to split value (Has to have the form of a <b>java regex</b>)
     * @return List of parsed values
     * @throws RuntimeException When parsing failed if implementation does not swallow exception
     */
    List<R> parse(final String value, final String token);

    /**
     * Parse many elements with default token of ','
     *
     * @param value Value to parse
     * @return List of values parsed in value parameter
     * @throws RuntimeException When parsing failed if implementation does not swallow exception
     */
    default List<R> parse(final String value) {
        return parse(value, ",");
    }

    /**
     * Parse many elements with default token of ','
     *
     * @param value            Value to parse
     * @param expectedElements Number of expected elements that list will contain
     * @return List of values parsed in value parameter
     * @throws FilteringApiException If expected size is different from returned list size
     */
    default List<R> parse(final String value, final int expectedElements) {
        final List<R> parse = parse(value, ",");
        if (parse.size() != expectedElements)
            throw new FilteringApiException("Expected " + expectedElements + " selection only.");
        return parse;
    }

    /**
     * Parse many elements with given token
     *
     * @param value            Value to parse
     * @param token            Token to split value (Has to have the form of a <b>java regex</b>)
     * @param expectedElements Number of expected elements that list will contain
     * @return List of values parsed in value parameter
     * @throws FilteringApiException If expected size is different from returned list size
     */
    default List<R> parse(final String value, final String token, final int expectedElements) {
        final List<R> parse = parse(value, token);
        if (parse.size() != expectedElements)
            throw new FilteringApiException("Expected " + expectedElements + " selection only, instead of ");
        return parse;
    }

    static FilterMultipleValueParser<Integer> ofInt() {
        return FilterMultipleValueParserInteger.INSTANCE;
    }

    static FilterMultipleValueParser<LocalDate> ofDate() {
        return FilterMultipleValueParserDate.INSTANCE;
    }

    static FilterMultipleValueParser<LocalTime> ofTime() {
        return FilterMultipleValueParserTime.INSTANCE;
    }

    static FilterMultipleValueParser<LocalDateTime> ofDateTime() {
        return FilterMultipleValueParserDateTime.INSTANCE;
    }

    static FilterMultipleValueParser<String> ofIdentity() {
        return FilterMultipleValueParserIdentity.INSTANCE;
    }

    static FilterMultipleValueParser<Byte> ofByte() {
        return FilterMultipleValueParserByte.INSTANCE;
    }
}
