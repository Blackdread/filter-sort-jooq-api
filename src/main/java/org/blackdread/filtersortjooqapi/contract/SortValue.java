package org.blackdread.filtersortjooqapi.contract;

import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.TableField;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Created by Yoann CAPLAIN on 2017/8/25.
 */
public interface SortValue {

    static SortValue of(@NotNull TableField tableField) {
        return new SortValueImpl(tableField);
    }

    static SortValue of(@NotNull Field field) {
        return new SortValueImpl(field);
    }

    /**
     * If empty then {@link #getField()} is not
     *
     * @return TableField to sort on if not empty
     * @deprecated maybe not useful
     */
    Optional<TableField> getTableField();

    /**
     * If empty then {@link #getTableField()} is not
     *
     * @return Field to sort on if not empty
     * @deprecated maybe not useful
     */
    Optional<Field> getField();

    /**
     *
     * @param sortOrder Sort order to apply to field
     * @return Field already sorted by given sort order
     */
    SortField getSortField(final SortOrder sortOrder);

    /**
     *
     * @param sortOrder Sort order to apply to field
     * @return Field sorted and null first
     */
    default SortField getSortFieldNullFirst(final SortOrder sortOrder) {
        return getSortField(sortOrder).nullsFirst();
    }

    /**
     *
     * @param sortOrder Sort order to apply to field
     * @return Field sorted and null last
     */
    default SortField getSortFieldNullLast(final SortOrder sortOrder) {
        return getSortField(sortOrder).nullsLast();
    }
}
