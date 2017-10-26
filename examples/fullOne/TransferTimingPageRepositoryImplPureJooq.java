package fullOne;

import org.blackdread.filtersortjooqapi.filter.Filter;
import org.blackdread.filtersortjooqapi.filter.FilterValue;
import org.blackdread.filtersortjooqapi.filter.parser.FilterMultipleValueParser;
import org.blackdread.filtersortjooqapi.filter.parser.FilterParser;
import org.blackdread.filtersortjooqapi.sort.SortValue;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.example.config.Constants.DATABASE_TIMEZONE;
import static com.example.jooq.tables.Atable.ATABLE;
import static com.example.jooq.tables.File.FILE;
import static com.example.jooq.tables.FileTransitionHistory.FILE_TRANSITION_HISTORY;
import static com.example.jooq.tables.FileType.FILE_TYPE;
import static com.example.jooq.tables.TransitionState.TRANSITION_STATE;
import static com.example.jooq.tables.User.USER;
import static com.example.jooq.tables.Visual.VISUAL;
import static com.example.jooq.tables.VisualHasSomething.VISUAL_HAS_SOMETHING;
import static org.jooq.impl.DSL.*;

@Repository
@Transactional(readOnly = true)
public class TransferTimingPageRepositoryImplPureJooq implements TransferTimingPageRepository {

    private final Logger log = LoggerFactory.getLogger(TransferTimingPageRepositoryImplPureJooq.class);

    private final DSLContext create;

    /**
     * Define the index table
     */
    private static final Table<Record> TRANSFER_TIMING = DSL.table(DSL.name("_transfer_timing"));
    private static final Field<Long> TRANSFER_TIMING_FILE_TRANSITION_HISTORY_ID = field(name(TRANSFER_TIMING.getName(), "file_transition_history_id"), SQLDataType.BIGINT.nullable(false));
    private static final Field<Integer> TRANSFER_TIMING_VISUAL_COUNT = field(name(TRANSFER_TIMING.getName(), "visual_count"), SQLDataType.INTEGER);
    private static final Field<Integer> TRANSFER_TIMING_SOMETHING_COUNT = field(name(TRANSFER_TIMING.getName(), "something_count"), SQLDataType.INTEGER);
    private static final Field<Integer> TRANSFER_TIMING_TRANSITION_STATE_FROM_ID = field(name(TRANSFER_TIMING.getName(), "transition_state_from_id"), SQLDataType.INTEGER);
    private static final Field<Integer> TRANSFER_TIMING_TRANSITION_STATE_TO_ID = field(name(TRANSFER_TIMING.getName(), "transition_state_to_id"), SQLDataType.INTEGER);
    private static final Field<Byte> TRANSFER_TIMING_TRANSFER_STATUS = field(name(TRANSFER_TIMING.getName(), "transfer_status"), SQLDataType.TINYINT);
    private static final Field<Timestamp> TRANSFER_TIMING_TRANSFER_STARTED_AT = field(name(TRANSFER_TIMING.getName(), "transfer_started_at"), SQLDataType.TIMESTAMP);
    private static final Field<Timestamp> TRANSFER_TIMING_TRANSFER_ENDED_AT = field(name(TRANSFER_TIMING.getName(), "transfer_ended_at"), SQLDataType.TIMESTAMP);
    private static final Field<Long> TRANSFER_TIMING_TRANSFER_TIME_SPENT_IN_SECONDS = field(name(TRANSFER_TIMING.getName(), "transfer_time_spent_in_seconds"), SQLDataType.BIGINT);

    @Autowired
    public TransferTimingPageRepositoryImplPureJooq(DSLContext create) {
        this.create = create;
    }

    private Collection<SortField<?>> buildSort(final Sort sortSpecification){
        Collection<SortField<?>> querySortFields = new ArrayList<>(10);

        if (sortSpecification == null) {
            // What is the default sorting?
            // Consistent way to write it in all app?
            return querySortFields;
        }

        List<String> usedSortKey = new ArrayList<>(10);

        for (final Sort.Order specifiedField : sortSpecification) {
            String sortFieldName = specifiedField.getProperty();
            Sort.Direction sortDirection = specifiedField.getDirection();

            if (usedSortKey.contains(sortFieldName))
                throw new IllegalArgumentException("Well how should it be handled? And should be same for all app to be consistent");

            querySortFields.add(getSortedField(sortFieldName, sortDirection));
            usedSortKey.add(sortFieldName);
        }
        return querySortFields;
    }

