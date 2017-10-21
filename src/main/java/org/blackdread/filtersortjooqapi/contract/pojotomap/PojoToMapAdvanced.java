package org.blackdread.filtersortjooqapi.contract.pojotomap;

import java.util.Map;

/**
 * <p>Allow to let Reflection of POJO/DTO to create the Map or a list of Object to be passed to
 * the condition builder (depends if pojo is already validated)</p>
 * <p>Reflection is cached for each object to make it faster on re-use</p>
 * Created by Yoann CAPLAIN on 2017/10/15.
 */
public interface PojoToMapAdvanced extends PojoToMap {

    default Map<String, String> toMap() {
        // TODO not done
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
