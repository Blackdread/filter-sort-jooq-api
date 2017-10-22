package org.blackdread.filtersortjooqapi.filter;

import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

public class FilterSupplierTest {

    @Test
    void ofSupplierThrowsIfNullReturned() {
        final FilterValue filter = Filter.of("myKey", () -> null);
        Assertions.assertThrows(IllegalStateException.class, filter::buildCondition);
    }

    @Test
    void ofSupplierNotNull() {
        final FilterValue filter = Filter.of("myKey", DSL::trueCondition);
        final Condition condition = filter.buildCondition();
        Assertions.assertNotNull(condition);
        Assertions.assertEquals(DSL.trueCondition(), condition);
    }

    @Test
    void ofSupplierThrowsIfWrongBuildCondition() {
        final FilterValue filter = Filter.of("myKey", () -> null);
        Assertions.assertThrows(IllegalStateException.class, () -> filter.buildCondition(Collections.singletonList("obj1")));
        Assertions.assertThrows(IllegalStateException.class, () -> filter.buildCondition(Arrays.asList("obj1", "obj2")));
        Assertions.assertThrows(IllegalStateException.class, () -> filter.buildCondition(Arrays.asList("obj1", "obj2", "obj3")));
    }

    @Test
    void supplierIsTrue() {
        final FilterValue filter = Filter.of("myKey", DSL::trueCondition);
        Assertions.assertTrue(filter.isConditionSupplier());
    }

    @Test
    void supplierIsNotTrueWithParams() {
        final FilterValue1<Integer> filter = Filter.of("myKey", valueToParse -> 5, value -> DSL.trueCondition());
        Assertions.assertFalse(filter.isConditionSupplier());
    }

    @Test
    void buildConditionNoArgumentsThrowsIfNoSupplier() {
        final FilterValue1<Integer> filter = Filter.of("myKey", valueToParse -> 5, value -> DSL.trueCondition());
        Assertions.assertThrows(IllegalStateException.class, filter::buildCondition);
    }

}