    private SortField<?> getSortedField(final String sortFieldName, final Sort.Direction sortDirection){
        SortOrder sortOrder;
        if (sortDirection == Sort.Direction.ASC) {
            sortOrder = SortOrder.ASC;
        } else {
            sortOrder = SortOrder.DESC;
        }
        switch (sortFieldName){
            case "id":
                return TRANSFER_TIMING_FILE_TRANSITION_HISTORY_ID.sort(sortOrder);
            case "atableName":
                return ATABLE.NAME.sort(sortOrder);
            case "username":
                return USER.USERNAME.sort(sortOrder);
            case "blablabla......... etc":
                // Etc for others
                return null;
            default:
                throw new IllegalArgumentException("Well how should it be handled? And should be same for all app to be consistent");
        }
    }

    private Condition buildConditions(final Map<String, String> requestParams){
        final List<Condition> conditions = new ArrayList<>(requestParams.size());
        conditions.add(DSL.trueCondition());

        if(requestParams.containsKey("code")){
            conditions.add(FILE.CODE.likeIgnoreCase("%" + requestParams.get("code") + "%"));
        }
        if(requestParams.containsKey("username")){
            conditions.add(USER.USERNAME.likeIgnoreCase("%" + requestParams.get("username") + "%"));
        }
        if(requestParams.containsKey("atableName")){
            conditions.add(ATABLE.NAME.likeIgnoreCase("%" + requestParams.get("atableName") + "%"));
        }
        if(requestParams.containsKey("transferStatus")){
            // Ok so we use valueOf so we will get NumberFormatException if the value is not good -> which might result in html 500 internal server error in web environment
            conditions.add(TRANSFER_TIMING_TRANSFER_STATUS.eq(Byte.valueOf(requestParams.get("transferStatus"))));
        }

        // What about the case the date range is specified like (need split, parse): key=value -> transferTimingRange=date1,date2

        // Here what about checking if only one present and not the other, should throw an exception
        if(requestParams.containsKey("transferStartedAt") && requestParams.containsKey("transferEndedAt")){
            // again parse the LocalDate
            // handle or not exception
            // add the condition
        }

        // Etc
        // Do if/else
        // parse values
        // throw exceptions (but which one, will it stay consistent through all app?)
        // Keep consistent through all your app
        // Copy/paste to try to keep consistent

        // ===========
        // Here what about other keys that were not matched to a if? We just ignore? What about having a
        // consistent REST API where you expect to send data based on a consistent client behavior to pass
        // only required fields amd all fields passed should have meaning and used.
        // So no ignore and un-expected behavior, so here we should do even more tests with some more if/else
        // ===========

        return DSL.and(conditions);
    }

    /**
     * Get listing
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransferTimingListingDTO> getAll(Pageable pageable) {
        return new PageImpl<>(basicSelect(pageable), pageable, getCountMaxElements());
    }

    /**
     * Get filtered listing
     *
     * @param requestParams the queries of the search
     * @param pageable      the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransferTimingListingDTO> filter(Map<String, String> requestParams, Pageable pageable) {
        return new PageImpl<>(basicSelectFilter(pageable, requestParams), pageable, getCountMaxElements(requestParams));
    }

    /**
     * Get listing without paging
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransferTimingListingDTO> getAllWithoutPaging(Pageable pageable) {
        return basicSelectWithoutPaging(pageable);
    }

    /**
     * Get filtered listing without paging
     *
     * @param requestParams the queries of the search
     * @param pageable      the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransferTimingListingDTO> filterWithoutPaging(Map<String, String> requestParams, Pageable pageable) {
        return basicSelectFilterWithoutPaging(pageable, requestParams);
    }

    /**
     * Get count
     *
     * @return total count
     */
    private int getCountMaxElements() {
        return buildQuery(basicSelectCount()).fetchOne(0, int.class);
    }

