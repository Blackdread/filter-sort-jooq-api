package org.blackdread.filtersortjooqapi.filter.keyparser;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Yoann CAPLAIN on 2017/9/10.
 */
@SuppressWarnings({"unchecked"})
public final class KeyParsers {

    public static <T> KeyParser1<T> of(final String key, final Function<String, T> parser) {
        return new KeyParserImpl<>(ImmutableList.of(key), parser);
    }

    public static <T1, T2> KeyParser2<T1, T2> of(final String key1, final Function<String, T1> parser1,
                                                 final String key2, final Function<String, T2> parser2) {
        checkDuplicateKeys(key1, key2);
        return new KeyParserImpl<>(ImmutableList.of(key1, key2), parser1, parser2);
    }

    public static <T1, T2, T3> KeyParser3<T1, T2, T3> of(final String key1, final Function<String, T1> parser1,
                                                         final String key2, final Function<String, T2> parser2,
                                                         final String key3, final Function<String, T3> parser3) {
        checkDuplicateKeys(key1, key2, key3);
        return new KeyParserImpl<>(ImmutableList.of(key1, key2, key3), parser1, parser2, parser3);
    }

    public static <T1, T2, T3, T4> KeyParser4<T1, T2, T3, T4> of(final String key1, final Function<String, T1> parser1,
                                                                 final String key2, final Function<String, T2> parser2,
                                                                 final String key3, final Function<String, T3> parser3,
                                                                 final String key4, final Function<String, T4> parser4) {
        checkDuplicateKeys(key1, key2, key3, key4);
        return new KeyParserImpl<>(ImmutableList.of(key1, key2, key3, key4), parser1, parser2, parser3, parser4);
    }

    public static <T1, T2, T3, T4, T5> KeyParser5<T1, T2, T3, T4, T5> of(final String key1, final Function<String, T1> parser1,
                                                                         final String key2, final Function<String, T2> parser2,
                                                                         final String key3, final Function<String, T3> parser3,
                                                                         final String key4, final Function<String, T4> parser4,
                                                                         final String key5, final Function<String, T5> parser5) {
        checkDuplicateKeys(key1, key2, key3, key4, key5);
        return new KeyParserImpl<>(ImmutableList.of(key1, key2, key3, key4, key5), parser1, parser2, parser3, parser4, parser5);
    }

    public static <T1, T2, T3, T4, T5, T6> KeyParser6<T1, T2, T3, T4, T5, T6> of(final String key1, final Function<String, T1> parser1,
                                                                                 final String key2, final Function<String, T2> parser2,
                                                                                 final String key3, final Function<String, T3> parser3,
                                                                                 final String key4, final Function<String, T4> parser4,
                                                                                 final String key5, final Function<String, T5> parser5,
                                                                                 final String key6, final Function<String, T6> parser6) {
        checkDuplicateKeys(key1, key2, key3, key4, key5, key6);
        return new KeyParserImpl<>(ImmutableList.of(key1, key2, key3, key4, key5, key6), parser1, parser2, parser3, parser4, parser5, parser6);
    }

    /**
     * Version for KeyParser with more than 6 keys/parsers
     *
     * @param keys    List of key
     * @param parsers List of parser function
     * @return KeyParser with N element to parse
     * @throws IllegalArgumentException If keys and parsers size are different
     */
    public static KeyParser ofN(final List<String> keys, final List<Function<String, Object>> parsers) {
        if (keys.size() != parsers.size())
            throw new IllegalArgumentException("Keys and parsers should be of same size");
        checkDuplicateKeys(keys.toArray(new String[0]));
        return new KeyParserImpl(ImmutableList.copyOf(keys), ImmutableList.copyOf(parsers));
    }

    private static void checkDuplicateKeys(final String... keys) {
        for (int i = 0; i < keys.length - 1; i++) {
            if (ArrayUtils.indexOf(keys, keys[i], i + 1) != -1)
                throw new IllegalArgumentException("List of keys has duplicate values");
        }
    }

    private KeyParsers() {
    }

}
