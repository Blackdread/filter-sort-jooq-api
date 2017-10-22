package org.blackdread.filtersortjooqapi.filter;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by Yoann CAPLAIN on 2017/8/25.
 */
public interface FilteringJooq {

    // TODO Idea for Filter chaining AND/OR -> so kind of strategy pattern -> default on is all are AND, if overridden then it follow the one given (done via default method and overrides), we pass the whole context -> all values that matched and are ready to be filtered

    static final List<String> DEFAULT_IGNORED_KEY_FOR_FILTERING = ImmutableList.of("sort", "page", "size");

    /**
     * @param requestParams Keys and values to filter on
     * @return Fully constructed condition chaining (with nested conditions if implementation has)
     * @throws FilteringApiException if any filter field given cannot be found (it should result in a 400 error message)
     */
    @NotNull
    default Condition buildConditions(final Map<String, String> requestParams) {
        final List<Condition> conditions = new ArrayList<>(requestParams.size());

//        final List<String> usedKeys = new ArrayList<>(requestParams.size());
        final List<String> usedKeys = new ArrayList<>(getFilterValues().stream()
            .mapToInt(FilterValue::size)
            .sum());

        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            if (getIgnoredKeys().contains(entry.getKey()))
                continue;

            if (usedKeys.contains(entry.getKey()))
                continue;

            final FilterValue filterValue = getFilter(entry.getKey())
                .orElseThrow(() -> new FilteringApiException("No filter found with key (" + entry.getKey() + ")"));

            final List<String> filterValueKeys = filterValue.getKeyParser().keys();

            if (!requestParams.keySet().containsAll(filterValueKeys)) {

                if (filterValue.isThrowOnMissingKeysFound()) {
                    throw new FilteringApiException("Filter is expecting all keys to be present (" +
                        String.join(",", filterValueKeys) + ")");
                } else {
                    continue;
                }
            }

            usedKeys.addAll(filterValueKeys);

            if (!filterValue.isConditionSupplier()) {
                final ArrayList<String> valuesOfKeys = new ArrayList<>();
                for (String key : filterValueKeys) {
                    final Optional<String> value = getValue(requestParams, key);
                    if (!value.isPresent()) {
                        //TODO Deprecated
                        if (isSkipNotPresentValue()) {
                            valuesOfKeys.add("");
                            continue;
                        } else {
                            throw new FilteringApiException("Value is empty for filter key (" + entry.getKey() + ")");
                        }
                    }
                    valuesOfKeys.add(value.get());
                }

                final Function<String, ?>[] parsers = filterValue.getKeyParser().getParsers();

                if (parsers.length != valuesOfKeys.size())
                    throw new IllegalStateException("Values to parse and parser count should be identical");

                final List<Object> parsedValues = new ArrayList<>(valuesOfKeys.size());

                for (int i = 0; i < parsers.length; i++) {
                    parsedValues.add(parsers[i].apply(valuesOfKeys.get(i)));
                }

                conditions.add(filterValue.buildCondition(parsedValues));

                continue;
            }

            if (filterValue.isConditionSupplier()) {
                conditions.add(filterValue.buildCondition());

                continue;
            }

            throw new IllegalStateException("Cannot reach");
        }

        Condition andConditions = DSL.trueCondition();
        for (Condition condition : conditions) {
            andConditions = andConditions.and(condition);
        }
        return andConditions;
    }

    // not needed for now
//    default boolean hasAndNotEmpty(final Map<String, String> map, final String key) {
//        return StringUtils.isNotBlank(map.get(key));
//    }

    /**
     * @param map Map to search in
     * @param key Key to look for
     * @return The value contained in the map if key is found
     */
    default Optional<String> getValue(final Map<String, String> map, final String key) {
        return Optional.ofNullable(map.get(key))
            .filter(StringUtils::isNotBlank);
    }

    /**
     * Get filter associated with the key
     *
     * @param key Key to search for
     * @return Filter associated with key or an empty optional if not found
     */
    default Optional<FilterValue> getFilter(final String key) {
        return getFilterValues().stream()
            .filter(filterValue -> filterValue.getKeyParser().keys().contains(key))
            .findFirst();
    }

    /*
     * Type safety version of getFilter
     *
     * @param key    Key to search for
     * @param tClass Class of filter type
     * @param <T>
     * @return Filter associated with key or an empty optional if not found
     * @deprecated Not sure it is useful now as not used
     */
    /*
    default <T> Optional<FilterValue<T>> getFilter(final String key, final Class<T> tClass) {
        return Optional.ofNullable((FilterValue<T>) getFilterValues().get(key));
    }
    // */

    /**
     * @return List of FilterValue to create condition
     */
    @NotNull
    List<FilterValue> getFilterValues();


    /**
     * This is not a replacement of good code, you should pass to buildConditions only a map of key and values that should be used for filtering and not always define ignored keys
     * <p>This should almost never be overridden but as a precaution we include that possibility in this API</p>
     *
     * @return List of keys that filtering will skip if encountered
     */
    default List<String> getIgnoredKeys() {
        return DEFAULT_IGNORED_KEY_FOR_FILTERING;
    }

    /**
     * By default it is False.
     * <p>This is not a replacement of good code, you should pass to buildConditions only a map of key and values that should be used for filtering and that value is set</p>
     * <p>If true then no exception are thrown on empty value for a key value from {@code Map< String, String>}</p>
     * <p>If False then exception are thrown on empty value for a key value from {@code Map< String, String>}</p>
     *
     * @return True if should skip value that are not present for a key
     * @deprecated Not sure to keep that as it makes unexpected result and does not respect API from client who sent a filter key with no value but that key expects to have a value
     */
    @Deprecated
    default boolean isSkipNotPresentValue() {
        return false;
    }

    /**
     * Helper method but not recommended to use as parsing has to be done inside conditionCreator
     *
     * @param map              Map containing key and values of request
     * @param key              Key to search for
     * @param conditionCreator Condition creator
     * @return Condition created or empty optional
     * @deprecated Only deprecated as not sure we should keep that
     */
    default Optional<Condition> getCondition(final Map<String, String> map, final String key, final Function<String, Condition> conditionCreator) {
        return getValue(map, key)
            .map(conditionCreator);
    }

    /**
     * Helper method but not recommended to use as parsing has to be done inside conditionCreator
     *
     * @param map              Map containing key and values of request
     * @param key              Key to search for
     * @param conditionCreator Condition creator
     * @return Condition created or null
     * @deprecated Only deprecated as not sure we should keep that
     */
    default Condition getConditionOrNull(final Map<String, String> map, final String key, final Function<String, Condition> conditionCreator) {
        return getValue(map, key)
            .map(conditionCreator)
            .orElse(null);
    }
}
