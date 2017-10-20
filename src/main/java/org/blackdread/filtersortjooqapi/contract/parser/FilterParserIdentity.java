package org.blackdread.filtersortjooqapi.contract.parser;

/**
 * Identity filter parser that simply return value passed
 * Created by Yoann CAPLAIN on 2017/8/29.
 */
class FilterParserIdentity implements FilterParser<String> {

    static final FilterParser<String> INSTANCE = new FilterParserIdentity();

    private FilterParserIdentity() {
    }

    @Override
    public String parse(final String value) {
        return value;
    }
}
