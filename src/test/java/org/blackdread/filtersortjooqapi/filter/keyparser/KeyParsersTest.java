package org.blackdread.filtersortjooqapi.filter.keyparser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

class KeyParsersTest {

    @Test
    void of1() {
        final KeyParser1<Integer> keyParser = KeyParsers.of("key", val -> 0);
        Assertions.assertEquals("key", keyParser.key1());
        Assertions.assertEquals("key", keyParser.getKeys()[0]);
        Assertions.assertEquals(1, keyParser.size());
        Assertions.assertEquals(1, keyParser.getKeys().length);

        Assertions.assertEquals(0, (int) keyParser.parser1().apply("any"));
        Assertions.assertEquals(1, keyParser.getParsers().length);
    }

    @Test
    void of2() {
        final KeyParser2<Integer, String> keyParser = KeyParsers.of("key1", val -> 0, "key2", val -> "other");
        Assertions.assertEquals("key1", keyParser.key1());
        Assertions.assertEquals("key2", keyParser.key2());
        Assertions.assertEquals("key1", keyParser.getKeys()[0]);
        Assertions.assertEquals("key2", keyParser.getKeys()[1]);
        Assertions.assertEquals(2, keyParser.size());
        Assertions.assertEquals(2, keyParser.getKeys().length);

        Assertions.assertEquals(0, (int) keyParser.parser1().apply("any"));
        Assertions.assertEquals("other", keyParser.parser2().apply("any"));
        Assertions.assertEquals(2, keyParser.getParsers().length);
    }

    @Test
    void of3() {
        final KeyParser3<Integer, String, LocalTime> keyParser = KeyParsers.of(
            "key1", val -> 0,
            "key2", val -> "other",
            "key3", val -> LocalTime.MIDNIGHT);
        Assertions.assertEquals("key1", keyParser.key1());
        Assertions.assertEquals("key2", keyParser.key2());
        Assertions.assertEquals("key3", keyParser.key3());
        Assertions.assertEquals("key1", keyParser.getKeys()[0]);
        Assertions.assertEquals("key2", keyParser.getKeys()[1]);
        Assertions.assertEquals("key3", keyParser.getKeys()[2]);
        Assertions.assertEquals(3, keyParser.size());
        Assertions.assertEquals(3, keyParser.getKeys().length);

        Assertions.assertEquals(0, (int) keyParser.parser1().apply("any"));
        Assertions.assertEquals("other", keyParser.parser2().apply("any"));
        Assertions.assertEquals(LocalTime.MIDNIGHT, keyParser.parser3().apply("any"));
        Assertions.assertEquals(3, keyParser.getParsers().length);
    }

    @Test
    void of4() {
        final KeyParser4<Integer, String, LocalTime, Locale> keyParser = KeyParsers.of(
            "key1", val -> 0,
            "key2", val -> "other",
            "key3", val -> LocalTime.MIDNIGHT,
            "key4", val -> Locale.ENGLISH);
        Assertions.assertEquals("key1", keyParser.key1());
        Assertions.assertEquals("key2", keyParser.key2());
        Assertions.assertEquals("key3", keyParser.key3());
        Assertions.assertEquals("key4", keyParser.key4());
        Assertions.assertEquals("key1", keyParser.getKeys()[0]);
        Assertions.assertEquals("key2", keyParser.getKeys()[1]);
        Assertions.assertEquals("key3", keyParser.getKeys()[2]);
        Assertions.assertEquals("key4", keyParser.getKeys()[3]);
        Assertions.assertEquals(4, keyParser.size());
        Assertions.assertEquals(4, keyParser.getKeys().length);

        Assertions.assertEquals(0, (int) keyParser.parser1().apply("any"));
        Assertions.assertEquals("other", keyParser.parser2().apply("any"));
        Assertions.assertEquals(LocalTime.MIDNIGHT, keyParser.parser3().apply("any"));
        Assertions.assertEquals(Locale.ENGLISH, keyParser.parser4().apply("en"));
        Assertions.assertEquals(4, keyParser.getParsers().length);
    }

