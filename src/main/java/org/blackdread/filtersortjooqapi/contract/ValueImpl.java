package org.blackdread.filtersortjooqapi.contract;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by Yoann CAPLAIN on 2017/9/10.
 */
@SuppressWarnings({"unchecked"})
public class ValueImpl<T1, T2, T3, T4, T5, T6> implements
    Value1<T1>,
    Value2<T1, T2>,
    Value3<T1, T2, T3>,
    Value4<T1, T2, T3, T4>,
    Value5<T1, T2, T3, T4, T5>,
    Value6<T1, T2, T3, T4, T5, T6> {

    private final Object[] values;

    ValueImpl(final Object... values) {
        this.values = Objects.requireNonNull(values);
    }

    ValueImpl(final Collection<Object> values) {
        this.values = Objects.requireNonNull(values).toArray(new Object[values.size()]);
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public Object getValue(final int index) {
        // TODO should secure index access? -> by throwing an exception -> it is safe already with API
        return values[index];
    }

    @Override
    public <T> T getValue(final int index, final Class<T> type) {
        return (T) getValue(index);
    }

    @Override
    public Stream<Object> valueStream() {
        return Arrays.stream(values);
    }

    @Override
    public Object[] values() {
        return values;
    }

    @Override
    public T1 value1() {
        return (T1) getValue(0);
    }

    @Override
    public T2 value2() {
        return (T2) getValue(1);
    }

    @Override
    public T3 value3() {
        return (T3) getValue(2);
    }

    @Override
    public T4 value4() {
        return (T4) getValue(3);
    }

    @Override
    public T5 value5() {
        return (T5) getValue(4);
    }

    @Override
    public T6 value6() {
        return (T6) getValue(5);
    }
}
