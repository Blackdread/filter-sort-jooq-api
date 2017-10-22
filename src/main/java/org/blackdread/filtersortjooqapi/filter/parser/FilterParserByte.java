package org.blackdread.filtersortjooqapi.filter.parser;

/**
 * Created by Yoann CAPLAIN on 2017/8/28.
 */
class FilterParserByte implements FilterParser<Byte> {

    static final FilterParser<Byte> INSTANCE = new FilterParserByte();

    private FilterParserByte() {
    }

    @Override
    public Byte parse(final String value) {
        // TODO remove '0x' then ...
        return Byte.valueOf(value);
    }
}