    /**
     * Get count from filtered result
     *
     * @param requestParams the queries of the search
     * @return total count
     */
    private int getCountMaxElements(Map<String, String> requestParams) {
        return buildQuery(basicSelectCount())
            .where(buildConditions(requestParams))
            .fetchOne(0, int.class);
    }

    /**
     * Build full select query
     *
     * @param pageable the pagination information
     * @return list of dtos
     */
    private List<TransferTimingListingDTO> basicSelect(Pageable pageable) {
        return buildQuery(basicSelectQuery())
            .orderBy(buildSort(pageable.getSort()))
            .limit(pageable.getOffset(), pageable.getPageSize())
            .fetch()
            .map(TransferTimingPageRepositoryImpl::recordToDto);
    }

    /**
     * Build full select query with filter
     *
     * @param pageable      the pagination information
     * @param requestParams the queries of the search
     * @return list of dtos
     */
    private List<TransferTimingListingDTO> basicSelectFilter(Pageable pageable, Map<String, String> requestParams) {
        return buildQuery(basicSelectQuery())
            .and(buildConditions(requestParams))
            .orderBy(buildSort(pageable.getSort()))
            .limit(pageable.getOffset(), pageable.getPageSize())
            .fetch()
            .map(TransferTimingPageRepositoryImpl::recordToDto);
    }

    /**
     * Build full select query without paging
     *
     * @param pageable the pagination information
     * @return list of dtos
     */
    private List<TransferTimingListingDTO> basicSelectWithoutPaging(Pageable pageable) {
        return buildQuery(basicSelectQuery())
            .orderBy(buildSort(pageable.getSort()))
            .fetch()
            .map(TransferTimingPageRepositoryImpl::recordToDto);
    }

    /**
     * Build full select query with filter without paging
     *
     * @param pageable      the pagination information
     * @param requestParams the queries of the search
     * @return list of dtos
     */
    private List<TransferTimingListingDTO> basicSelectFilterWithoutPaging(Pageable pageable, Map<String, String> requestParams) {
        return buildQuery(basicSelectQuery())
            .and(buildConditions(requestParams))
            .orderBy(buildSort(pageable.getSort()))
            .fetch()
            .map(TransferTimingPageRepositoryImpl::recordToDto);
    }

    /**
     * Build select count query
     *
     * @return total count
     */
    private SelectSelectStep<Record1<Integer>> basicSelectCount() {
        return create.select(count());
    }

    /**
     * Build select query
     *
     * @return selected records
     */
    private SelectSelectStep<Record16<Long, Integer, Integer, String, String, String, String, Integer, Integer, String, String, String, Byte, Timestamp, Timestamp, Long>> basicSelectQuery() {
        return create.select(
            TRANSFER_TIMING_FILE_TRANSITION_HISTORY_ID,
            FILE.ID.as("fileId"),
            ATABLE.ID.as("atableId"),
            ATABLE.NAME.as("atableName"),
            USER.USERNAME,
            FILE.CODE,
            FILE_TYPE.NAME.as("fileTypeName"),
            TRANSFER_TIMING_VISUAL_COUNT,
            TRANSFER_TIMING_SOMETHING_COUNT,
            field(name("tableFromState", TRANSITION_STATE.NAME.getName()), SQLDataType.VARCHAR(50)).as("fromStatus"),
            field(name("tableToState", TRANSITION_STATE.NAME.getName()), SQLDataType.VARCHAR(50)).as("toStatus"),
            FILE_TRANSITION_HISTORY.LOCATION_IPV4,
            TRANSFER_TIMING_TRANSFER_STATUS,
            TRANSFER_TIMING_TRANSFER_STARTED_AT,
            TRANSFER_TIMING_TRANSFER_ENDED_AT,
            TRANSFER_TIMING_TRANSFER_TIME_SPENT_IN_SECONDS
        );
    }

