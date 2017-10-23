package org.blackdread.filtersortjooqapi.sort;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO Might replace that with Mockito
 * Created by Yoann CAPLAIN on 2017/10/23.
 */
public class SortingJooqImpl1 implements SortingJooq {

    private final Map<String, SortValue> map = new HashMap<>();

    @Override
    public @NotNull Map<String, SortValue> getSortAliasMapping() {
        return map;
    }
}