    @Test
    void of5() {
        final KeyParser5<Integer, String, LocalTime, Locale, Byte> keyParser = KeyParsers.of(
            "key1", val -> 0,
            "key2", val -> "other",
            "key3", val -> LocalTime.MIDNIGHT,
            "key4", val -> Locale.ENGLISH,
            "key5", val -> (byte) 0xF);
        Assertions.assertEquals("key5", keyParser.key5());
        Assertions.assertEquals("key5", keyParser.getKeys()[4]);
        Assertions.assertEquals(5, keyParser.size());
        Assertions.assertEquals(5, keyParser.getKeys().length);

        Assertions.assertEquals(0xF, (byte) keyParser.parser5().apply("any"));
        Assertions.assertEquals(5, keyParser.getParsers().length);
    }

    @Test
    void of6() {
        final KeyParser6<Integer, String, LocalTime, Locale, Byte, LocalDateTime> keyParser = KeyParsers.of(
            "key1", val -> 0,
            "key2", val -> "other",
            "key3", val -> LocalTime.MIDNIGHT,
            "key4", val -> Locale.ENGLISH,
            "key5", val -> (byte) 0xFF,
            "key6", val -> LocalDateTime.MAX);
        Assertions.assertEquals("key6", keyParser.key6());
        Assertions.assertEquals("key6", keyParser.getKeys()[5]);
        Assertions.assertEquals(6, keyParser.size());
        Assertions.assertEquals(6, keyParser.getKeys().length);

        Assertions.assertEquals(LocalDateTime.MAX, keyParser.parser6().apply("any"));
        Assertions.assertEquals(6, keyParser.getParsers().length);
    }

    @Test
    void ofN() {
        final KeyParser keyParser = KeyParsers.ofN(
            Arrays.asList("key", "key1", "key2", "key3", "key4", "key5", "key6", "key7", "key8"),
            Arrays.asList(val1 -> "value", val1 -> "value1", val1 -> "value2", val1 -> "value", val1 -> "value", val1 -> "value6", val1 -> "value", val1 -> "value", val1 -> "value9")
        );
        Assertions.assertEquals(9, keyParser.keys().size());
        Assertions.assertEquals(9, keyParser.getKeys().length);
        Assertions.assertEquals(9, keyParser.getParsers().length);
        Assertions.assertEquals("value9", keyParser.getParsers()[8].apply("any"));
    }

    @Test
    void ofNThrowsIfKeyOrParserEmpty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.ofN(Collections.emptyList(), Collections.emptyList()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.ofN(Arrays.asList("k", "k2"), Collections.emptyList()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.ofN(Collections.emptyList(), Collections.singletonList(val -> "value")));
    }

    @Test
    void ofNThrowsIfKeyAndParserSizeMistamatch() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.ofN(Arrays.asList("k", "k2"), Arrays.asList(val -> "value", val -> "value", val -> "value")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.ofN(Arrays.asList("k", "k2", "k3", "k4"), Arrays.asList(val -> "value", val -> "value", val -> "value")));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.ofN(Arrays.asList("k", "k2"), Collections.singletonList(val -> "value")));
    }

    @Test
    void throwsOnDuplicateKey() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.of("key", val -> 0, "key", val -> 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.of("key", val -> 0, "key", val -> 0, "key", val -> 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.of("key", val -> 0, "key", val -> 0, "key", val -> 0, "key", val -> 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.of("key", val -> 0, "key", val -> 0, "key", val -> 0, "key", val -> 0, "key", val -> 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.of("key", val -> 0, "key", val -> 0, "key", val -> 0, "key", val -> 0, "key", val -> 0, "key", val -> 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> KeyParsers.ofN(
            Arrays.asList("key", "key1", "key2", "key3", "key4", "key5", "key6", "key6", "key8"),
            Arrays.asList(val1 -> "value", val1 -> "value1", val1 -> "value2", val1 -> "value", val1 -> "value", val1 -> "value6", val1 -> "value", val1 -> "value", val1 -> "value9")
        ));
    }

}
