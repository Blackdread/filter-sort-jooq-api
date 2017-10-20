package org.blackdread.filtersortjooqapi.contract;

import java.util.function.Function;

/**
 * Created by Yoann CAPLAIN on 2017/9/8.
 */
public interface KeyParser3<T1, T2, T3> extends KeyParser {

    String key1();

    String key2();

    String key3();

    Function<String, T1> parser1();

    Function<String, T2> parser2();

    Function<String, T3> parser3();
}
