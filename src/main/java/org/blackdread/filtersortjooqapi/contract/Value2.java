package org.blackdread.filtersortjooqapi.contract;

/**
 * Created by Yoann CAPLAIN on 2017/9/8.
 */
public interface Value2<T1, T2> extends Value {
    T1 value1();

    T2 value2();
}
