package org.blackdread.filtersortjooqapi.sort;

import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SortValueTest {

    private final Field intField = DSL.field("id", Integer.class);
    private final Field stringField = DSL.field("name", String.class);

    private final SortValue intSortValue = SortValue.of(intField);
    private final SortValue stringSortValue = SortValue.of(stringField);

    @Test
    void createSortValueWithNullThrows() {
        Assertions.assertThrows(NullPointerException.class, () -> SortValue.of((Field) null));
        Assertions.assertThrows(NullPointerException.class, () -> SortValue.of((TableField) null));
    }

    @Test
    void sortValueWorksWithCreatedField() {
        final SortValue ofInt = SortValue.of(intField);
        final SortValue ofString = SortValue.of(stringField);

        Assertions.assertNotNull(ofInt);
        Assertions.assertNotNull(ofString);
    }

    @Test
    void getSortFieldWorks() {
        final SortField sortFieldAscId = intSortValue.getSortField(SortOrder.ASC);
        final SortField sortFieldDescId = intSortValue.getSortField(SortOrder.DESC);

        Assertions.assertEquals("id", sortFieldAscId.getName());
        Assertions.assertEquals(SortOrder.ASC, sortFieldAscId.getOrder());

        Assertions.assertEquals("id", sortFieldDescId.getName());
        Assertions.assertEquals(SortOrder.DESC, sortFieldDescId.getOrder());


        final SortField sortFieldAscName = stringSortValue.getSortField(SortOrder.ASC);
        final SortField sortFieldDescName = stringSortValue.getSortField(SortOrder.DESC);

        Assertions.assertEquals("name", sortFieldAscName.getName());
        Assertions.assertEquals(SortOrder.ASC, sortFieldAscName.getOrder());

        Assertions.assertEquals("name", sortFieldDescName.getName());
        Assertions.assertEquals(SortOrder.DESC, sortFieldDescName.getOrder());
    }

    @Test
    void getTableField() {
        // TODO need to create a TableField
//        Assertions.assertNotNull(ofTableField.getTableField());
//        Assertions.assertNotSame(Optional.empty(), ofTableField.getTableField());
//        Assertions.assertEquals(Optional.empty(), ofTableField.getField());
//        Assertions.assertNotNull(ofTableField.getField().get());
    }

    @Test
    void getField() {
        final SortValue ofInt = SortValue.of(intField);

        Assertions.assertNotNull(ofInt.getField());
        Assertions.assertNotSame(Optional.empty(), ofInt.getField());
        Assertions.assertEquals(Optional.empty(), ofInt.getTableField());
        Assertions.assertNotNull(ofInt.getField().get());
        Assertions.assertEquals(intField, ofInt.getField().get());
    }

    @Test
    void getSortNullFirst() {
        final SortField sortFieldNullFirstAscInt = intSortValue.getSortFieldNullFirst(SortOrder.ASC);
        final SortField sortFieldNullFirstDescInt = intSortValue.getSortFieldNullFirst(SortOrder.DESC);

        final SortField sortFieldNullFirstAscString = stringSortValue.getSortFieldNullFirst(SortOrder.ASC);
        final SortField sortFieldNullFirstDescString = stringSortValue.getSortFieldNullFirst(SortOrder.DESC);

        Assertions.assertNotNull(sortFieldNullFirstAscInt);
        Assertions.assertNotNull(sortFieldNullFirstDescInt);
        Assertions.assertNotNull(sortFieldNullFirstAscString);
        Assertions.assertNotNull(sortFieldNullFirstDescString);
    }

    @Test
    void getSortNullLast() {
        final SortField sortFieldNullLastAscInt = intSortValue.getSortFieldNullLast(SortOrder.ASC);
        final SortField sortFieldNullLastDescInt = intSortValue.getSortFieldNullLast(SortOrder.DESC);

        final SortField sortFieldNullLastAscString = stringSortValue.getSortFieldNullLast(SortOrder.ASC);
        final SortField sortFieldNullLastDescString = stringSortValue.getSortFieldNullLast(SortOrder.DESC);

        Assertions.assertNotNull(sortFieldNullLastAscInt);
        Assertions.assertNotNull(sortFieldNullLastDescInt);
        Assertions.assertNotNull(sortFieldNullLastAscString);
        Assertions.assertNotNull(sortFieldNullLastDescString);
    }
}
