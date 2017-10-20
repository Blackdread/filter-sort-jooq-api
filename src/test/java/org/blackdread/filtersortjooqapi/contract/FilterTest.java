package org.blackdread.filtersortjooqapi.contract;

import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Yoann CAPLAIN on 2017/10/20.
 */
class FilterTest {

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

    @Test
    void buildConditionWithParamThrowsOnNullConditionReturned() {
        final FilterValue1<Integer> filter = Filter.of("myKey", valueToParse -> 5, value -> null);
        Assertions.assertThrows(IllegalStateException.class, () -> filter.buildCondition(Collections.singletonList(9)));
    }

    // TODO To investigate that behavior
    @Test
    void ofCustomParserButStrangeCastingBehaviorThrows() {
        final FilterValue1<Integer> filter = Filter.of("myKey", valueToParse -> 5, value -> DSL.trueCondition());
        Assertions.assertThrows(ClassCastException.class, () -> filter.buildCondition(Collections.singletonList("whatever")));
        Assertions.assertThrows(ClassCastException.class, () -> filter.buildCondition(Collections.singletonList("54")));
        Assertions.assertEquals(DSL.trueCondition(), filter.buildCondition(Collections.singletonList(56)));
    }

    // TODO To investigate that behavior
    @Test
    void ofCustomParserButStrangeCastingBehaviorThrows2() {
        final FilterValue filter = Filter.of("myKey", valueToParse -> 5, value -> DSL.trueCondition());
        Assertions.assertThrows(ClassCastException.class, () -> filter.buildCondition(Collections.singletonList("whatever")));
        Assertions.assertThrows(ClassCastException.class, () -> filter.buildCondition(Collections.singletonList("54")));
        Assertions.assertEquals(DSL.trueCondition(), filter.buildCondition(Collections.singletonList(56)));
    }

    // TODO To investigate that behavior
    @Test
    void ofCustomParserButStrangeCastingBehavior() {
        final FilterValue1<Object> filter = Filter.of("myKey", valueToParse -> 5, value -> DSL.trueCondition());
        Assertions.assertEquals(DSL.trueCondition(), filter.buildCondition(Collections.singletonList("whatever")));
        Assertions.assertEquals(DSL.trueCondition(), filter.buildCondition(Collections.singletonList("54")));
        Assertions.assertEquals(DSL.trueCondition(), filter.buildCondition(Collections.singletonList(56)));
    }

    @Test
    void ofCustomParser1Param() {
        final FilterValue1<Integer> filter = Filter.of("myKey", valueToParse -> 5, value -> DSL.trueCondition());
        final Condition condition = filter.buildCondition(Collections.singletonList(9));

        final int parserResult = filter.getKeyParser1().parser1().apply("whatever");
        Assertions.assertEquals(5, parserResult);
        Assertions.assertEquals(DSL.trueCondition(), condition);
    }

    @Test
    void ofCustomParser2Params() {
    }

    @Test
    void ofCustomParser3Params() {

    }

    @Test
    void of2() {
    }

    @Test
    void of3() {
    }

    @Test
    void of4() {
    }

    @Test
    void of5() {
    }

    @Test
    void of6() {
    }

    @Test
    void of7() {
    }

    @Test
    void of8() {
    }

    @Test
    void of9() {
    }

    @Test
    void of10() {
    }

    @Test
    void ofN() {
    }

}
