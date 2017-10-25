package org.blackdread.filtersortjooqapi.filter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class FilteringJooqImpl2 implements FilteringJooq {

    private final List<FilterValue> filterValues = new ArrayList<>();

    @Override
    public @NotNull List<FilterValue> getFilterValues() {
        return filterValues;
    }

    @Override
    public boolean isSkipNotPresentValue() {
        return true;
    }
}
