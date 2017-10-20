package org.blackdread.filtersortjooqapi.contract;

import org.blackdread.filtersortjooqapi.contract.parser.FilterParser;
import org.jooq.Condition;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Yoann CAPLAIN on 2017/9/8.
 */
public abstract class AbstractFilterValue implements FilterValue {

    private final Supplier<Condition> conditionSupplier;

    private final KeyParser keyParser;

    /**
     * Object can be anything but if it is a {@link Value} then it is handled differently for user of API
     * <p>For 95% of use case, user only have one key/value so instead of using {@link Value1} we allow to pass a Function that take directly the object desired</p>
     * <p>After count of value is more than 2 then we take in input a {@link Value}</p>
     */
    private final Function<Object, Condition> conditionCreator;

    /**
     * To allow an easier condition creator for user of API instead of having {@link Value2}
     */
    private final BiFunction<Object, Object, Condition> conditionCreator2;

    private final boolean throwOnMissingKeysFound;

    AbstractFilterValue(final String key, final Supplier<Condition> conditionSupplier) {
        this.conditionSupplier = Objects.requireNonNull(conditionSupplier);
        this.keyParser = KeyParsers.of(key, FilterParser.ofIdentity()::parse);
        this.conditionCreator = null;
        this.conditionCreator2 = null;
        this.throwOnMissingKeysFound = false;
    }

    AbstractFilterValue(final KeyParser keyParser, final Function<Object, Condition> conditionCreator, boolean throwOnMissingKeysFound) {
        this.conditionSupplier = null;
        this.keyParser = Objects.requireNonNull(keyParser);
        this.conditionCreator = Objects.requireNonNull(conditionCreator);
        this.conditionCreator2 = null;
        this.throwOnMissingKeysFound = throwOnMissingKeysFound;
    }

    AbstractFilterValue(final KeyParser keyParser, final BiFunction<Object, Object, Condition> conditionCreator, boolean throwOnMissingKeysFound) {
        this.conditionSupplier = null;
        this.keyParser = Objects.requireNonNull(keyParser);
        this.conditionCreator = null;
        this.conditionCreator2 = Objects.requireNonNull(conditionCreator);
        this.throwOnMissingKeysFound = throwOnMissingKeysFound;
    }

    @Override
    public int size() {
        return keyParser.size();
    }

    @Override
    public boolean isThrowOnMissingKeysFound() {
        return throwOnMissingKeysFound;
    }

    @NotNull
    @Override
    public KeyParser getKeyParser() {
        return keyParser;
    }

    @NotNull
    @Override
    public Condition buildCondition() {
        if (!isConditionSupplier())
            throw new IllegalStateException("Condition supplier is null");
        return throwIfConditionIsull(conditionSupplier.get(), "Cannot return a null condition from a supplier");
    }

    @NotNull
    @Override
    public Condition buildCondition(List<Object> values) {
        if (isConditionSupplier())
            throw new IllegalStateException("Condition supplier is not null");
        // TODO No checking on list size as API should be safe on that but if buildCondition in FilteringJooq is overridden then cannot make sure value count is correct
        if (conditionCreator2 != null) {
            return throwIfConditionIsull(conditionCreator2.apply(values.get(0), values.get(1)), "Condition cannot be null");
        }

        // TODO could eventually allow Value1 but would require to try a casting to it of the function, if exception then we apply the value without casting otherwise we create the value

        // No checking as API does not allow to specify for one value to use Value1<T1>
        if (values.size() == 1) {
            return throwIfConditionIsull(conditionCreator.apply(values.get(0)), "Condition cannot be null");
        }

        // More than 2 values will send back a Value object
        final Value value = new ValueImpl(values);
        return throwIfConditionIsull(conditionCreator.apply(value), "Condition cannot be null");
    }

    @Override
    public boolean isConditionSupplier() {
        return conditionSupplier != null;
    }

    private Condition throwIfConditionIsull(final Condition condition, final String message) {
        if (condition == null)
            throw new IllegalStateException(message);
        return condition;
    }
}
