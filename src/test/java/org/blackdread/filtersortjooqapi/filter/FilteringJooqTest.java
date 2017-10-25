package org.blackdread.filtersortjooqapi.filter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

class FilteringJooqTest {

    private static final Map<String, String> emptyMap = new HashMap<>();

    private static final Map<String, String> notEmptyMap = Maps.asMap(Sets.newHashSet("key1", "key2", "key3"), a -> a + "val");

    private static final Map<String, String> mapWithNullValues = Maps.asMap(Sets.newHashSet("key1", "key2", "key3"), a -> null);

    private static final Map<String, String> mapWithEmptyValues = Maps.asMap(Sets.newHashSet("key1", "key2", "key3"), a -> "");
    private static final Map<String, String> mapWithSpaceValues = Maps.asMap(Sets.newHashSet("key1", "key2", "key3"), a -> "   ");

    // TODO Add more good ones and create another for bad ones
    static Stream<Arguments> goodMapConditionAndResult() {
        return Stream.of(
            Arguments.of(
                ImmutableMap.of(),
                Collections.emptyList(),
                DSL.trueCondition()
            ),
            Arguments.of(
                // this one makes no sense but this is correct ^^
                ImmutableMap.of("key1", "value1", "key2", "12:25:30", "key3", "2017-05-17T12:25:30"),
                Arrays.asList(Filter.of("key1", DSL::trueCondition), Filter.of("key2", DSL::trueCondition),
                    Filter.of("key3", DSL::trueCondition), Filter.of("key4", DSL::trueCondition),
                    Filter.of("key5", DSL::trueCondition)),
                createNTrueCondition(3)
            ),
            // TODO might fail sometimes as Map of Impl is not predictable in ordering
            Arguments.of(
                ImmutableMap.of("key1", "value1", "key2", "12:25:30", "key3", "2017-05-17T12:25:30"),
                Arrays.asList(Filter.of(
                    "key1", "key2",
                    v1 -> "val1", v2 -> LocalTime.of(12, 23, 34),
                    (v1, v2) -> DSL.field("name", String.class).likeIgnoreCase(v1 + "%")
                        .and(DSL.field("atime", Time.class).ge(Time.valueOf(v2)))),
                    Filter.of("key3", LocalDateTime::parse, v1 -> DSL.field("a_date_time", Timestamp.class).lessThan(Timestamp.valueOf(v1))),
                    Filter.of("key4", DSL::trueCondition), Filter.of("key5", DSL::trueCondition)),
                DSL.field("name", String.class).likeIgnoreCase("val1%")
                    .and(DSL.field("atime", Time.class).ge(Time.valueOf("12:23:34")))
                    .and(DSL.field("a_date_time", Timestamp.class).lessThan(Timestamp.valueOf("2017-05-17 12:25:30"))))
        );
    }

    private FilteringJooqImpl1 filteringJooqImpl1;

    @BeforeEach
    void setUp() {
        filteringJooqImpl1 = new FilteringJooqImpl1();
    }

    @AfterEach
    void teardDown() {

    }

    private static Condition createNTrueCondition(final int n) {
        if (n < 1)
            throw new IllegalArgumentException("Cannot have n < 1");
        Condition condition = null;
        for (int i = 0; i < n; i++) {
            if (condition == null)
                condition = DSL.trueCondition();
            else
                condition = condition.and(DSL.trueCondition());
        }
        return condition;
    }

    @ParameterizedTest
    @MethodSource("goodMapConditionAndResult")
    void buildConditions(final Map<String, String> params, final List<FilterValue> filterValues, final Condition expectedCondition) {
        filteringJooqImpl1.getFilterValues().addAll(filterValues);
        final Condition condition = filteringJooqImpl1.buildConditions(params);
        Assertions.assertEquals(expectedCondition, condition);
    }

    @Test
    void getValueWorksBasicOfMap() {
        Assertions.assertFalse(filteringJooqImpl1.getValue(emptyMap, "myKey").isPresent());
        Assertions.assertFalse(filteringJooqImpl1.getValue(notEmptyMap, "myKey").isPresent());

        Assertions.assertFalse(filteringJooqImpl1.getValue(notEmptyMap, "myKey").isPresent());
        Assertions.assertTrue(filteringJooqImpl1.getValue(notEmptyMap, "key1").isPresent());
    }

    @Test
    void getValueIgnoreEmptyValues() {
        Assertions.assertFalse(filteringJooqImpl1.getValue(mapWithNullValues, "key1").isPresent());
        Assertions.assertFalse(filteringJooqImpl1.getValue(mapWithEmptyValues, "key1").isPresent());
        Assertions.assertFalse(filteringJooqImpl1.getValue(mapWithSpaceValues, "key1").isPresent());
    }

