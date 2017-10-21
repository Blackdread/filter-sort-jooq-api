package org.blackdread.filtersortjooqapi.filter;

import org.blackdread.filtersortjooqapi.filter.keyparser.KeyParser6;

/**
 * Created by Yoann CAPLAIN on 2017/9/11.
 */
public interface FilterValue6<T1, T2, T3, T4, T5, T6> extends FilterValue {

    KeyParser6<T1, T2, T3, T4, T5, T6> getKeyParser6();
}
