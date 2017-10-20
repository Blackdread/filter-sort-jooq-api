package org.blackdread.filtersortjooqapi.contract;

import org.jooq.Condition;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * TODO Later should put a builder to make it easier to handle supplier or creator and then optional parameters like throw, skip, etc
 * Created by Yoann CAPLAIN on 2017/8/25.
 */
@SuppressWarnings({"unchecked"})
class FilterValueImpl<T1, T2, T3, T4, T5, T6> extends AbstractFilterValue implements
    FilterValue1<T1>,
    FilterValue2<T1, T2>,
    FilterValue3<T1, T2, T3>,
    FilterValue4<T1, T2, T3, T4>,
    FilterValue5<T1, T2, T3, T4, T5>,
    FilterValue6<T1, T2, T3, T4, T5, T6> {


    /**
     * Useful when only want to find a key that has no value but activates a condition for filtering
     * @param key Key that will activate that condition supplier
     * @param conditionSupplier Condition supplier that will be used if {@code key} is found
     */
    FilterValueImpl(final String key, final Supplier<Condition> conditionSupplier) {
        super(key, conditionSupplier);
    }

    /**
     *
     * @param keyParser KeyParser for keys
     * @param conditionCreator The condition creator of a one variable. Can be anything {@link Object} and {@link Value}
     * @param throwOnMissingKeysFound True if should throw exception if one key is found but other is not
     */
    FilterValueImpl(final KeyParser keyParser, final Function<Object, Condition> conditionCreator, final boolean throwOnMissingKeysFound) {
        super(keyParser, conditionCreator, throwOnMissingKeysFound);
    }

    /**
     * <p>Default valuefor {@link #throwOnMissingKeysFound} is True. Should throw exception if one key is found but other is not</p>
     * @param keyParser KeyParser for keys
     * @param conditionCreator The condition creator of a one variable. Can be anything {@link Object} and {@link Value}
     */
    FilterValueImpl(final KeyParser keyParser, final Function<Object, Condition> conditionCreator) {
        this(keyParser, conditionCreator, true);
    }

    /**
     * Easier for user of API to have a method that contains two variables instead of {@link Value2}
     *
     * @param keyParser               KeyParser for keys
     * @param conditionCreator        The condition creator of two variables
     * @param throwOnMissingKeysFound True if should throw exception if one key is found but other is not
     */
    FilterValueImpl(final KeyParser keyParser, final BiFunction<Object, Object, Condition> conditionCreator, boolean throwOnMissingKeysFound) {
        super(keyParser, conditionCreator, throwOnMissingKeysFound);
    }

    /**
     * Easier for user of API to have a method that contains two variables instead of {@link Value2}.
     * <p>Default valuefor {@link #throwOnMissingKeysFound} is True. Should throw exception if one key is found but other is not</p>
     *
     * @param keyParser        KeyParser for keys
     * @param conditionCreator the condition creator of two variables
     */
    FilterValueImpl(final KeyParser keyParser, final BiFunction<Object, Object, Condition> conditionCreator) {
        this(keyParser, conditionCreator, true);
    }

    @Override
    public KeyParser1<T1> getKeyParser1() {
        return (KeyParser1<T1>) getKeyParser();
    }

    @Override
    public KeyParser2<T1, T2> getKeyParser2() {
        return (KeyParser2<T1, T2>) getKeyParser();
    }

    @Override
    public KeyParser3<T1, T2, T3> getKeyParser3() {
        return (KeyParser3<T1, T2, T3>) getKeyParser();
    }

    @Override
    public KeyParser4<T1, T2, T3, T4> getKeyParser4() {
        return (KeyParser4<T1, T2, T3, T4>) getKeyParser();
    }

    @Override
    public KeyParser5<T1, T2, T3, T4, T5> getKeyParser5() {
        return (KeyParser5<T1, T2, T3, T4, T5>) getKeyParser();
    }

    @Override
    public KeyParser6<T1, T2, T3, T4, T5, T6> getKeyParser6() {
        return (KeyParser6<T1, T2, T3, T4, T5, T6>) getKeyParser();
    }
}
