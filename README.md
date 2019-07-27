[![Build Status](https://travis-ci.org/Blackdread/filter-sort-jooq-api.svg?branch=master)](https://travis-ci.org/Blackdread/filter-sort-jooq-api)
[![Coverage Status](https://coveralls.io/repos/github/Blackdread/filter-sort-jooq-api/badge.svg?branch=master)](https://coveralls.io/github/Blackdread/filter-sort-jooq-api?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.blackdread/filter-sort-jooq-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.blackdread/filter-sort-jooq-api)
[![StackShare](https://img.shields.io/badge/tech-stack-0690fa.svg?style=flat)](https://stackshare.io/Blackdread/filter-sort-jooq-api)
<!-- [![Read the Docs](https://img.shields.io/readthedocs/Blackdread/filter-sort-jooq-api/pip.svg)](https://github.com/Blackdread/filter-sort-jooq-api) -->

# About Filter Sort jOOQ API

Main objective is to filter/sort queries built with jOOQ and to remove a 
lot of copy/paste code that filtering and sorting involves with Aliases, Validation, **if/else**, etc.

Main points:

- Build Filter for jOOQ Condition

- Build Sort for jOOQ Sort

- Simple parsing of values with always default behavior through all your app (exception is always the same, can used in Spring with ControllerAdvice for **REST API**)

- **Consistent filtering and sorting** through all app as the API forces to always write the same way to filter/sort

- Allows to not leak your internal implementation details, you can use aliases in your API

- jOOQ is great ! And this API allow to easily filter/sort complex report sql built with jOOQ 

See: http://www.blackdread.org/

# Note

Most logic have been moved to [Rest filter](https://github.com/Blackdread/rest-filter)

# Usage

It is recommended to use this API in a Spring App with jOOQ (have a dependency to spring-data-commons for Sort).

In Spring Boot, I usually build the List of Filter in an init method annotated with @PostConstruct in a repository class

Can use @RequestParam Map<String, String> requestParams and Pageable pageable, or build automatically (via 
Spring) a DTO/POJO from params by specifying it in parameters of @RequestMapping method.

Check the folder [examples](https://github.com/Blackdread/filter-sort-jooq-api/tree/master/examples) and [fullOne](https://github.com/Blackdread/filter-sort-jooq-api/tree/master/examples/fullOne) examples as it shows more how it can be used.

# Current version

Takes Map of key/alias with value and do the sorting/filtering.

Javax validation is used mostly as documentation for code (Nullable and NotNull).

Custom exception to allow to easily catch it in web environment of Spring with @ControllerAdvice and then give proper error message in REST API.

No if/else usage, everything is inside API.

## Filtering

A filter takes:
- key/alias with no value or anything as the filter is activated if that key is present and filter does not check the value (neither parse it)
- key/alias with one value (usually use FilterParser)
- key/alias with many values separated by a token (usually use FilterMultipleValueParser, result is List\<T\>)
- many keys/aliases with many values as a result (use of FilterParser and/or FilterMultipleValueParser)

Throws exception on unknown key/alias, value cannot be parsed.

## Sorting

A sort takes:
- a key/alias and a value that define ASC or DESC sorting (See Spring to use Pageable, Sort.by(...) and Sort.Order; In url it looks like "sort=key/alias,asc/desc")

Throws exception on unknown key/alias.

# Future changes

- make it compatible with java 9

- build builders for Filter creation (would be easier to add later more options e.g: handle 
the throwOnMissingKeysFound; And to add checks at build time)

- build an API to solve condition chaining with AND and OR

- maybe open more classes (for extend) of API to end-users but to check what would make sense (check AbstractFilterValue, FilterMultipleValueParserAbstract, etc)

- create more functions in FilteringJooq to allow to override logic methods for method "default Condition buildConditions(final Map<String, String> requestParams)" 

- build reflection of POJO/DTO to create Map of Key/Values to be sent to Filtering/Sorting. See PojoToMap and 
advanced version classes, it is not implemented neither used yet but it would certainly be used that way.

- Not sure -> maybe change Sort to not make it mandatory to use Spring but main idea is to be able
 to use Pageable in parameters of @RequesMapping and do pageable.getSort()

- Maybe force implementation of Map for sorting to be a Map that keep order constant -> NavigableMap, SortedMap, 
TreeMap, LinkedHashMap.
Could remove Map and instead abstract this part from user with a better Sorting API to allow 
different methods of ordering:
   - based on order each Sorting is created (internal can use LinkedHashMap or introduce a new int field)
   - based on key, natural ordering of SortedMap. 2 ways -> default or with a comparator given
   - based on current way it works -> it is based on Sort object and its returned List of Sort.Order values 
   ordering (need to check if Spring creates that list ordered based on url params ordering)
   - more ideas?

- give a secondary API that would take already parsed (and possibly already Validated) DTOs (see 
Spring Framework @Valid @RequestBody). E.g: Data coming from a request and transform into a DTO (validated?) 
then DTO passed to service/repository


### AND / OR chaining API 
It is not a priority as I did not have any use for chaining conditions in many OR/AND (usually it is always AND, could a simple feature to override with a OR).
And it requires more thought as some key/alias might be missing (ignore filter or throw or use only what's 
available if missing keys/aliases? allow duplicate key in different Filter.or/and?) in a Filter.and(..)/Filter.or(...).

Also we might need to have dynamic AND/OR at runtime -> often with check boxes in UI, so should design a parsable text API for dynamic AND/OR.

All that is already possible by using different keys/aliases.

And if use versions of Filter.of() with more than one value to be parsed then the condition chaining is already done inside the condition creator function, that is dependent on the developer code (your code).

Default behavior is already to apply a AND between each filtered values.

Might use Strategy pattern, default behavior and context given.

Something that would look like:
    
    Filter.and(Filter.of(...), Filter.of(...))
    
    Filter.or(Filter.of(...), Filter.of(...))
    
    Filter.or(
        Filter.and(Filter.of(...), Filter.of(...)), 
        Filter.or(Filter.of(...), Filter.of(...))
    )


### Reflection of POJO/DTO

In web environment it is easy to use @RequestParam Map<String, String> requestParams 
with Spring Framework but sometimes you want to be more type-safe and explicit of which 
parameters an API endpoint expects, therefore reflection can provide ease to Map 
these POJOs/DTOs to fit with Filtering and Sorting API.

Problem is that POJO/DTO might already be validated and mapped to correct type and current API would transform those to
String to then parse it again... That's something we could get rid of but to see if really necessary to implement a solution for that.

Use of annotations, like @FilterIgnore to ignore a variable in filtering of that DTO.

### Sorting Map

Something that would look like:

    To be defined 


# How to contribute pull requests

Do a pull request.
Explain your changes, reasons, write tests.

# Inspired from
Got the idea to create that API after reading this page: [using-jooq-with-spring-sorting-and-pagination](https://www.petrikainulainen.net/programming/jooq/using-jooq-with-spring-sorting-and-pagination/)



# Some links
[jOOQ dynamic SQL](https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql/)

[My Website](http://www.blackdread.org/)

[https://oss.sonatype.org/content/repositories/snapshots/org/blackdread/filter-sort-jooq-api/](https://oss.sonatype.org/content/repositories/snapshots/org/blackdread/filter-sort-jooq-api/)
