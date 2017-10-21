package org.blackdread.filtersortjooqapi.filter.parser;

/**
 * Created by Yoann CAPLAIN on 2017/8/29.
 */
class FilterMultipleValueParserInteger extends FilterMultipleValueParserAbstract<Integer> {

    static final FilterMultipleValueParser<Integer> INSTANCE = new FilterMultipleValueParserInteger();

    private FilterMultipleValueParserInteger() {
    }

    @Override
    protected FilterParser<Integer> getParser() {
        return FilterParser.ofInt();
    }

}
