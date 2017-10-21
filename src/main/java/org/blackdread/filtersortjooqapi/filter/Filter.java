package org.blackdread.filtersortjooqapi.filter;

import org.blackdread.filtersortjooqapi.filter.keyparser.*;
import org.jooq.Condition;

import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Yoann CAPLAIN on 2017/9/7.
 */
@SuppressWarnings({"unchecked"})
public final class Filter {

    /**
     * Build a filter value that does not have any parser so condition is only based on presence of key
     *
     * @param key               Key that activate that filter
     * @param conditionSupplier Supplier function of condition
     * @return Filter value that will be dynamically called when building conditions
     */
    public static FilterValue of(@NotNull final String key, @NotNull final Supplier<Condition> conditionSupplier) {
        return new FilterValueImpl<>(key, conditionSupplier);
    }

    /**
     * Build a filter value that will take a value to parse then pass it to condition creator function
     *
     * @param key              Key that activate that filter
     * @param parser           A parser function that takes a value to transform to another value and applied to conditionCreator
     * @param conditionCreator Condition creator function of condition that takes value produced by parser
     * @param <T>              Type of value produced
     * @return Filter value that will be dynamically called when building conditions
     */
    public static <T> FilterValue1<T> of(@NotNull final String key,
                                         @NotNull final Function<String, T> parser,
                                         @NotNull final Function<T, Condition> conditionCreator) {
        return new FilterValueImpl(KeyParsers.of(key, parser), conditionCreator);
    }

    /**
     * Build a filter value that will take a value to parse then pass it to condition creator function
     *
     * @param keyParser        Key that activate that filter and associated parser of value
     * @param conditionCreator Condition creator function of condition that takes value produced by parser
     * @param <T>              Type of value produced
     * @return Filter value that will be dynamically called when building conditions
     */
    public static <T> FilterValue1<T> of(@NotNull final KeyParser1<T> keyParser,
                                         @NotNull final Function<T, Condition> conditionCreator) {
        return new FilterValueImpl(keyParser, conditionCreator);
    }

    /**
     * Build a filter value that will take two values to parse then pass it to condition creator function
     *
     * @param key1             Key that activate that filter
     * @param key2             Key that activate that filter
     * @param parser1          A parser function that takes a value from key 1 to transform to another value and applied to conditionCreator
     * @param parser2          A parser function that takes a value from key 2 to transform to another value and applied to conditionCreator
     * @param conditionCreator Condition creator function of condition that takes values produced by parser
     * @param <T1>             Type of value 1 produced
     * @param <T2>             Type of value 2 produced
     * @return Filter value that will be dynamically called when building conditions
     */
    public static <T1, T2> FilterValue2<T1, T2> of(@NotNull final String key1,
                                                   @NotNull final String key2,
                                                   @NotNull final Function<String, T1> parser1,
                                                   @NotNull final Function<String, T2> parser2,
                                                   @NotNull final BiFunction<T1, T2, Condition> conditionCreator) {
        return new FilterValueImpl(KeyParsers.of(key1, parser1, key2, parser2), conditionCreator);
    }

    /**
     * Build a filter value that will take two values to parse then pass it to condition creator function
     *
     * @param keyParser        Keys that activate that filter and associated parsers of values
     * @param conditionCreator Condition creator function of condition that takes values produced by parser
     * @param <T1>             Type of value 1 produced
     * @param <T2>             Type of value 2 produced
     * @return Filter value that will be dynamically called when building conditions
     */
    public static <T1, T2> FilterValue2<T1, T2> of(@NotNull final KeyParser2<T1, T2> keyParser,
                                                   @NotNull final BiFunction<T1, T2, Condition> conditionCreator) {
        return new FilterValueImpl(keyParser, conditionCreator);
    }

    /**
     * Build a filter value that will take three values to parse then pass it to condition creator function
     *
     * @param key1             Key that activate that filter
     * @param key2             Key that activate that filter
     * @param key3             Key that activate that filter
     * @param parser1          A parser function that takes a value from key 1 to transform to another value and applied to conditionCreator
     * @param parser2          A parser function that takes a value from key 2 to transform to another value and applied to conditionCreator
     * @param parser3          A parser function that takes a value from key 3 to transform to another value and applied to conditionCreator
     * @param conditionCreator Condition creator function of condition that takes values produced by parser
     * @param <T1>             Type of value 1 produced
     * @param <T2>             Type of value 2 produced
     * @param <T3>             Type of value 3 produced
     * @return Filter value that will be dynamically called when building conditions
     */
    public static <T1, T2, T3> FilterValue3<T1, T2, T3> of(@NotNull final String key1,
                                                           @NotNull final String key2,
                                                           @NotNull final String key3,
                                                           @NotNull final Function<String, T1> parser1,
                                                           @NotNull final Function<String, T2> parser2,
                                                           @NotNull final Function<String, T3> parser3,
                                                           @NotNull final Function<Value3<T1, T2, T3>, Condition> conditionCreator) {
        return new FilterValueImpl(KeyParsers.of(key1, parser1, key2, parser2, key3, parser3), conditionCreator);
    }

    /**
     * Build a filter value that will take three values to parse then pass it to condition creator function
     *
     * @param keyParser        Keys that activate that filter and associated parsers of values
     * @param conditionCreator Condition creator function of condition that takes values produced by parser
     * @param <T1>             Type of value 1 produced
     * @param <T2>             Type of value 2 produced
     * @param <T3>             Type of value 3 produced
     * @return Filter value that will be dynamically called when building conditions
     */
    public static <T1, T2, T3> FilterValue3<T1, T2, T3> of(@NotNull final KeyParser3<T1, T2, T3> keyParser,
                                                           @NotNull final Function<Value3<T1, T2, T3>, Condition> conditionCreator) {
        return new FilterValueImpl(keyParser, conditionCreator);
    }

