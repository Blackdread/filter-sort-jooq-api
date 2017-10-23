package org.blackdread.filtersortjooqapi.sort;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yoann CAPLAIN on 2017/10/23.
 */
public class SortingJooqImpl1 implements SortingJooq {
    // Could try that with Mockito but no point here

    private final Map<String, SortValue> map = new HashMap<>();

    @Override
    public @NotNull Map<String, SortValue> getSortAliasMapping() {
        return map;
    }
}
