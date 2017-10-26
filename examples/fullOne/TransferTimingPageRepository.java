package fullOne;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface TransferTimingPageRepository {

    /**
     * Get listing
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TransferTimingListingDTO> getAll(Pageable pageable);

    /**
     * Get filtered listing
     *
     * @param requestParams the queries of the search
     * @param pageable      the pagination information
     * @return the list of entities
     */
    Page<TransferTimingListingDTO> filter(Map<String, String> requestParams, Pageable pageable);

    /**
     * Get listing without paging
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    List<TransferTimingListingDTO> getAllWithoutPaging(Pageable pageable);

    /**
     * Get listing without paging
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    List<TransferTimingListingDTO> filterWithoutPaging(Map<String, String> requestParams, Pageable pageable);

    /**
     * Create index
     */
    void createIndex();

    /**
     * Force create index
     *
     * @param force true for force index
     */
    void createIndex(Boolean force);
}
