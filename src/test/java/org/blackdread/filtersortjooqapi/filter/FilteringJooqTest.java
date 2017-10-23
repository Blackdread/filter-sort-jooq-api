package org.blackdread.filtersortjooqapi.filter;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class FilteringJooqTest {

    private static final Map<String, String> emptyMap = new HashMap<>();

    private static final Map<String, String> notEmptyMap = Maps.asMap(Sets.newHashSet("key1", "key2", "key3"), a -> a + "val");

    private static final Map<String, String> mapWithNullValues = Maps.asMap(Sets.newHashSet("key1", "key2", "key3"), a -> null);

    private static final Map<String, String> mapWithEmptyValues = Maps.asMap(Sets.newHashSet("key1", "key2", "key3"), a -> "");
    private static final Map<String, String> mapWithSpaceValues = Maps.asMap(Sets.newHashSet("key1", "key2", "key3"), a -> "   ");

    private FilteringJooqImpl1 filteringJooqImpl1;

    @BeforeEach
    void setUp() {
        filteringJooqImpl1 = new FilteringJooqImpl1();
    }

    @AfterEach
    void teardDown() {

    }

    @Test
    void buildConditions() {
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

}
