package fullOne;

import org.blackdread.filtersortjooqapi.exception.BadUsageApiException;
import org.blackdread.filtersortjooqapi.exception.FilteringApiException;
import org.blackdread.filtersortjooqapi.exception.SortingApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {

    private final Logger log = LoggerFactory.getLogger(ExceptionTranslator.class);


    @ExceptionHandler(FilteringApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processFilteringApiError(FilteringApiException ex) {
        // See jHipster for ErrorVM
        return new ErrorVM(ErrorConstants.ERR_API_FILTERING, ex.getMessage());
    }

    @ExceptionHandler(SortingApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processSortingApiError(SortingApiException ex) {
        // See jHipster for ErrorVM
        return new ErrorVM(ErrorConstants.ERR_API_SORTING, ex.getMessage());
    }

    @ExceptionHandler(BadUsageApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorVM processBadUsageApiError(BadUsageApiException ex) {
        // See jHipster for ErrorVM
        return new ErrorVM(ErrorConstants.ERR_API_BAD_USAGE, ex.getMessage());
    }
}
