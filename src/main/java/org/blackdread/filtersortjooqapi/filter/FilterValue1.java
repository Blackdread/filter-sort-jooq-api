package org.blackdread.filtersortjooqapi.filter;

import org.blackdread.filtersortjooqapi.filter.keyparser.KeyParser1;

/**
 * Created by Yoann CAPLAIN on 2017/8/25.
 */
public interface FilterValue1<T1> extends FilterValue {

    KeyParser1<T1> getKeyParser1();

//    Condition buildCondition(final T1 value1);
}