    /**
     * Build a filter value that will take four values to parse then pass it to condition creator function
     *
     * @param key1             Key that activate that filter
     * @param key2             Key that activate that filter
     * @param key3             Key that activate that filter
     * @param key4             Key that activate that filter
     * @param parser1          A parser function that takes a value from key 1 to transform to another value and applied to conditionCreator
     * @param parser2          A parser function that takes a value from key 2 to transform to another value and applied to conditionCreator
     * @param parser3          A parser function that takes a value from key 3 to transform to another value and applied to conditionCreator
     * @param parser4          A parser function that takes a value from key 4 to transform to another value and applied to conditionCreator
     * @param conditionCreator Condition creator function of condition that takes values produced by parser
     * @param <T1>             Type of value 1 produced
     * @param <T2>             Type of value 2 produced
     * @param <T3>             Type of value 3 produced
     * @param <T4>             Type of value 4 produced
     * @return Filter value that will be dynamically called when building conditions
     */
    public static <T1, T2, T3, T4> FilterValue4<T1, T2, T3, T4> of(@NotNull final String key1,
                                                                   @NotNull final String key2,
                                                                   @NotNull final String key3,
                                                                   @NotNull final String key4,
                                                                   @NotNull final Function<String, T1> parser1,
                                                                   @NotNull final Function<String, T2> parser2,
                                                                   @NotNull final Function<String, T3> parser3,
                                                                   @NotNull final Function<String, T4> parser4,
                                                                   @NotNull final Function<Value4<T1, T2, T3, T4>, Condition> conditionCreator) {
        return new FilterValueImpl(KeyParsers.of(key1, parser1, key2, parser2, key3, parser3, key4, parser4), conditionCreator);
    }

    /**
     * Build a filter value that will take four values to parse then pass it to condition creator function
     *
     * @param keyParser        Keys that activate that filter and associated parsers of values
     * @param conditionCreator Condition creator function of condition that takes values produced by parser
     * @param <T1>             Type of value 1 produced
     * @param <T2>             Type of value 2 produced
     * @param <T3>             Type of value 3 produced
     * @param <T4>             Type of value 4 produced
     * @return Filter value that will be dynamically called when building conditions
     */
    public static <T1, T2, T3, T4> FilterValue4<T1, T2, T3, T4> of(@NotNull final KeyParser4<T1, T2, T3, T4> keyParser,
                                                                   @NotNull final Function<Value4<T1, T2, T3, T4>, Condition> conditionCreator) {
        return new FilterValueImpl(keyParser, conditionCreator);
    }

    /**
     * Build a filter value that will take five values to parse then pass it to condition creator function
     *
     * @param keyParser        Keys that activate that filter and associated parsers of values
     * @param conditionCreator Condition creator function of condition that takes values produced by parser
     * @param <T1>             Type of value 1 produced
     * @param <T2>             Type of value 2 produced
     * @param <T3>             Type of value 3 produced
     * @param <T4>             Type of value 4 produced
     * @param <T5>             Type of value 5 produced
     * @return Filter value that will be dynamically called when building conditions
     */
    public static <T1, T2, T3, T4, T5> FilterValue5<T1, T2, T3, T4, T5> of(@NotNull final KeyParser5<T1, T2, T3, T4, T5> keyParser,
                                                                           @NotNull final Function<Value5<T1, T2, T3, T4, T5>, Condition> conditionCreator) {
        return new FilterValueImpl(keyParser, conditionCreator);
    }

    /**
     * Build a filter value that will take six values to parse then pass it to condition creator function
     *
     * @param keyParser        Keys that activate that filter and associated parsers of values
     * @param conditionCreator Condition creator function of condition that takes values produced by parser
     * @param <T1>             Type of value 1 produced
     * @param <T2>             Type of value 2 produced
     * @param <T3>             Type of value 3 produced
     * @param <T4>             Type of value 4 produced
     * @param <T5>             Type of value 5 produced
     * @param <T6>             Type of value 6 produced
     * @return Filter value that will be dynamically called when building conditions
     */
    public static <T1, T2, T3, T4, T5, T6> FilterValue6<T1, T2, T3, T4, T5, T6> of(@NotNull final KeyParser6<T1, T2, T3, T4, T5, T6> keyParser,
                                                                                   @NotNull final Function<Value6<T1, T2, T3, T4, T5, T6>, Condition> conditionCreator) {
        return new FilterValueImpl(keyParser, conditionCreator);
    }

    /**
     * @param keyParser        KeyParser containing keys and parsers
     * @param conditionCreator Condition function of condition that takes values produced by parser
     * @param <T>              To allow to be able to not cast in condition creator function when using less values than other interfaces defined from {@link Value}
     * @return Filter value that will be dynamically called when building conditions
     * @throws InvalidParameterException If KeyParser size is less than 2
     */
    public static <T extends Value> FilterValue ofN(@NotNull final KeyParser keyParser,
                                                    @NotNull final Function<T, Condition> conditionCreator) {
        if (keyParser.size() < 2) {
            throw new InvalidParameterException("KeyParser must be of size 2 or more. Use another builder");
        }
        return new FilterValueImpl(keyParser, conditionCreator);
    }
}
