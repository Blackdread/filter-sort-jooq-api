[![Build Status](https://travis-ci.org/Blackdread/filter-sort-jooq-api.svg?branch=master)](https://travis-ci.org/Blackdread/filter-sort-jooq-api)
[![Coverage Status](https://coveralls.io/repos/github/Blackdread/filter-sort-jooq-api/badge.svg?branch=master)](https://coveralls.io/github/Blackdread/filter-sort-jooq-api?branch=master)
[![Maven Central](Not yet)](https://github.com/Blackdread/filter-sort-jooq-api)
[![ReadTheDocs](Not yet)](https://github.com/Blackdread/filter-sort-jooq-api)

# Note
First commit will contains everything in root, next release (as soon as travis and coveralls are setup) will 
use better packaging to separate Filtering and Sorting, and also review the package "contract".

Next commits will also review some parts of current  API, so it might break things but this to solve some minors issues.
Give more access on with many small methods FilteringJooq, maybe give access to ValueImpl

Next commits will review POM to allow to make it available it maven with jar, source, docs, etc. An important step to be done soon.

# About Filter Sort jOOQ API

Main objective is to filter/sort queries built with jOOQ and to remove a 
lot of copy/paste code that filtering and sorting involves with Aliases, Validation, etc.

Main points:

- Build Filter for jOOQ Condition

- Build Sort for jOOQ Sort

- Simple parsing of values with always default behavior through all your app

- Allows to not leak your internal implementation details, you can use aliases in your API

- jOOQ is great ! And this API allow to easily filter/sort complex report sql built with jOOQ 

See: http://www.blackdread.org/

#### Personal usage

I use this API in a Spring Boot App with Spring Data JPA and jOOQ.

In Spring Boot, I usually build the List of Filter in an init method annotated with @PostConstruct in a repository class

You can use @RequestParam Map<String, String> requestParams and Pageable pageable, or build automatically (via 
Spring) a DTO/POJO from params by specifying it in parameters of @RequestMapping method.


# Current version

Takes Map of key/alias with value and do the sorting/filtering


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


##### AND / OR chaining API 
It is not a priority as I did not have any use for chaining conditions in many OR/AND.

Default behavior is already to apply a AND between each filtered values.

Something that would look like:
    
    Filter.and(Filter.of(...), Filter.of(...))
    
    Filter.or(Filter.of(...), Filter.of(...))
    
    Filter.or(
        Filter.and(Filter.of(...), Filter.of(...)), 
        Filter.or(Filter.of(...), Filter.of(...))
    )


##### Reflection of POJO/DTO

In web environment it is easy to use @RequestParam Map<String, String> requestParams 
with Spring Framework but sometimes you want to be more type-safe and explicit of which 
parameters an API endpoint expects, therefore reflection can provide ease to Map 
these POJOs/DTOs to fit with Filtering and Sorting API.

Problem is that POJO/DTO might already be validated and mapped to correct type and current API would transform those to
String to then parse it again... That's something we could get rid of but to see if really necessary to implement a solution for that.


##### Sorting Map

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


