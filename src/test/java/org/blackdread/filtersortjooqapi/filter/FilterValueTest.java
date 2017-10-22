package org.blackdread.filtersortjooqapi.filter;

import org.blackdread.filtersortjooqapi.filter.keyparser.*;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FilterValueTest {

    private static FilterValue filterSupplier;

    private static FilterValue filterValue1;

    private static FilterValue filterValue2;

    private static FilterValue filterValue3;

    private static FilterValue filterValue4;

    private static FilterValue filterValue5;

    private static FilterValue filterValue6;

    @BeforeAll
    static void setUp() {
        filterSupplier = Filter.of("key", DSL::trueCondition);
        filterValue1 = Filter.of("key", val -> "value", value -> DSL.trueCondition());

        filterValue2 = Filter.of(
            "key", "key2",
            val1 -> "value", val2 -> "value",
            (value1, value2) -> DSL.trueCondition());

        filterValue3 = Filter.of(
            "key", "key2", "key3",
            val1 -> "value", val2 -> "value", val3 -> "value",
            value -> DSL.trueCondition());

        filterValue4 = Filter.of(
            "key", "key2", "key3", "key4",
            val1 -> "value", val2 -> "value", val3 -> "value", val4 -> "value",
            value -> DSL.trueCondition());

        filterValue5 = Filter.of(KeyParsers.of(
            "key", val1 -> "value", "key2", val1 -> "value", "key3", val1 -> "value", "key4", val1 -> "value", "key5", val1 -> "value"),
            value -> DSL.trueCondition());

        filterValue6 = Filter.of(KeyParsers.of(
            "key", val1 -> "value", "key2", val1 -> "value", "key3", val1 -> "value", "key4", val1 -> "value", "key5", val1 -> "value", "key6", val1 -> "value"),
            value -> DSL.trueCondition());
    }


    @Test
    void size() {
        Assertions.assertEquals(1, filterSupplier.size());

        Assertions.assertEquals(1, filterValue1.size());
        Assertions.assertEquals(2, filterValue2.size());
        Assertions.assertEquals(3, filterValue3.size());
        Assertions.assertEquals(4, filterValue4.size());
        Assertions.assertEquals(5, filterValue5.size());
        Assertions.assertEquals(6, filterValue6.size());
    }

    @Test
    void getKeyParser() {
        Assertions.assertNotNull(filterSupplier.getKeyParser());

        Assertions.assertNotNull(filterValue1.getKeyParser());
        Assertions.assertNotNull(filterValue2.getKeyParser());
        Assertions.assertNotNull(filterValue3.getKeyParser());
        Assertions.assertNotNull(filterValue4.getKeyParser());
        Assertions.assertNotNull(filterValue5.getKeyParser());
        Assertions.assertNotNull(filterValue6.getKeyParser());
    }


    @Test
    void getKeyParser1() {
        final KeyParser1 keyParser = ((FilterValue1) filterValue1).getKeyParser1();
        Assertions.assertNotNull(keyParser);
    }

    @Test
    void getKeyParser2() {
        final KeyParser2 keyParser = ((FilterValue2) filterValue2).getKeyParser2();
        Assertions.assertNotNull(keyParser);
    }

    @Test
    void getKeyParser3() {
        final KeyParser3 keyParser = ((FilterValue3) filterValue3).getKeyParser3();
        Assertions.assertNotNull(keyParser);
    }

    @Test
    void getKeyParser4() {
        final KeyParser4 keyParser = ((FilterValue4) filterValue4).getKeyParser4();
        Assertions.assertNotNull(keyParser);
    }

    @Test
    void getKeyParser5() {
        final KeyParser5 keyParser = ((FilterValue5) filterValue5).getKeyParser5();
        Assertions.assertNotNull(keyParser);
    }

    @Test
    void getKeyParser6() {
        final KeyParser6 keyParser = ((FilterValue6) filterValue6).getKeyParser6();
        Assertions.assertNotNull(keyParser);
    }

    @Test
    void isThrowOnMissingKeysFound() {
    }

}
