package org.blackdread.filtersortjooqapi.filter.parser;

import java.time.LocalDate;

/**
 * Created by Yoann CAPLAIN on 2017/8/29.
 */
class FilterMultipleValueParserDate extends FilterMultipleValueParserAbstract<LocalDate> {

    static final FilterMultipleValueParser<LocalDate> INSTANCE = new FilterMultipleValueParserDate();

    private FilterMultipleValueParserDate() {
    }

    @Override
    protected FilterParser<LocalDate> getParser() {
        return FilterParser.ofDate();
    }

}
