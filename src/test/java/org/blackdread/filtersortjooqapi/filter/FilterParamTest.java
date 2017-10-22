package org.blackdread.filtersortjooqapi.filter;

import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class FilterParamTest {

    @Test
    void buildConditionWithParamThrowsOnNullConditionReturned() {
        final FilterValue1<Integer> filter = Filter.of("myKey", valueToParse -> 5, value -> null);
        Assertions.assertThrows(IllegalStateException.class, () -> filter.buildCondition(Collections.singletonList(9)));
    }

    // It throws as filter.buildCondition(...) is not supposed to be called from external of API, it fails due to generic is defined as Integer (but this is normal behavior)
    @Test
    void ofCustomParserAndManuallyCallBuildConditionWithWrongTypeThrows() {
        final FilterValue1<Integer> filter = Filter.of("myKey", valueToParse -> 5, value -> DSL.trueCondition());
        Assertions.assertThrows(ClassCastException.class, () -> filter.buildCondition(Collections.singletonList("whatever")));
        Assertions.assertThrows(ClassCastException.class, () -> filter.buildCondition(Collections.singletonList("54")));
        Assertions.assertEquals(DSL.trueCondition(), filter.buildCondition(Collections.singletonList(56)));
    }

    // It throws as filter.buildCondition(...) is not supposed to be called from external of API, it fails due to generic is defined as Integer (but this is normal behavior)
    @Test
    void ofCustomParserAndManuallyCallBuildConditionWithWrongTypeThrows2() {
        final FilterValue filter = Filter.of("myKey", valueToParse -> 5, value -> DSL.trueCondition());
        Assertions.assertThrows(ClassCastException.class, () -> filter.buildCondition(Collections.singletonList("whatever")));
        Assertions.assertThrows(ClassCastException.class, () -> filter.buildCondition(Collections.singletonList("54")));
        Assertions.assertEquals(DSL.trueCondition(), filter.buildCondition(Collections.singletonList(56)));
    }

    // Does not throw as there is no casting done to get an Object type
    @Test
    void ofCustomParserAndManuallyCallBuildConditionWithNoTypeSafety() {
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
}
