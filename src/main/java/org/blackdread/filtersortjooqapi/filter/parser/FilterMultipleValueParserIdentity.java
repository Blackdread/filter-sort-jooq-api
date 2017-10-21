package org.blackdread.filtersortjooqapi.filter.parser;

/**
 * Created by Yoann CAPLAIN on 2017/8/29.
 */
public class FilterMultipleValueParserIdentity extends FilterMultipleValueParserAbstract<String> {

    static final FilterMultipleValueParser<String> INSTANCE = new FilterMultipleValueParserIdentity();

    private FilterMultipleValueParserIdentity() {
    }

    @Override
    protected FilterParser<String> getParser() {
        return FilterParser.ofIdentity();
    }
}
