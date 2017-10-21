package org.blackdread.filtersortjooqapi.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Yoann CAPLAIN on 2017/10/20.
 */
public class ExceptionTest {

    @Test
    void canCreateBadApiUsageException() {
        final BadUsageApiException test = new BadUsageApiException("test");
        Assertions.assertEquals("test", test.getMessage());
    }

    @Test
    void canCreateFilteringApiException() {
        final BadUsageApiException test = new FilteringApiException("test");
        Assertions.assertEquals("test", test.getMessage());
    }

    @Test
    void canCreateSortingApiException() {
        final BadUsageApiException test = new SortingApiException("test");
        Assertions.assertEquals("test", test.getMessage());
    }
}
