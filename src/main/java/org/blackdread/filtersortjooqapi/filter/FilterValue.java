package org.blackdread.filtersortjooqapi.filter;

import org.blackdread.filtersortjooqapi.filter.keyparser.KeyParser;
import org.jooq.Condition;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Yoann CAPLAIN on 2017/9/7.
 */
public interface FilterValue {

    /**
     * Get the number of keys/values to be filtered (there are as many parsers)
     */
    int size();

    /**
     * @return {@link KeyParser} that will be used to get keys and parse values from keys
     */
    @NotNull
    KeyParser getKeyParser();

    /**
     * Call supplier method to get condition
     *
     * @return Condition created from supplier
     * @throws IllegalStateException If method is called when this {@link FilterValue} is <b>not</b> a condition supplier
     */
    @NotNull
    Condition buildCondition();

    /**
     * Call function method to get condition with values passed
     *
     * @param values list of values to be passed to function creator
     * @return Condition created from function creator
     * @throws IllegalStateException If method is called when this {@link FilterValue} is a condition supplier
     */
    @NotNull
    Condition buildCondition(List<Object> values);

    /**
     * @return True if should throw an exception when some keys are found but not all
     */
    boolean isThrowOnMissingKeysFound();

    /**
     * If it returns true then {@link #buildCondition()} should be called instead of {@link #buildCondition(List)}
     *
     * @return True if this FilterValue is a condition supplier
     */
    boolean isConditionSupplier();
}
