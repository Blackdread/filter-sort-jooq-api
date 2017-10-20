package org.blackdread.filtersortjooqapi.contract;

import java.util.stream.Stream;

/**
 * Created by Yoann CAPLAIN on 2017/9/10.
 */
public interface Value {

    /**
     * Number of values
     * @return Number of values
     */
    int size();

    /**
     * Will throw an exception if index is out of range
     * @param index Index of value
     * @return Value of index given
     */
    Object getValue(final int index);

    /**
     * Will throw an exception if index is out of range
     * @param index Index of value
     * @param type Type of value
     * @param <T> Type of value
     * @return Value of index given
     */
    <T> T getValue(final int index, Class<T> type);

    /**
     * Return the values in a stream
     * @return Stream of values
     */
    Stream<Object> valueStream();

    /**
     *
     * @return Values in an array
     */
    Object[] values();
}
