# Simple ORM
This is a simple ORM implementation to demonstrate the usage of the reflection API.
Well known ORM frameworks like Hibernate or EclipseLink are built based on the same principles.
To demonstrate the reflection API on methods, a simple dependency injection is also implemented with the same principles as used by Spring, CDI or Guice.
ORM and DI, are the two main applications of the reflection API.

## H2 database
1. Create DB file: `src/db-files/db-orm.mv.db`
1. Update URL in the EntityManager (without .mv.db): `jdbc:h2:<path-to>/src/db-files/db-orm`
1. Start the DBLauncher.
1. Use same URL as the EntityManager to connect with browser to DB: `jdbc:h2:<path-to>/src/db-files/db-orm`
1. Create table:
```
create table Person (
    k_id int primary key,
    c_name varchar(40),
    c_age int
)
```

## Run the ORM
Once the H2 database is setup, disconnect the browser client to be able to run:
1. WritingObjects
1. ReadingObjects

Verify the the information in the standard output.