package org.blackdread.filtersortjooqapi.filter.keyparser;

import java.util.List;
import java.util.function.Function;

/**
 * <p>Class to group keys and parsers together. Each index of key/parser corresponds to a parser/key</p>
 * Created by Yoann CAPLAIN on 2017/9/8.
 */
public interface KeyParser {

    /**
     * Get the number of keys to be filtered (there are as many parsers)
     */
    int size();

    /**
     *
     * @return Keys list
     */
    List<String> keys();

    /**
     *
     * @return Keys as array
     */
    String[] getKeys();

    /**
     *
     * @return Parsers that take a {@link String} and return an {@link Object}
     */
    Function<String, ?>[] getParsers();
}
