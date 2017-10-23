package org.blackdread.filtersortjooqapi.sort;

import org.blackdread.filtersortjooqapi.exception.SortingApiException;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Yoann CAPLAIN on 2017/10/23.
 */
class SortingJooqTest {

    private SortingJooqImpl1 sortingJooqImpl1;


    @BeforeEach
    void setUp() {
        sortingJooqImpl1 = new SortingJooqImpl1();
    }

    @AfterEach
    void teardDown() {

    }

    @Test
    void emptySortingIsEmpty() {
        Assertions.assertEquals(true, sortingJooqImpl1.emptySorting().isEmpty());
    }

    @Test
    void emptySortingIsNotModifiable() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> sortingJooqImpl1.emptySorting().add(DSL.field("any", Integer.class).asc()));
    }

    @Test
    void buildOrderByWithEmptySort() {
        final Collection<SortField<?>> sortFields = sortingJooqImpl1.buildOrderBy(Sort.unsorted());
        Assertions.assertNotNull(sortFields);
        Assertions.assertTrue(sortFields.isEmpty());
    }

    @Test
    void buildOrderByThrowsWhenNotFoundSortKey() {
        Assertions.assertThrows(SortingApiException.class, () -> sortingJooqImpl1.buildOrderBy(Sort.by(Sort.Order.asc("id"), Sort.Order.desc("asdas"), Sort.Order.asc("dwadw"))));
    }

    @Test
    void buildOrderByWithNullParam() {
        Assertions.assertNotNull(sortingJooqImpl1.buildOrderBy(null));
        Assertions.assertTrue(sortingJooqImpl1.buildOrderBy(null).isEmpty());
    }

    @Test
    void buildOrderByWithDuplicatesThrows() {
        sortingJooqImpl1.getSortAliasMapping().put("id", SortValue.of(DSL.field("col_id", Integer.class)));
        sortingJooqImpl1.getSortAliasMapping().put("name", SortValue.of(DSL.field("any", String.class)));
        Assertions.assertThrows(SortingApiException.class, () -> sortingJooqImpl1.buildOrderBy(Sort.by(Sort.Order.asc("id"), Sort.Order.desc("id"), Sort.Order.asc("id"))));
    }

    @Test
    void buildOrderByWorks() {
        sortingJooqImpl1.getSortAliasMapping().put("id", SortValue.of(DSL.field("col_id", Integer.class)));
        sortingJooqImpl1.getSortAliasMapping().put("name", SortValue.of(DSL.field("any", String.class)));
        sortingJooqImpl1.getSortAliasMapping().put("listBla", SortValue.of(DSL.field("a_column", String.class)));
        sortingJooqImpl1.getSortAliasMapping().put("other", SortValue.of(DSL.field("another", Timestamp.class)));
        final Collection<SortField<?>> sortFields = sortingJooqImpl1.buildOrderBy(Sort.by(Sort.Order.asc("id"), Sort.Order.desc("listBla"), Sort.Order.asc("other")));
        Assertions.assertNotNull(sortFields);
        Assertions.assertFalse(sortFields.isEmpty());
        Assertions.assertEquals(3, sortFields.size());
    }


    @Test
    void buildOrderBySortBySortOrderGiven() {
        sortingJooqImpl1.getSortAliasMapping().put("name", SortValue.of(DSL.field("any", String.class)));
        sortingJooqImpl1.getSortAliasMapping().put("id", SortValue.of(DSL.field("col_id", Integer.class)));
        sortingJooqImpl1.getSortAliasMapping().put("listBla", SortValue.of(DSL.field("a_column", String.class)));
        sortingJooqImpl1.getSortAliasMapping().put("other", SortValue.of(DSL.field("another", Timestamp.class)));
        final Collection<SortField<?>> sortFields1 = sortingJooqImpl1.buildOrderBy(Sort.by(Sort.Order.asc("id"), Sort.Order.desc("listBla"), Sort.Order.asc("other")));
        final Collection<SortField<?>> sortFields2 = sortingJooqImpl1.buildOrderBy(Sort.by(Sort.Order.desc("listBla"), Sort.Order.asc("other"), Sort.Order.asc("id")));
        Assertions.assertEquals(3, sortFields1.size());
        Assertions.assertEquals(3, sortFields2.size());
        final SortField[] sortField1 = sortFields1.toArray(new SortField[3]);
        final SortField[] sortField2 = sortFields2.toArray(new SortField[3]);

        Assertions.assertEquals("col_id", sortField1[0].getName());
        Assertions.assertEquals("a_column", sortField1[1].getName());
        Assertions.assertEquals("another", sortField1[2].getName());
        Assertions.assertEquals(SortOrder.ASC, sortField1[0].getOrder());
        Assertions.assertEquals(SortOrder.DESC, sortField1[1].getOrder());
        Assertions.assertEquals(SortOrder.ASC, sortField1[2].getOrder());

        Assertions.assertEquals("a_column", sortField2[0].getName());
        Assertions.assertEquals("another", sortField2[1].getName());
        Assertions.assertEquals("col_id", sortField2[2].getName());
        Assertions.assertEquals(SortOrder.DESC, sortField2[0].getOrder());
        Assertions.assertEquals(SortOrder.ASC, sortField2[1].getOrder());
        Assertions.assertEquals(SortOrder.ASC, sortField2[2].getOrder());
    }

    @Test
    void getTableFieldWorks() {
        sortingJooqImpl1.getSortAliasMapping().put("myKey", SortValue.of(DSL.field("any", Integer.class)));
        Assertions.assertNotNull(sortingJooqImpl1.getTableField("myKey"));
        Assertions.assertEquals("any", sortingJooqImpl1.getTableField("myKey").getSortField(SortOrder.ASC).getName());

    }

    @Test
    void getTableFieldThrowsIfNotFound() {
        Assertions.assertThrows(SortingApiException.class, () -> sortingJooqImpl1.getTableField("blabla"));
    }

    @Test
    void convertTableFieldToSortField() {
        final SortValue sortValue = SortValue.of(DSL.field("any", Integer.class));
        Assertions.assertNotNull(sortingJooqImpl1.convertTableFieldToSortField(sortValue, Sort.Direction.ASC));
        Assertions.assertNotNull(sortingJooqImpl1.convertTableFieldToSortField(sortValue, Sort.Direction.DESC));
    }

    @Test
    void getSortAliasMapping() {
        Assertions.assertNotNull(sortingJooqImpl1.getSortAliasMapping());
    }

    @Test
    void defaultSortingIsEmptyAndNeverNull() {
        Assertions.assertNotNull(sortingJooqImpl1.getDefaultSorting());
        Assertions.assertTrue(sortingJooqImpl1.getDefaultSorting().isEmpty());
    }

    @Test
    void defaultSortingIsNotModifiable() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> sortingJooqImpl1.getDefaultSorting().add(DSL.field("any", Integer.class).asc()));
    }

    @Test
    void defaultSortingIsNotEmptyIfOneSortDefined() {
        sortingJooqImpl1.getSortAliasMapping().put("myKey", SortValue.of(DSL.field("any", Integer.class)));
        Assertions.assertNotNull(sortingJooqImpl1.getDefaultSorting());
        Assertions.assertFalse(sortingJooqImpl1.getDefaultSorting().isEmpty());
        Assertions.assertEquals(1, sortingJooqImpl1.getDefaultSorting().size());
    }

}