    /**
     * Build query with select and table
     *
     * @param selectedField select field(s)
     * @param <T>           record type
     * @return built query
     */
    private <T extends Record> SelectOnConditionStep<T> buildQuery(final SelectSelectStep<T> selectedField) {
        return selectedField
            .from(TRANSFER_TIMING)
            .join(FILE_TRANSITION_HISTORY).on(FILE_TRANSITION_HISTORY.ID.eq(field(name(TRANSFER_TIMING.getName(), TRANSFER_TIMING_FILE_TRANSITION_HISTORY_ID.getName()), SQLDataType.INTEGER)))
            .join(ATABLE).on(ATABLE.ID.eq(FILE_TRANSITION_HISTORY.FILE_LISTING_ATABLE_ID))
            .join(USER).on(USER.ID.eq(FILE_TRANSITION_HISTORY.LOCATION_USER_ID))
            .join(TRANSITION_STATE.asTable("tableFromState")).on(field(name("tableFromState", TRANSITION_STATE.ID.getName())).eq(field(name(TRANSFER_TIMING.getName(), TRANSFER_TIMING_TRANSITION_STATE_FROM_ID.getName()), SQLDataType.INTEGER)))
            .join(TRANSITION_STATE.asTable("tableToState")).on(field(name("tableToState", TRANSITION_STATE.ID.getName())).eq(field(name(TRANSFER_TIMING.getName(), TRANSFER_TIMING_TRANSITION_STATE_TO_ID.getName()), SQLDataType.INTEGER)))
            .join(FILE).on(FILE.ID.eq(FILE_TRANSITION_HISTORY.FILE_ID))
            .join(FILE_TYPE).on(FILE_TYPE.ID.eq(FILE.FILE_TYPE_ID));
    }

    private static TransferTimingListingDTO recordToDto(Record16<Long, Integer, Integer, String, String, String, String, Integer, Integer, String, String, String, Byte, Timestamp, Timestamp, Long> r) {
        // You can do your mapping manually or use jooq methods like "into" etc
        return new TransferTimingListingDTO(
            r.get(TRANSFER_TIMING_FILE_TRANSITION_HISTORY_ID, Long.class),
            r.get("fileId", Long.class),
            r.get("atableId", Long.class),
            r.get("atableName", String.class),
            r.get(USER.USERNAME, String.class),
            r.get(FILE.CODE, String.class),
            r.get("fileTypeName", String.class),
            r.get(TRANSFER_TIMING_VISUAL_COUNT, Integer.class),
            r.get(TRANSFER_TIMING_SOMETHING_COUNT, Integer.class),
            r.get(field("fromStatus"), String.class),
            r.get(field("toStatus"), String.class),
            r.get(FILE_TRANSITION_HISTORY.LOCATION_IPV4, String.class),
            r.get(TRANSFER_TIMING_TRANSFER_STATUS, Boolean.class),
            // Set zone to specific one if you are not using UTC (you should)
            r.get(TRANSFER_TIMING_TRANSFER_STARTED_AT).toLocalDateTime().atZone(DATABASE_TIMEZONE),
            r.get(TRANSFER_TIMING_TRANSFER_ENDED_AT).toLocalDateTime().atZone(DATABASE_TIMEZONE),
            r.get(TRANSFER_TIMING_TRANSFER_TIME_SPENT_IN_SECONDS, Long.class)
        );
    }

    /**
     * Create index
     */
    @Transactional(readOnly = false)
    public void createIndex() {
        buildIndex();
    }

    /**
     * Force create index
     *
     * @param force true for force index
     */
    @Transactional(readOnly = false)
    public void createIndex(Boolean force) {
        if (force.equals(true)) {
            create.dropTableIfExists(TRANSFER_TIMING).execute();
        }

        buildIndex();
    }

