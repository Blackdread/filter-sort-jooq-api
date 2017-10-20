package org.blackdread.filtersortjooqapi.contract;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created by Yoann CAPLAIN on 2017/9/8.
 */
@SuppressWarnings({"unchecked"})
class KeyParserImpl<T1, T2, T3, T4, T5, T6> implements
    KeyParser1<T1>,
    KeyParser2<T1, T2>,
    KeyParser3<T1, T2, T3>,
    KeyParser4<T1, T2, T3, T4>,
    KeyParser5<T1, T2, T3, T4, T5>,
    KeyParser6<T1, T2, T3, T4, T5, T6> {

    //    private final String[] keys;
    private final List<String> keys;

    //    private final List<Function<String, ?>> parsers;
    private final Function<String, ?>[] parsers;

    /**
     * Keys and parsers should be ordered.
     * <p>Key 1 match parser 1, etc.</p>
     *
     * @param keys    List of keys
     * @param parsers varargs of parsers
     */
    KeyParserImpl(final List<String> keys, final Function<String, ?>... parsers) {
//        this.keys = keys.toArray(new String[keys.size()]);
        if (parsers.length != keys.size())
            throw new IllegalArgumentException("Keys and parsers should be of same size");
        this.keys = ImmutableList.copyOf(keys);
        this.parsers = Objects.requireNonNull(parsers);

    }

    /**
     * Keys and parsers should be ordered.
     * <p>Key 1 match parser 1, etc.</p>
     * <p><b>IMPORTANT:</b> Constructor for KeyParser of more than 6 elements</p>
     *
     * @param keys    List of keys
     * @param parsers varargs of parsers
     */
    KeyParserImpl(final List<String> keys, final List<Function<String, ?>> parsers) {
//        this.keys = keys.toArray(new String[keys.size()]);
        if (parsers.size() != keys.size())
            throw new IllegalArgumentException("Keys and parsers should be of same size");
        this.keys = ImmutableList.copyOf(keys);
        this.parsers = Objects.requireNonNull(parsers).toArray(new Function[parsers.size()]);

    }

    @Override
    public int size() {
        return keys.size();
    }

    @Override
    public List<String> keys() {
        return keys;
    }

    @Override
    public String[] getKeys() {
        return keys.toArray(new String[keys.size()]);
    }

    @Override
    public Function<String, ?>[] getParsers() {
//        return (Function<String, ?>[]) parsers.toArray();
        return parsers;
    }

    @Override
    public String key1() {
        return getKey(0);
    }

    @Override
    public String key2() {
        return getKey(1);
    }

    @Override
    public String key3() {
        return getKey(2);
    }

    @Override
    public String key4() {
        return getKey(3);
    }

    @Override
    public String key5() {
        return getKey(4);
    }

    @Override
    public String key6() {
        return getKey(5);
    }

    @Override
    public Function<String, T1> parser1() {
        return (Function<String, T1>) getParser(0);
    }

    @Override
    public Function<String, T2> parser2() {
        return (Function<String, T2>) getParser(1);
    }

    @Override
    public Function<String, T3> parser3() {
        return (Function<String, T3>) getParser(2);
    }

    @Override
    public Function<String, T4> parser4() {
        return (Function<String, T4>) getParser(3);
    }

    @Override
    public Function<String, T5> parser5() {
        return (Function<String, T5>) getParser(4);
    }

    @Override
    public Function<String, T6> parser6() {
        return (Function<String, T6>) getParser(5);
    }

    private String getKey(final int index) {
        return keys.get(index);
//        return keys[index];
    }

    private Function<String, ?> getParser(final int index) {
//        return parsers.get(index);
        return parsers[index];
    }
}
