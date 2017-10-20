package org.blackdread.filtersortjooqapi.contract.parser;


import org.blackdread.filtersortjooqapi.contract.exception.FilteringApiException;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Yoann CAPLAIN on 2017/8/28.
 */
@FunctionalInterface
public interface FilterParser<R> {

    /**
     * @param value Value to be parsed
     * @return Value parsed and transform to generic value
     * @throws RuntimeException When parsing failed if implementation does not swallow exception
     */
    @NotNull
    R parse(final String value);

    /**
     * @param value  Value to be parsed
     * @param parser
     * @return Value parsed and transform to generic value
     * @throws FilteringApiException If an error occurred at parsing
     * @deprecated No sure it is useful, if need to define a new parser then better to create the class and etc
     */
    default R parseWithApiException(final String value, final Function<String, R> parser) throws FilteringApiException {
        try {
            return parser.apply(value);
        } catch (Exception e) {
            throw new FilteringApiException("Could not parse value (" + value + ") to " + this.getClass().getSimpleName());
        }
    }

    /**
     * @param value Value to be parsed
     * @return Value parsed and transform to generic value
     * @throws FilteringApiException If an error occurred at parsing
     */
    default R parseWithApiException(final String value) throws FilteringApiException {
        try {
            return parse(value);
        } catch (Exception e) {
            throw new FilteringApiException("Could not parse value (" + value + ") to " + this.getClass().getSimpleName());
        }
    }

    /**
     * @param value        Value to be parsed
     * @param defaultValue Default value returned if parse throws an exception
     * @return Value parsed and transform to generic value
     */
    default R parseOrDefault(final String value, final R defaultValue) {
        try {
            return parse(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @param value        Value to be parsed
     * @param defaultValue Default value returned if parse throws an exception
     * @return Value parsed and transform to generic value
     */
    default R parseOrDefault(final String value, final Supplier<R> defaultValue) {
        try {
            return parse(value);
        } catch (Exception e) {
            return defaultValue.get();
        }
    }

    /**
     * @return Parser to change string to Integer
     */
    static FilterParser<Integer> ofInt() {
        return FilterParserInteger.INSTANCE;
    }

    /**
     * @return Parser to change string to LocalDate
     */
    static FilterParser<LocalDate> ofDate() {
        return FilterParserDate.INSTANCE;
    }

    /**
     * @return Parser to change string to LocalTime
     */
    static FilterParser<LocalTime> ofTime() {
        return FilterParserTime.INSTANCE;
    }

    /**
     * @return Parser to change string to LocalDateTime
     */
    static FilterParser<LocalDateTime> ofDateTime() {
        return FilterParserDateTime.INSTANCE;
    }

    /**
     * A parser that does not parse. An identity string
     *
     * @return A parser that does not parse value
     */
    static FilterParser<String> ofIdentity() {
        return FilterParserIdentity.INSTANCE;
    }

    /**
     * @return Parser to change string to Byte
     */
    static FilterParser<Byte> ofByte() {
        return FilterParserByte.INSTANCE;
    }

    // TODO To see if meaning full to have a "fake" parser that returns a value predefined
//    static <T> FilterParser<T> of(final T value) {
//        return FilterParse;
//    }

}