    /**
     * Build index
     */
    private void buildIndex() {
        int tableExist =
            create.select(count())
                .from("information_schema.tables")
                .where("table_schema = '" + FILE.getSchema().getName() + "'")
                .and("table_name = '" + TRANSFER_TIMING.getName() + "'")
                .fetchOne(0, int.class);

        if (tableExist == 0) {
            createIndexTable();
        }

        final Select<Record1<Long>> indexedIds = create.select(
            TRANSFER_TIMING_FILE_TRANSITION_HISTORY_ID
        ).from(TRANSFER_TIMING);


        // select and compute records
        final Field<Integer> visualCount = count(VISUAL.ID).as("visualCount");
        final Table<Record2<Integer, Integer>> tableVisualCount = create.select(
            visualCount,
            VISUAL.FILE_ID
        ).from(VISUAL)
            .groupBy(VISUAL.FILE_ID)
            .asTable("tableVisualCount");

        final Field<Integer> somethingCount = count().as("somethingCount");
        final Table<Record2<Integer, Integer>> tablesomethingCount = create.select(
            somethingCount,
            VISUAL_HAS_SOMETHING.VISUAL_FILE_ID
        ).from(VISUAL_HAS_SOMETHING)
            .groupBy(VISUAL_HAS_SOMETHING.VISUAL_FILE_ID)
            .asTable("tablesomethingCount");

        SelectOnConditionStep<Record6<Integer, Integer, Integer, Integer, Integer, Timestamp>> query = create.select(
            FILE_TRANSITION_HISTORY.ID,
            FILE_TRANSITION_HISTORY.FILE_ID,
            tableVisualCount.field(visualCount.getName(), SQLDataType.INTEGER),
            tablesomethingCount.field(somethingCount.getName(), SQLDataType.INTEGER),
            FILE_TRANSITION_HISTORY.TRANSITION_STATE_ID,
            FILE_TRANSITION_HISTORY.CREATE_TIME
        ).from(FILE_TRANSITION_HISTORY)
            .join(USER).on(FILE_TRANSITION_HISTORY.LOCATION_USER_ID.eq(USER.ID))
            .join(TRANSITION_STATE).on(FILE_TRANSITION_HISTORY.TRANSITION_STATE_ID.eq(TRANSITION_STATE.ID))
            .join(FILE).on(FILE_TRANSITION_HISTORY.FILE_ID.eq(FILE.ID))
            .join(FILE_TYPE).on(FILE.FILE_TYPE_ID.eq(FILE_TYPE.ID))
            .join(ATABLE).on(FILE_TRANSITION_HISTORY.FILE_LISTING_ATABLE_ID.eq(ATABLE.ID))
            .leftJoin(tableVisualCount).on(FILE_TRANSITION_HISTORY.FILE_ID.eq(tableVisualCount.field(VISUAL.FILE_ID)))
            .leftJoin(tablesomethingCount).on(FILE_TRANSITION_HISTORY.FILE_ID.eq(tablesomethingCount.field(VISUAL_HAS_SOMETHING.VISUAL_FILE_ID)));

        query.where(field(name(FILE_TRANSITION_HISTORY.getName(), FILE_TRANSITION_HISTORY.ID.getName()), SQLDataType.BIGINT).notIn(indexedIds));

        query.orderBy(FILE_TRANSITION_HISTORY.FILE_ID.asc(), FILE_TRANSITION_HISTORY.CREATE_TIME.asc(), FILE_TRANSITION_HISTORY.ID.asc());

        Result<Record6<Integer, Integer, Integer, Integer, Integer, Timestamp>> result = query.fetch();

        Long fileIdTemp = 0L;
        // Here are possible status (usually you use an Enum)
        Integer statusTransferring = 2;
        Integer statusTransferred = 3;
        Integer statusError = 1;
        ZonedDateTime transferStartedAt = null;
        ZonedDateTime transferEndedAt = null;
        Integer transitionStateFromId = null;
        Integer transitionStateToId = null;
        Boolean transferStatus = false;
        Long durationInSeconds = 0L;
        List<TransferTimingIndexDTO> transferTimingIndexDTOs = new ArrayList<>();

        boolean gotTransferring = false;

        for (Record r : result) {
            Long fileTransitionHistoryId = r.get(FILE_TRANSITION_HISTORY.ID, Long.class);
            Long fileId = r.get(FILE_TRANSITION_HISTORY.FILE_ID, Long.class);
            Integer visualCountValue = r.get(visualCount, Integer.class);
            Integer somethingCountValue = r.get(somethingCount, Integer.class);
            Integer transitionStateId = r.get(FILE_TRANSITION_HISTORY.TRANSITION_STATE_ID, Integer.class);
            ZonedDateTime createTime = r.get(FILE_TRANSITION_HISTORY.CREATE_TIME).toLocalDateTime().atZone(DATABASE_TIMEZONE);

            if (!fileIdTemp.equals(fileId)) {
                gotTransferring = false;
                fileIdTemp = fileId;
            }

            if (statusTransferring.equals(transitionStateId)) {
                gotTransferring = true;
                transferStartedAt = createTime;
                transitionStateFromId = transitionStateId;
                continue;
            }

            if (statusTransferred.equals(transitionStateId) || statusError.equals(transitionStateId)) {
                if (!gotTransferring) {
                    continue;
                }
                transferEndedAt = createTime;
                durationInSeconds = Duration.between(transferStartedAt, transferEndedAt).toMillis() / 1000;
                transitionStateToId = transitionStateId;

                transferStatus = statusTransferred.equals(transitionStateId);
            }

            transferTimingIndexDTOs.add(new TransferTimingIndexDTO(
                fileTransitionHistoryId,
                visualCountValue,
                somethingCountValue,
                transitionStateFromId,
                transitionStateToId,
                transferStatus,
                transferStartedAt,
                transferEndedAt,
                durationInSeconds
            ));
        }

        if (transferTimingIndexDTOs.size() > 0) {
            // build insert statement
            InsertValuesStep9<Record, Long, Integer, Integer, Integer, Integer, Byte, Timestamp, Timestamp, Long> insert = create.insertInto(TRANSFER_TIMING,
                TRANSFER_TIMING_FILE_TRANSITION_HISTORY_ID,
                TRANSFER_TIMING_VISUAL_COUNT,
                TRANSFER_TIMING_SOMETHING_COUNT,
                TRANSFER_TIMING_TRANSITION_STATE_FROM_ID,
                TRANSFER_TIMING_TRANSITION_STATE_TO_ID,
                TRANSFER_TIMING_TRANSFER_STATUS,
                TRANSFER_TIMING_TRANSFER_STARTED_AT,
                TRANSFER_TIMING_TRANSFER_ENDED_AT,
                TRANSFER_TIMING_TRANSFER_TIME_SPENT_IN_SECONDS);

            transferTimingIndexDTOs.forEach(e -> insert.values(
                e.getFileTransitionHistoryId(),
                e.getVisualCount(),
                e.getsomethingCount(),
                e.getTransitionStateFromId(),
                e.getTransitionStateToId(),
                ByteUtil.booleanToByte(e.getTransferStatus()),
                Timestamp.valueOf(e.getTransferStartedAt().toLocalDateTime()),
                Timestamp.valueOf(e.getTransferEndedAt().toLocalDateTime()),
                e.getTransferTimeSpentInSeconds()));

            // execute batch insert
            insert.execute();
        }
    }

