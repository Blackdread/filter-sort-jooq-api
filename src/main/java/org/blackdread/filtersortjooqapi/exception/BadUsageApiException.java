package org.blackdread.filtersortjooqapi.exception;

/**
 * <p>Exception thrown in methods that expects arguments sent by users and application cannot
 * (or choose not to) infer default behavior from it</p>
 * Created by Yoann CAPLAIN on 2017/8/29.
 */
public class BadUsageApiException extends RuntimeException {

    public BadUsageApiException(String message) {
        super(message);
    }
}
