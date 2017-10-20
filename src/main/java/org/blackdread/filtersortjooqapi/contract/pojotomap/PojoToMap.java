package org.blackdread.filtersortjooqapi.contract.pojotomap;

import java.util.Map;

/**
 * <p>Simple interface to let user manually do the mapping of POJO/DTO to the Map to be used with Filtering or Sorting</p>
 * Created by Yoann CAPLAIN on 2017/10/15.
 */
public interface PojoToMap {

    /**
     * User should map fields to be Filtered/Sorted on of object it implements this interface.
     *
     * @return Map of keys/aliases with there values to be applied to Sorting or Filtering
     */
    Map<String, String> toMap();
}
