package org.blackdread.filtersortjooqapi.filter.parser;

import java.time.LocalDate;

/**
 * Created by Yoann CAPLAIN on 2017/8/28.
 */
class FilterParserDate implements FilterParser<LocalDate> {

    static final FilterParser<LocalDate> INSTANCE = new FilterParserDate();

    private FilterParserDate() {
    }

    @Override
    public LocalDate parse(final String value) {
        // Very simple but straight forward parser, it use default format DateTimeFormatter.ISO_LOCAL_DATE
        // so like: "2000-01-15"
        // year-month-day
        return LocalDate.parse(value);
    }
}
