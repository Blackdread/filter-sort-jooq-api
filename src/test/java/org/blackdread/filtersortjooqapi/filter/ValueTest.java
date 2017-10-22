package org.blackdread.filtersortjooqapi.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Stream;

class ValueTest {

    static Stream<Value> valuesStream() {
        return Stream.of(
            new ValueImpl<>("0"),
            new ValueImpl<>("0", "oo"),
            new ValueImpl<>("0", "oo", 2),
            new ValueImpl<>("0", "oo", 2, -66),
            new ValueImpl<>("0", "oo", 2, -66, LocalDateTime.now()),
            new ValueImpl<>("0", "oo", 2, -66, LocalDateTime.now(), "val")
        );
    }

    @Test
    void emptyValueThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, ValueImpl::new);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ValueImpl<>(new Object[0]));
        final Object ff = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ValueImpl<>(ff));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ValueImpl<>(Collections.emptyList()));
    }

    @ParameterizedTest
    @MethodSource("valuesStream")
    void sizeWorks(final Value value) {
        value.size();
    }

    @ParameterizedTest
    @MethodSource("valuesStream")
    void getValue(final Value value) {
        for (int i = 0; i < value.size(); i++) {
            Assertions.assertNotNull(value.getValue(i));
        }
    }

    @ParameterizedTest
    @MethodSource("valuesStream")
    void getValueThrowsWhenOutOfRange(final Value value) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> value.getValue(value.size() + 1));
    }

    @Test
    void getValueWithType() {
        final ValueImpl<Object, Object, Object, Object, Object, Object> value = new ValueImpl<>("0", "oo", 2, -66, LocalDateTime.now());
        Assertions.assertNotNull(value.getValue(0, String.class));
        Assertions.assertNotNull(value.getValue(1, String.class));
        Assertions.assertNotNull(value.getValue(2, Integer.class));
        Assertions.assertNotNull(value.getValue(3, Integer.class));
        Assertions.assertNotNull(value.getValue(4, LocalDateTime.class));
    }

    @ParameterizedTest
    @MethodSource("valuesStream")
    void valueStream(final Value value) {
        for (int i = 0; i < value.size(); i++) {
            final Object val = value.valueStream().skip(i).findFirst().orElseThrow(() -> new IllegalStateException("Shall not happen"));
            Assertions.assertEquals(value.getValue(i), val);
        }
    }

    @ParameterizedTest
    @MethodSource("valuesStream")
    void values(final Value value) {
        Assertions.assertEquals(value.size(), value.values().length);
    }

    @ParameterizedTest
    @MethodSource("valuesStream")
    void valueWithGetterNumbered(final Value value) {
        final int size = value.size();
        Assertions.assertNotNull(((Value1) value).value1());

        if (size > 1)
            Assertions.assertNotNull(((Value2) value).value2());

        if (size > 2)
            Assertions.assertNotNull(((Value3) value).value3());

        if (size > 3)
            Assertions.assertNotNull(((Value4) value).value4());

        if (size > 4)
            Assertions.assertNotNull(((Value5) value).value5());

        if (size > 5)
            Assertions.assertNotNull(((Value6) value).value6());
    }

    @ParameterizedTest
    @MethodSource("valuesStream")
    void valueWithGetterNumberedThrowsOutOfRange(final Value value) {
        final int size = value.size();

        // No class cast exception as Value == ValueImpl (supposedly)
        if (size <= 1)
            Assertions.assertThrows(IndexOutOfBoundsException.class, ((Value2) value)::value2);

        if (size <= 2)
            Assertions.assertThrows(IndexOutOfBoundsException.class, ((Value3) value)::value3);

        if (size <= 3)
            Assertions.assertThrows(IndexOutOfBoundsException.class, ((Value4) value)::value4);

        if (size <= 4)
            Assertions.assertThrows(IndexOutOfBoundsException.class, ((Value5) value)::value5);

        if (size <= 5)
            Assertions.assertThrows(IndexOutOfBoundsException.class, ((Value6) value)::value6);
    }

}
