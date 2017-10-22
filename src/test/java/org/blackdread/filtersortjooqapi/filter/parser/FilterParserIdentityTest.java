package org.blackdread.filtersortjooqapi.filter.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FilterParserIdentityTest {

    @Test
    void parse() {
        Assertions.assertEquals("5", FilterParser.ofIdentity().parse("5"));
        Assertions.assertEquals("-5", FilterParser.ofIdentity().parse("-5"));
        Assertions.assertEquals("0", FilterParser.ofIdentity().parse("0"));
        Assertions.assertEquals("066", FilterParser.ofIdentity().parse("066"));
        Assertions.assertEquals("312313", FilterParser.ofIdentity().parse("312313"));
    }

    @Test
    void parseWithApiException() {
        Assertions.assertEquals("5", FilterParser.ofIdentity().parseWithApiException("5"));
        Assertions.assertEquals("-5", FilterParser.ofIdentity().parseWithApiException("-5"));
        Assertions.assertEquals("0", FilterParser.ofIdentity().parseWithApiException("0"));
        Assertions.assertEquals("066", FilterParser.ofIdentity().parseWithApiException("066"));
        Assertions.assertEquals("312313", FilterParser.ofIdentity().parseWithApiException("312313"));
    }

    @Test
    void parseWithApiExceptionWithCustomParser() {
        Assertions.assertEquals("bla1", FilterParser.ofIdentity().parseWithApiException("5", val -> "bla1"));
        Assertions.assertEquals("-5", FilterParser.ofIdentity().parseWithApiException("-5", val -> val));
    }

    @Test
    void parseOrDefault() {
        final String result = FilterParser.ofIdentity().parseOrDefault("5", "no");
        Assertions.assertEquals("5", result);
    }

    @Test
    void parseOrDefaultWithSupplier() {
        final String result = FilterParser.ofIdentity().parseOrDefault("5", () -> "no");
        Assertions.assertEquals("5", result);
    }
}
