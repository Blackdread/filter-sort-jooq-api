package org.blackdread.filtersortjooqapi.filter;

import org.blackdread.filtersortjooqapi.filter.keyparser.KeyParser3;

/**
 * Created by Yoann CAPLAIN on 2017/9/6.
 */
public interface FilterValue3<T1, T2, T3> extends FilterValue {

    KeyParser3<T1, T2, T3> getKeyParser3();
}
