package org.blackdread.filtersortjooqapi.filter.keyparser;

import java.util.function.Function;

/**
 * Created by Yoann CAPLAIN on 2017/9/8.
 */
public interface KeyParser2<T1, T2> extends KeyParser {

    String key1();

    String key2();

    Function<String, T1> parser1();

    Function<String, T2> parser2();
}
