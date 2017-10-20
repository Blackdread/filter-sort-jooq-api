package org.blackdread.filtersortjooqapi.contract.parser;

/**
 * Created by Yoann CAPLAIN on 2017/9/12.
 */
class FilterMultipleValueParserByte extends FilterMultipleValueParserAbstract<Byte> {

    static final FilterMultipleValueParser<Byte> INSTANCE = new FilterMultipleValueParserByte();

    private FilterMultipleValueParserByte() {
    }

    @Override
    protected FilterParser<Byte> getParser() {
        return FilterParser.ofByte();
    }

}
