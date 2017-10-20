package org.blackdread.filtersortjooqapi.contract;

import java.util.function.Function;

/**
 * Created by Yoann CAPLAIN on 2017/9/8.
 */
public interface KeyParser1<T1> extends KeyParser {

    String key1();

    Function<String, T1> parser1();
}
