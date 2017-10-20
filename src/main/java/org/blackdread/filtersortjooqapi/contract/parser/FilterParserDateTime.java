package org.blackdread.filtersortjooqapi.contract.parser;

import java.time.LocalDateTime;

/**
 * Created by Yoann CAPLAIN on 2017/8/28.
 */
class FilterParserDateTime implements FilterParser<LocalDateTime> {

    static final FilterParser<LocalDateTime> INSTANCE = new FilterParserDateTime();

    private FilterParserDateTime() {
    }

    @Override
    public LocalDateTime parse(final String value) {
        // Very simple but straight forward parser, it use default format DateTimeFormatter.ISO_LOCAL_DATE_TIME
        // so like: "2007-12-03T10:15:30"
        // year-month-dayThour:minutes:seconds
        // TODO there is a 'T' that we could allow to put a space instead so we would replace that space with a 'T' for default parser to work
        return LocalDateTime.parse(value);
    }
}