    /**
     * Create index table
     */
    private void createIndexTable() {
        create.createTableIfNotExists(TRANSFER_TIMING)
            .column(TRANSFER_TIMING_FILE_TRANSITION_HISTORY_ID)
            .column(TRANSFER_TIMING_VISUAL_COUNT)
            .column(TRANSFER_TIMING_SOMETHING_COUNT)
            .column(TRANSFER_TIMING_TRANSITION_STATE_FROM_ID)
            .column(TRANSFER_TIMING_TRANSITION_STATE_TO_ID)
            .column(TRANSFER_TIMING_TRANSFER_STATUS)
            .column(TRANSFER_TIMING_TRANSFER_STARTED_AT)
            .column(TRANSFER_TIMING_TRANSFER_ENDED_AT)
            .column(TRANSFER_TIMING_TRANSFER_TIME_SPENT_IN_SECONDS)
            .constraints(
                constraint("primary").primaryKey(TRANSFER_TIMING_FILE_TRANSITION_HISTORY_ID)
            )
            .execute();

        String sql = "ALTER TABLE " + TRANSFER_TIMING.getName() + " ENGINE = INNODB";
        create.execute(sql);

        create.createIndex("transition_state_from_id").on(TRANSFER_TIMING, TRANSFER_TIMING_TRANSITION_STATE_FROM_ID).execute();
        create.createIndex("transition_state_to_id").on(TRANSFER_TIMING, TRANSFER_TIMING_TRANSITION_STATE_TO_ID).execute();
    }
}
