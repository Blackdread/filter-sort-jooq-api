package org.blackdread.filtersortjooqapi.filter.parser;

/**
 * Created by Yoann CAPLAIN on 2017/8/28.
 */
class FilterParserInteger implements FilterParser<Integer> {

    static final FilterParser<Integer> INSTANCE = new FilterParserInteger();

    private FilterParserInteger() {
    }

    @Override
    public Integer parse(final String value) {
        return Integer.valueOf(value);
    }
}
