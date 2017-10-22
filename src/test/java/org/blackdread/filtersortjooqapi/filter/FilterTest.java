package org.blackdread.filtersortjooqapi.filter;

import org.blackdread.filtersortjooqapi.filter.keyparser.*;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Created by Yoann CAPLAIN on 2017/10/20.
 */
class FilterTest {

    @Test
    void throwWithDuplicateKeySameFilter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Filter.of("key", "key", val1 -> "value", val2 -> "value", (value1, value2) -> DSL.trueCondition()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Filter.of("key", "key", "key3", val1 -> "value", val2 -> "value", val3 -> "value", value3 -> DSL.trueCondition()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Filter.of("key", "key2", "key", "key4", val1 -> "value", val2 -> "value", val3 -> "value", val4 -> "value", value4 -> DSL.trueCondition()));

        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.ofN(
            Arrays.asList("key", "key1", "key2", "key", "key4", "key5", "key6", "key6", "key8"),
            Arrays.asList(val1 -> "value", val1 -> "value", val1 -> "value", val1 -> "value", val1 -> "value", val1 -> "value", val1 -> "value", val1 -> "value", val1 -> "value")
        ));
    }

    @Test
    void ofSupplier() {
        final FilterValue supplierFilter = Filter.of("key", DSL::trueCondition);
        Assertions.assertEquals(1, supplierFilter.size());
        Assertions.assertNotNull(supplierFilter);
    }

    @Test
    void of1() {
        final FilterValue filterValue1 = Filter.of("key", val -> "value", value -> DSL.trueCondition());
        Assertions.assertEquals(1, filterValue1.size());
        Assertions.assertNotNull(filterValue1);
    }

    @Test
    void of1KeyParser() {
        final FilterValue filterValue1 = Filter.of(KeyParsers.of("key", val -> "value"), value -> DSL.trueCondition());
        Assertions.assertEquals(1, filterValue1.size());
        Assertions.assertNotNull(filterValue1);
    }

    @Test
    void of2() {
        final FilterValue filterValue2 = Filter.of(
            "key", "key2",
            val1 -> "value", val2 -> "value",
            (value1, value2) -> DSL.trueCondition());
        Assertions.assertEquals(2, filterValue2.size());
        Assertions.assertNotNull(filterValue2);
    }

    @Test
    void of2KeyParser() {
        final FilterValue filterValue2 =
            Filter.of(
                KeyParsers.of("key", val1 -> "value", "key2", val2 -> "value"),
                (value1, value2) -> DSL.trueCondition());
        Assertions.assertEquals(2, filterValue2.size());
        Assertions.assertNotNull(filterValue2);
    }

    @Test
    void of3() {
        final FilterValue filterValue3 = Filter.of(
            "key", "key2", "key3",
            val1 -> "value", val2 -> "value", val3 -> "value",
            value -> DSL.trueCondition());
        Assertions.assertEquals(3, filterValue3.size());
        Assertions.assertNotNull(filterValue3);
    }

    @Test
    void of3KeyParser() {
        final FilterValue filterValue3 = Filter.of(
            KeyParsers.of("key", val1 -> "value", "key2", val2 -> "value", "key3", val3 -> "value"),
            value -> DSL.trueCondition());
        Assertions.assertEquals(3, filterValue3.size());
        Assertions.assertNotNull(filterValue3);
    }

    @Test
    void of4() {
        final FilterValue filterValue4 = Filter.of(
            "key", "key2", "key3", "key4",
            val1 -> "value", val2 -> "value", val3 -> "value", val4 -> "value",
            value -> DSL.trueCondition());
        Assertions.assertEquals(4, filterValue4.size());
        Assertions.assertNotNull(filterValue4);
    }

    @Test
    void of4KeyParser() {
        final FilterValue filterValue4 = Filter.of(
            KeyParsers.of("key", val1 -> "value", "key2", val2 -> "value", "key3", val3 -> "value", "key4", val4 -> "value"),
            value -> DSL.trueCondition());
        Assertions.assertEquals(4, filterValue4.size());
        Assertions.assertNotNull(filterValue4);
    }

    @Test
    void of5() {
        final KeyParser5<String, String, String, String, String> keyParser5 =
            KeyParsers.of("key", val1 -> "value", "key2", val1 -> "value", "key3", val1 -> "value", "key4", val1 -> "value", "key5", val1 -> "value");
        final FilterValue filterValue5 = Filter.of(keyParser5, value5 -> DSL.trueCondition());
        Assertions.assertEquals(5, filterValue5.size());
        Assertions.assertNotNull(filterValue5);
    }

    @Test
    void of6() {
        final KeyParser6<String, String, String, String, String, String> keyParser6 =
            KeyParsers.of("key", val1 -> "value", "key2", val1 -> "value", "key3", val1 -> "value", "key4", val1 -> "value", "key5", val1 -> "value", "key6", val1 -> "value");
        final FilterValue filterValue6 = Filter.of(keyParser6, value6 -> DSL.trueCondition());
        Assertions.assertEquals(6, filterValue6.size());
        Assertions.assertNotNull(filterValue6);
    }

    @Test
    void ofNThrowsLessThan2() {
        final KeyParser1<String> keyParser1 = KeyParsers.of("key1", val -> "value");
        final KeyParser2<String, String> keyParser2 = KeyParsers.of("key1", val -> "value", "key2", val -> "value");
        Assertions.assertThrows(IllegalArgumentException.class, () -> Filter.ofN(keyParser1, value -> DSL.trueCondition()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Filter.ofN(keyParser2, value -> DSL.trueCondition()));
    }

    @Test
    void ofN() {
        final KeyParser keyParserN = KeyParsers.ofN(
            Arrays.asList("key", "key1", "key2", "key3", "key4", "key5", "key6", "key7", "key8"),
            Arrays.asList(val1 -> "value", val1 -> "value", val1 -> "value", val1 -> "value", val1 -> "value", val1 -> "value", val1 -> "value", val1 -> "value", val1 -> "value")
        );
        final FilterValue filterValueN = Filter.ofN(keyParserN, value -> DSL.trueCondition());
        Assertions.assertEquals(9, filterValueN.size());
        Assertions.assertNotNull(filterValueN);
    }

}