    @RepeatedTest(10)
    void getFilterGetsSameFirstKeyFoundOnDuplicates() {
        filteringJooqImpl1.getFilterValues().add(Filter.of("myKey", DSL::trueCondition));
        filteringJooqImpl1.getFilterValues().add(Filter.of("myKey2", DSL::trueCondition));
        filteringJooqImpl1.getFilterValues().add(Filter.of("myKey", notParsed -> 5, val -> DSL.trueCondition()));
        final Optional<FilterValue> myKey = filteringJooqImpl1.getFilter("myKey");
        Assertions.assertTrue(myKey.isPresent());
        Assertions.assertTrue(myKey.get().isConditionSupplier());
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(myKey.get(), filteringJooqImpl1.getFilter("myKey").get());
            Assertions.assertTrue(filteringJooqImpl1.getFilter("myKey").get().isConditionSupplier());
        }
    }

    @Test
    void getFilterEmptyForMissingKey() {
        Assertions.assertFalse(filteringJooqImpl1.getFilter("myKey").isPresent());
    }

    @Test
    void getFilterValues() {
        Assertions.assertNotNull(filteringJooqImpl1.getFilterValues());
        Assertions.assertTrue(filteringJooqImpl1.getFilterValues().isEmpty());
    }

    @Test
    void ignoredKeysHasSpecificDefaultValues() {
        Assertions.assertTrue(FilteringJooq.DEFAULT_IGNORED_KEY_FOR_FILTERING.contains("sort"));
        Assertions.assertTrue(FilteringJooq.DEFAULT_IGNORED_KEY_FOR_FILTERING.contains("page"));
        Assertions.assertTrue(FilteringJooq.DEFAULT_IGNORED_KEY_FOR_FILTERING.contains("size"));
        Assertions.assertEquals(3, FilteringJooq.DEFAULT_IGNORED_KEY_FOR_FILTERING.size());
    }

    @Test
    void ignoredKeysIsImmutable() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> filteringJooqImpl1.getIgnoredKeys().add("any"));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> FilteringJooq.DEFAULT_IGNORED_KEY_FOR_FILTERING.add("any"));
    }

    @Test
    @SuppressWarnings("deprecation")
    void isSkipNotPresentValueFalseByDefault() {
        Assertions.assertFalse(filteringJooqImpl1.isSkipNotPresentValue());
    }

    @Test
    @SuppressWarnings("deprecation")
    void getCondition() {
        Assertions.assertFalse(filteringJooqImpl1.getCondition(emptyMap, "any", s -> DSL.trueCondition()).isPresent());
        Assertions.assertFalse(filteringJooqImpl1.getCondition(notEmptyMap, "any", s -> DSL.trueCondition()).isPresent());
        Assertions.assertTrue(filteringJooqImpl1.getCondition(notEmptyMap, "key1", s -> DSL.trueCondition()).isPresent());
        Assertions.assertFalse(filteringJooqImpl1.getCondition(mapWithNullValues, "key1", s -> DSL.trueCondition()).isPresent());
        Assertions.assertFalse(filteringJooqImpl1.getCondition(mapWithEmptyValues, "key1", s -> DSL.trueCondition()).isPresent());
        Assertions.assertFalse(filteringJooqImpl1.getCondition(mapWithSpaceValues, "key1", s -> DSL.trueCondition()).isPresent());
    }

    @Test
    @SuppressWarnings("deprecation")
    void getConditionOrNull() {
        Assertions.assertNull(filteringJooqImpl1.getConditionOrNull(emptyMap, "any", s -> DSL.trueCondition()));
        Assertions.assertNull(filteringJooqImpl1.getConditionOrNull(notEmptyMap, "any", s -> DSL.trueCondition()));
        Assertions.assertNotNull(filteringJooqImpl1.getConditionOrNull(notEmptyMap, "key1", s -> DSL.trueCondition()));
        Assertions.assertNull(filteringJooqImpl1.getConditionOrNull(mapWithNullValues, "key1", s -> DSL.trueCondition()));
        Assertions.assertNull(filteringJooqImpl1.getConditionOrNull(mapWithEmptyValues, "key1", s -> DSL.trueCondition()));
        Assertions.assertNull(filteringJooqImpl1.getConditionOrNull(mapWithSpaceValues, "key1", s -> DSL.trueCondition()));
    }

    @Test
    void buildConditionsThrowsOnNotFoundFilterKey() {
        filteringJooqImpl1.getFilterValues().add(Filter.of("key1", DSL::trueCondition));
        Assertions.assertThrows(FilteringApiException.class, () -> filteringJooqImpl1.buildConditions(ImmutableMap.of("key1", "value1", "key2", "12:25:30")));
    }
}
