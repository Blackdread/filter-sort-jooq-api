@Repository
@Transactional(readOnly = true)
public class ExampleOneRepositoryImpl implements SortingJooq, FilteringJooq, ExampleOneRepository {

    private final DSLContext create;

    private final Map<String, SortValue> sortAliasMapping = new HashMap<>(7);

    private final List<FilterValue> filterValues = new ArrayList<>(4);

    // A jOOQ Object table
    // Either define all you use here or everytime you redefine it in different methods...
    private final ANY_TABLE anyTable = ANY_TABLE.as("r");

    private final Field<MyType> myFieldBlaBla = DSL.field("blabl", MyType.class).as("blablablabllbal");

    @Autowired
    public ExampleOneRepositoryImpl(final DSLContext create) {
        this.create = create;
    }

    @PostConstruct
    public void init() {
        sortAliasMapping.put("id", SortValue.of(anyTable.ID));
        sortAliasMapping.put("listingName", SortValue.of(LISTING.NAME.as("listingName")));
        sortAliasMapping.put("code", SortValue.of(anyTable.CODE));
        sortAliasMapping.put("companyName", SortValue.of(COMPANY.NAME.as("companyName")));
        sortAliasMapping.put("myAlias", SortValue.of(A_TABLE.as("s").NAME.as("lastBlaBlaDone")));
        // for this one the table is internal in the select but we can take any table, take a field and alias it
        sortAliasMapping.put("lastHistoryTime", SortValue.of(HISTORY.CREATE_TIME.as("anotherAlias")));

        filterValues.add(Filter.of("myAlias2", value1 -> FilterParser.ofInt().parseWithApiException(value1), value -> A_TABLE.as("s").ID.eq(value)));
        filterValues.add(Filter.of("code", FilterParser.ofIdentity()::parse, value -> anyTable.CODE.likeIgnoreCase("%" + value + "%")));
        filterValues.add(Filter.of("id", FilterParser.ofInt()::parseWithApiException, anyTable.ID::eq));

        // ====== ====== ====== ====== ====== ======
        // it is even possible to use:
        // DSL.field("blabla")
        // DSL.field("blabla", MyType.class)
        // etc
        // ====== ====== ====== ====== ====== ======
    }

    @Override
    public Page<AnObjectDTO> getAll(Pageable pageable) {
        return new PageImpl<>(basicSelect(pageable), pageable, getCountMaxElements());
    }

    @Override
    public Page<AnObjectDTO> filter(Map<String, String> requestParams, Pageable pageable) {
        return new PageImpl<>(basicSelectFilter(pageable, requestParams), pageable, getCountMaxElements(requestParams));
    }

    /*
     * ====================
     * create.fetchCount() is nice but you might want to count differently sometimes as this is not efficient
     * So do not forget that jOOQ allow to return SelectStatement to build your queries, so make use of it, compose your methods for building your query.
     * Make use of generic methods to be able to chain Select methods and then the common business logic of the query to finally return the values with the correct return type with generics
     * ====================
     */

    private int getCountMaxElements() {
        return create.fetchCount(basicSelectQuery());
    }

    private int getCountMaxElements(Map<String, String> requestParams) {
        return create.fetchCount(
            basicSelectQuery()
                .and(buildConditions(requestParams))
        );
    }

    private List<AnObjectDTO> basicSelect(Pageable pageable) {
        return basicSelectQuery()
            .orderBy(buildOrderBy(pageable.getSort()))
            .limit(pageable.getOffset(), pageable.getPageSize())
            .fetch()
            .map(ExampleOneRepositoryImpl::recordToDto);
    }

    private List<AnObjectDTO> basicSelectFilter(Pageable pageable, Map<String, String> requestParams) {
        return basicSelectQuery()
            .and(buildConditions(requestParams))
            .orderBy(buildOrderBy(pageable.getSort()))
            .limit(pageable.getOffset(), pageable.getPageSize())
            .fetch()
            .map(ExampleOneRepositoryImpl::recordToDto);
    }

    @NotNull
    @Override
    public Map<String, SortValue> getSortAliasMapping() {
        return sortAliasMapping;
    }

    @Override
    public List<FilterValue> getFilterValues() {
        return filterValues;
    }


    private static AnObjectDTO recordToDto(Record21<Integer, Integer, String, Integer, String, String, String, Byte, Integer, String, Integer, String, String, String, Integer, String, String, String, String, String, Timestamp> r) {
        // Let's say you want to do your own mapping, here you can or let jOOQ do, etc
    }

    /**
     * End with 'where is not considered finished'
     *
     * @return
     */
    private SelectConditionStep<Record21<Integer, Integer, String, Integer, String, String, String, Byte, Integer, String, Integer, String, String, String, Integer, String, String, String, String, String, Timestamp>> basicSelectQuery() {
        // Define your fields as well if you prefer (but you could put that at top of class instead, better)
        final ATableBlabbla rsh1 = A_TABLE_BLABLA.as("rsh1");
        final ATableBlabbla rsh2 = A_TABLE_BLABLA.as("rsh2");

        // Build your internal select -> sub-select etc
        final Table<Record5<Integer, Integer, Integer, Integer, Timestamp>> lastBlaBla = create.select(
            // your fields
            .from(rsh1)
            // any join
            .leftJoin(rsh2).on(rsh1.BLABLA_ID.eq(rsh2.BLABLA_ID)
                .and(rsh1.ID.lessThan(rsh2.ID)))
            .where(rsh2.ID.isNull()).asTable("blablalablbalbal");

        return create.select(
            // put your fields
            )
            .from(anyTable)
            // do your joins
            //.join(BLABLA).on(BLABLABLAB)
            // can put a where clause that does not depend on any filter passed by user
            .where(anyTable.IS_DELETED.isFalse());
    }
}
