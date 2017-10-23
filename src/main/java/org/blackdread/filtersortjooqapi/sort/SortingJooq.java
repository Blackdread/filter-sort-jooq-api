package org.blackdread.filtersortjooqapi.sort;

import org.blackdread.filtersortjooqapi.exception.SortingApiException;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.springframework.data.domain.Sort;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>Interface to  be implemented by repository that wants to provide sorting</p>
 * <p>Interface defines default methods to limit copy/paste code while still providing a way to override
 * default behavior. Using an abstract class would allow to hide internal methods but wait
 * for Java 9 private methods :)</p>
 * Created by Yoann CAPLAIN on 2017/8/25.
 */
public interface SortingJooq {

    /**
     * Empty immutable list for sorting
     *
     * @return an empty immutable sort list
     */
    @NotNull
    default Collection<SortField<?>> emptySorting() {
        return Collections.emptyList();
    }

    /**
     * TODO Change to List for return value
     *
     * @param sortSpecification Sort from Spring Data
     * @return Collection of fields to sort (can be empty if {@code sortSpecification} is null or no sort values)
     * @throws SortingApiException if any sort field given cannot be found (it should result in a 400 error message) or sort contains duplicate keys
     */
    @NotNull
    default Collection<SortField<?>> buildOrderBy(@Nullable final Sort sortSpecification) {
        Collection<SortField<?>> querySortFields = new ArrayList<>(10);

        if (sortSpecification == null) {
            return getDefaultSorting();
        }

        List<String> usedSortKey = new ArrayList<>(10);

        for (final Sort.Order specifiedField : sortSpecification) {
            String sortFieldName = specifiedField.getProperty();
            Sort.Direction sortDirection = specifiedField.getDirection();

            if (usedSortKey.contains(sortFieldName))
                throw new SortingApiException("Cannot sort on duplicate keys");

            SortValue sortValue = getTableField(sortFieldName);
            SortField<?> querySortField = convertTableFieldToSortField(sortValue, sortDirection);
            querySortFields.add(querySortField);
            usedSortKey.add(sortFieldName);
        }

        return querySortFields;
    }


    /**
     * Is not supposed to be called from external of interface (might be private in Java 9)
     *
     * @param sortFieldName name of mapping field
     * @return SortValue associated with given sort field name
     * @throws SortingApiException if sort field cannot be found (it should result in a 400 error message)
     */
    @NotNull
    default SortValue getTableField(String sortFieldName) {
        SortValue sortValue = getSortAliasMapping().get(sortFieldName);
        if (sortValue == null) {
            throw new SortingApiException("Could not find sort field: " + sortFieldName);
        }
        return sortValue;
    }

    /**
     * Is not supposed to be called from external of interface (might be private in Java 9)
     *
     * @param sortValue     SortValue to get SortField from
     * @param sortDirection Direction of sorting from request
     * @return A field sorted for jOOQ
     */
    @NotNull
    default SortField<?> convertTableFieldToSortField(SortValue sortValue, Sort.Direction sortDirection) {
        if (sortDirection == Sort.Direction.ASC) {
            return sortValue.getSortField(SortOrder.ASC);
        } else {
            return sortValue.getSortField(SortOrder.DESC);
        }
    }

    /**
     * <p>Key contains key of sorting, it can represent a real column or an alias</p>
     *
     * @return Map of keys that represents sort fields for jOOQ
     */
    @NotNull
    Map<String, SortValue> getSortAliasMapping();

    /**
     * TODO Change to List for return value
     * <p>Return default sorting. An empty collection if not sorting values set or the first one defined.</p>
     * <p>Overridden implementation can set many field to sort on by default</p>
     *
     * @return Collection of field to sort on (can be empty)
     */
    @NotNull
    default Collection<SortField<?>> getDefaultSorting() {
        final Collection<SortValue> values = getSortAliasMapping().values();
        if (values.isEmpty())
            return emptySorting();
        return Collections.singletonList(values.iterator().next().getSortField(SortOrder.DESC));
    }
}
