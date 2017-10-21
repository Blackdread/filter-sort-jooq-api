package org.blackdread.filtersortjooqapi.filter;

import org.blackdread.filtersortjooqapi.filter.keyparser.KeyParser2;

/**
 * Created by Yoann CAPLAIN on 2017/9/6.
 */
public interface FilterValue2<T1, T2> extends FilterValue {

    KeyParser2<T1, T2> getKeyParser2();

//    Condition buildCondition(final T1 value1, final T2 value2);

//    BiFunction<T1, T2, Condition> getConditionCreator2();
}
