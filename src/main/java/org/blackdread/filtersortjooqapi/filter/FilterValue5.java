package org.blackdread.filtersortjooqapi.filter;

import org.blackdread.filtersortjooqapi.filter.keyparser.KeyParser5;

/**
 * Created by Yoann CAPLAIN on 2017/9/11.
 */
public interface FilterValue5<T1, T2, T3, T4, T5> extends FilterValue {

    KeyParser5<T1, T2, T3, T4, T5> getKeyParser5();
}
