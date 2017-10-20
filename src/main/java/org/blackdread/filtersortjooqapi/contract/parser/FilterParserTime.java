package org.blackdread.filtersortjooqapi.contract.parser;

import java.time.LocalTime;

/**
 * Created by Yoann CAPLAIN on 2017/8/28.
 */
class FilterParserTime implements FilterParser<LocalTime> {

    static final FilterParser<LocalTime> INSTANCE = new FilterParserTime();

    private FilterParserTime() {
    }

    @Override
    public LocalTime parse(final String value) {
        // Very simple but straight forward parser, it use default format DateTimeFormatter.ISO_LOCAL_TIME
        // so like: "10:15:30"
        // hour:minutes:seconds
        return LocalTime.parse(value);
    }
}
