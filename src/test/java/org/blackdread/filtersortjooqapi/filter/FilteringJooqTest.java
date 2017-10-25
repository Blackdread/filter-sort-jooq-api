package org.blackdread.filtersortjooqapi.filter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.blackdread.filtersortjooqapi.filter.parser.FilterParser;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class FilteringJooqTest {

    private static final Map<String, String> emptyMap = new HashMap<>();

    private static final Map<String, String> notEmptyMap = Maps.asMap(Sets.newHashSet("key1", "key2", "key3"), a -> a + "val");

    private static final Map<String, String> mapWithNullValues = Maps.asMap(Sets.newHashSet("key1", "key2", "key3"), a -> null);

    private static final Map<String, String> mapWithEmptyValues = Maps.asMap(Sets.newHashSet("key1", "key2", "key3"), a -> "");
    private static final Map<String, String> mapWithSpaceValues = Maps.asMap(Sets.newHashSet("key1", "key2", "key3"), a -> "   ");
    private FilteringJooqImpl1 filteringJooqImpl1;

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
                createFilterValueTrueConditionPrefixedN("key", 5),
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

    static Stream<Arguments> incompleteMapOfFilterMultipleKeys() {
        return Stream.of(
            Arguments.of(
                ImmutableMap.of("key1", "value1", "key2", "12:25:30", "key3", "2017-05-17T12:25:30"),
                Arrays.asList(Filter.of("key1", "missingKey", v1 -> "val1", v2 -> "val2", (val1, val2) -> DSL.trueCondition()), Filter.of("key3", DSL::trueCondition)))
        );
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

    private static List<FilterValue> createFilterValueTrueConditionPrefixedN(final String prefix, final int n) {
        if (n < 1)
            throw new IllegalArgumentException("Cannot have n < 1");
        return IntStream.range(1, n + 1)
            .mapToObj(i -> Filter.of(prefix + i, DSL::trueCondition))
            .collect(Collectors.toList());
    }

    private static List<FilterValue> createFilterValueTrueConditionByKeys(final String... keys) {
        if (keys.length < 1)
            throw new IllegalArgumentException("Cannot have keys length < 1");
        return Arrays.stream(keys)
            .map(key -> Filter.of(key, DSL::trueCondition))
            .collect(Collectors.toList());
    }

    @BeforeEach
    void setUp() {
        filteringJooqImpl1 = new FilteringJooqImpl1();
    }

    @AfterEach
    void teardDown() {

    }

    @ParameterizedTest
    @MethodSource("goodMapConditionAndResult")
    void buildConditions(final Map<String, String> params, final List<FilterValue> filterValues, final Condition expectedCondition) {
        filteringJooqImpl1.getFilterValues().addAll(filterValues);
        final Condition condition = filteringJooqImpl1.buildConditions(params);
        Assertions.assertEquals(expectedCondition, condition);
    }

    @ParameterizedTest
    @MethodSource("incompleteMapOfFilterMultipleKeys")
    void buildConditionsThrowsByDefaultForMissingKeyMultiple(final Map<String, String> params, final List<FilterValue> filterValues) {
        filteringJooqImpl1.getFilterValues().addAll(filterValues);
        Assertions.assertThrows(FilteringApiException.class, () -> filteringJooqImpl1.buildConditions(params));
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

    @Test
    void buildConditionsDoesNotThrowsOnMissingValueForConditionSupplier() {
        filteringJooqImpl1.getFilterValues().add(Filter.of("key1", DSL::trueCondition));
        filteringJooqImpl1.getFilterValues().add(Filter.of("key2", DSL::trueCondition));
        Assertions.assertEquals(createNTrueCondition(2), filteringJooqImpl1.buildConditions(ImmutableMap.of("key1", "", "key2", "12:25:30")));
        Assertions.assertEquals(createNTrueCondition(1), filteringJooqImpl1.buildConditions(ImmutableMap.of("key1", "   ")));
    }

    @Test
    void buildConditionsThrowsOnMissingValueForConditionCreator() {
        filteringJooqImpl1.getFilterValues().add(Filter.of("key1", v1 -> "val1", val1 -> DSL.trueCondition()));
        filteringJooqImpl1.getFilterValues().add(Filter.of("key2", v1 -> "val1", val1 -> DSL.trueCondition()));
        Assertions.assertThrows(FilteringApiException.class, () -> filteringJooqImpl1.buildConditions(ImmutableMap.of("key1", "", "key2", "12:25:30")));
        Assertions.assertThrows(FilteringApiException.class, () -> filteringJooqImpl1.buildConditions(ImmutableMap.of("key1", "   ")));
    }

    @Test
    void buildConditionsSkipOnMissingValueForConditionCreator() {
        // It skips by setting the value to "" but that will throw an exception at parsing if parser expect something
        final FilteringJooqImpl2 filteringJooqImpl2 = new FilteringJooqImpl2();
        filteringJooqImpl2.getFilterValues().add(Filter.of("key1", v1 -> "val1", val1 -> DSL.trueCondition()));
        filteringJooqImpl2.getFilterValues().add(Filter.of("key2", v1 -> "val1", val1 -> DSL.trueCondition()));
        // Does not throw as parser is not parsing ^^
        Assertions.assertEquals(createNTrueCondition(2), filteringJooqImpl2.buildConditions(ImmutableMap.of("key1", "", "key2", "12:25:30")));
        Assertions.assertEquals(createNTrueCondition(1), filteringJooqImpl2.buildConditions(ImmutableMap.of("key1", "   ")));

        // Now it will throw
        filteringJooqImpl2.getFilterValues().set(0, Filter.of("key", LocalDateTime::parse, val1 -> DSL.trueCondition()));
        Assertions.assertThrows(FilteringApiException.class, () -> filteringJooqImpl2.buildConditions(ImmutableMap.of("key1", "", "key2", "12:25:30")));
        Assertions.assertThrows(FilteringApiException.class, () -> filteringJooqImpl2.buildConditions(ImmutableMap.of("key1", "   ")));
    }

    @Test
    void buildConditionsSkipOnMissingValueButThrowsAtParsing() {
        final FilteringJooqImpl2 filteringJooqImpl2 = new FilteringJooqImpl2();
        filteringJooqImpl2.getFilterValues().add(Filter.of("key1", FilterParser.ofDateTime()::parseWithApiException, val1 -> DSL.trueCondition()));
        filteringJooqImpl2.getFilterValues().add(Filter.of("key2", FilterParser.ofDate()::parseWithApiException, val1 -> DSL.trueCondition()));
        filteringJooqImpl2.getFilterValues().add(Filter.of("key3", FilterParser.ofTime()::parseWithApiException, val1 -> DSL.trueCondition()));
        filteringJooqImpl2.getFilterValues().add(Filter.of("key4", FilterParser.ofInt()::parseWithApiException, val1 -> DSL.trueCondition()));
        filteringJooqImpl2.getFilterValues().add(Filter.of("key5", FilterParser.ofByte()::parseWithApiException, val1 -> DSL.trueCondition()));
        Assertions.assertThrows(FilteringApiException.class, () -> filteringJooqImpl2.buildConditions(ImmutableMap.of("key1", "", "key2", "12:25:30")));
        Assertions.assertThrows(FilteringApiException.class, () -> filteringJooqImpl2.buildConditions(ImmutableMap.of("key1", "   ")));
        Assertions.assertThrows(FilteringApiException.class, () -> filteringJooqImpl2.buildConditions(ImmutableMap.of("key2", "   ")));
        Assertions.assertThrows(FilteringApiException.class, () -> filteringJooqImpl2.buildConditions(ImmutableMap.of("key3", "   ")));
        Assertions.assertThrows(FilteringApiException.class, () -> filteringJooqImpl2.buildConditions(ImmutableMap.of("key4", "   ")));
        Assertions.assertThrows(FilteringApiException.class, () -> filteringJooqImpl2.buildConditions(ImmutableMap.of("key5", "   ")));
    }

    @Test
    void buildConditionsIgnoreSomeKeys() {
        filteringJooqImpl1.getFilterValues().addAll(Arrays.asList(Filter.of("key1", DSL::trueCondition), Filter.of("key2", DSL::trueCondition)));
        Assertions.assertEquals(DSL.trueCondition(), filteringJooqImpl1.buildConditions(ImmutableMap.of("sort", "fsfsefes", "size", "sdsdsd", "page", "6842")));
    }

}
