package org.blackdread.filtersortjooqapi.contract.parser;


import org.blackdread.filtersortjooqapi.contract.exception.FilteringApiException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yoann CAPLAIN on 2017/8/29.
 */
abstract class FilterMultipleValueParserAbstract<R> implements FilterMultipleValueParser<R> {

    FilterMultipleValueParserAbstract() {
    }

    @Override
    public List<R> parse(final String value, final String token) {
        final List<R> parsedValues = Arrays.stream(value.split(token))
//            .map(this::prepareValueForParsing)
            .map(getParser()::parseWithApiException)
            .collect(Collectors.toList());
        // This is a must have as we filter on values so minimum one has to be given/found
        if (parsedValues.isEmpty())
            throw new FilteringApiException("Parser of value (" + value + ") resulted in no value. Should result in at least one");
        return parsedValues;
    }

    /**
     * Get the parser to parse each individual value
     *
     * @return Parser for each individual value
     */
    protected abstract FilterParser<R> getParser();

    // TODO Could add this method to allow to make value passed less error prone to parsing -> example for int parser we could trim spaces (just to have a more reliable parsing way instead of throwing exception for tiny mistakes)
//    protected abstract String prepareValueForParsing(final String value);

}
