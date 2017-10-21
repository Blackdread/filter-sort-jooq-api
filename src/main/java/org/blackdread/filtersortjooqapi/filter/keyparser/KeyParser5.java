package org.blackdread.filtersortjooqapi.filter.keyparser;

import java.util.function.Function;

/**
 * Created by Yoann CAPLAIN on 2017/9/8.
 */
public interface KeyParser5<T1, T2, T3, T4, T5> extends KeyParser {

    String key1();

    String key2();

    String key3();

    String key4();

    String key5();

    Function<String, T1> parser1();

    Function<String, T2> parser2();

    Function<String, T3> parser3();

    Function<String, T4> parser4();

    Function<String, T5> parser5();
}
