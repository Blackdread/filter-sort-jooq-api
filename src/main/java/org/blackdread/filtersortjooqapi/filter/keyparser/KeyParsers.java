package org.blackdread.filtersortjooqapi.filter.keyparser;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Yoann CAPLAIN on 2017/9/10.
 */
@SuppressWarnings({"unchecked"})
public final class KeyParsers {

    /**
     *
     * @param key
     * @param parser
     * @param <T>
     * @return
     */
    public static <T> KeyParser1<T> of(String key, Function<String, T> parser) {
        return new KeyParserImpl<>(ImmutableList.of(key), parser);
    }

    /**
     * @param key1
     * @param parser1
     * @param key2
     * @param parser2
     * @param <T1>
     * @param <T2>
     * @return
     */
    public static <T1, T2> KeyParser2<T1, T2> of(String key1, Function<String, T1> parser1,
                                                 String key2, Function<String, T2> parser2) {
        return new KeyParserImpl<>(ImmutableList.of(key1, key2), parser1, parser2);
    }

    public static <T1, T2, T3> KeyParser3<T1, T2, T3> of(String key1, Function<String, T1> parser1,
                                                         String key2, Function<String, T2> parser2,
                                                         String key3, Function<String, T3> parser3) {
        return new KeyParserImpl<>(ImmutableList.of(key1, key2, key3), parser1, parser2, parser3);
    }

    public static <T1, T2, T3, T4> KeyParser4<T1, T2, T3, T4> of(String key1, Function<String, T1> parser1,
                                                                 String key2, Function<String, T2> parser2,
                                                                 String key3, Function<String, T3> parser3,
                                                                 String key4, Function<String, T4> parser4) {
        return new KeyParserImpl<>(ImmutableList.of(key1, key2, key3, key4), parser1, parser2, parser3, parser4);
    }

    public static <T1, T2, T3, T4, T5> KeyParser5<T1, T2, T3, T4, T5> of(String key1, Function<String, T1> parser1,
                                                                         String key2, Function<String, T2> parser2,
                                                                         String key3, Function<String, T3> parser3,
                                                                         String key4, Function<String, T4> parser4,
                                                                         String key5, Function<String, T5> parser5) {
        return new KeyParserImpl<>(ImmutableList.of(key1, key2, key3, key4, key5), parser1, parser2, parser3, parser4, parser5);
    }

    public static <T1, T2, T3, T4, T5, T6> KeyParser6<T1, T2, T3, T4, T5, T6> of(String key1, Function<String, T1> parser1,
                                                                                 String key2, Function<String, T2> parser2,
                                                                                 String key3, Function<String, T3> parser3,
                                                                                 String key4, Function<String, T4> parser4,
                                                                                 String key5, Function<String, T5> parser5,
                                                                                 String key6, Function<String, T6> parser6) {
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
    public static KeyParser ofN(List<String> keys, List<Function<String, Object>> parsers) {
        if (keys.size() != parsers.size())
            throw new IllegalArgumentException("Keys and parsers should be of same size");
        return new KeyParserImpl(ImmutableList.copyOf(keys), ImmutableList.copyOf(parsers));
    }

    private KeyParsers() {
    }

}
