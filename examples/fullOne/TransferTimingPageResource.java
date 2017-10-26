package fullOne;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing transfer timing pages.
 */
@RestController
@RequestMapping("/api")
public class TransferTimingPageResource {

    private final Logger log = LoggerFactory.getLogger(TransferTimingPageResource.class);

    private final TransferTimingPageService transferTimingPageService;

    @Autowired
    public TransferTimingPageResource(TransferTimingPageService transferTimingPageService) {
        this.transferTimingPageService = transferTimingPageService;
    }

    /**
     * GET  /transfer-timings : get all ref transfer timings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all transfer timing
     */
    @GetMapping("/transfer-timings")
    @Timed
    public ResponseEntity<List<TransferTimingListingDTO>> getAll(@ApiParam Pageable pageable) {
        final Page<TransferTimingListingDTO> page = transferTimingPageService.getAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transfer-timings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * SEARCH  /_search/transfer-timings : search for the transfer timing corresponding
     * to the query.
     *
     * @param requestParams the query of the transfer timing by user search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/transfer-timings")
    @Timed
    public ResponseEntity<List<TransferTimingListingDTO>> search(@RequestParam Map<String,String> requestParams, @ApiParam Pageable pageable) {
        Page<TransferTimingListingDTO> page = transferTimingPageService.search(requestParams, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transfer-timings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Export result to csv
     *
     * @param requestParams the query of the transfer timing search
     * @param pageable      the pagination information
     * @return downloadable csv
     */
    @GetMapping(value = "/transfer-timings.csv", produces = CsvMediaType.TEXT_CSV_VALUE)
    @Timed
    public ResponseEntity<List<TransferTimingListingDTO>> exportCsv(@RequestParam Map<String, String> requestParams, @ApiParam Pageable pageable) {
        /* ==========================
         * In this example you might not understand as I did not put the other part of code but I have a AbstractHttpMessageConverter
         * that converts object or list of objects to a CSV
         * ==========================
         */
        final List<TransferTimingListingDTO> list;

        if (requestParams != null) {
            list = transferTimingPageService.searchWithoutPaging(requestParams, pageable);
        } else {
            list = transferTimingPageService.getAllWithoutPaging(pageable);
        }

        String filename = "visual-timings-" + Instant.now().getEpochSecond() + ".csv";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename);
        headers.add("X-Save-As", filename);

        return new ResponseEntity<>(list, headers, HttpStatus.OK);
    }
}
