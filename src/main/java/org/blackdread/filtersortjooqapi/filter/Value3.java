package org.blackdread.filtersortjooqapi.filter;

/**
 * Created by Yoann CAPLAIN on 2017/9/8.
 */
public interface Value3<T1, T2, T3> extends Value {
    T1 value1();

    T2 value2();

    T3 value3();
}
