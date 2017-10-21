package org.blackdread.filtersortjooqapi.filter.parser;

import java.time.LocalDateTime;

/**
 * Created by Yoann CAPLAIN on 2017/8/29.
 */
class FilterMultipleValueParserDateTime extends FilterMultipleValueParserAbstract<LocalDateTime> {

    static final FilterMultipleValueParser<LocalDateTime> INSTANCE = new FilterMultipleValueParserDateTime();

    private FilterMultipleValueParserDateTime() {
    }

    @Override
    protected FilterParser<LocalDateTime> getParser() {
        return FilterParser.ofDateTime();
    }

}
