# Note

In this example, I do not give the SQL to build the tables but all variables like "TRANSITION_STATE" should be treated as tables from generated jOOQ tables.
So you cannot compile and other classes are just some small extracted parts.

I have include the class "ExceptionTranslator" to show how FilteringApiException and SortingApiException can be handled in REST API environment.

Using the API allow to be consistent through the APP by using "same code" and  respecting exception for parsing values sent by users of REST API.

# Index table

I put a dynamic index table example to try to show the filtering/sorting done on it after it is created.

# FilterMultipleValueParser and Filter with more than 1 key/alias

A good example of "FilterMultipleValueParser" is used for date range, as well for using a filter of 2 keys.
