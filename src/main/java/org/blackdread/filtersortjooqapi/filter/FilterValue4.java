package org.blackdread.filtersortjooqapi.filter;

import org.blackdread.filtersortjooqapi.filter.keyparser.KeyParser4;

/**
 * Created by Yoann CAPLAIN on 2017/9/6.
 */
public interface FilterValue4<T1, T2, T3, T4> extends FilterValue {

    KeyParser4<T1, T2, T3, T4> getKeyParser4();
}
