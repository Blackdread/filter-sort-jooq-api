package org.blackdread.filtersortjooqapi.sort;

import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.TableField;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by Yoann CAPLAIN on 2017/8/25.
 */
public class SortValueImpl implements SortValue {

    private final TableField tableField;
    private final Field field;

    SortValueImpl(TableField tableField) {
        Objects.requireNonNull(tableField);
        this.tableField = tableField;
        this.field = null;
    }

    SortValueImpl(Field field) {
        Objects.requireNonNull(field);
        this.field = field;
        this.tableField = null;
    }

    @Override
    public Optional<TableField> getTableField() {
        return Optional.ofNullable(tableField);
    }

    @Override
    public Optional<Field> getField() {
        return Optional.ofNullable(field);
    }

    @Override
    public SortField getSortField(final SortOrder sortOrder) {
        return tableField == null ? field.sort(sortOrder) : tableField.sort(sortOrder);
    }
}
