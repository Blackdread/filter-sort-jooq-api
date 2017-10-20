package org.blackdread.filtersortjooqapi.contract.parser;

import java.time.LocalTime;

/**
 * Created by Yoann CAPLAIN on 2017/8/29.
 */
class FilterMultipleValueParserTime extends FilterMultipleValueParserAbstract<LocalTime> {

    static final FilterMultipleValueParser<LocalTime> INSTANCE = new FilterMultipleValueParserTime();

    private FilterMultipleValueParserTime() {
    }

    @Override
    protected FilterParser<LocalTime> getParser() {
        return FilterParser.ofTime();
    }

}
