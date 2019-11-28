# Readme

Warning! This is an experimental API.

Copies all rows from a source table to a destination table. Both source and 
destination tables must exist before copying. Since all copying is done through Java
primitives, Migrator doesn't need to handle different databases individually. The
following data types are supported:

## Supported data types

Migrator supports all data types in provided in the JDBC API.

## Supported databases

Migrator supports all databases with a JDBC driver

## Source
Specify the connection details for the source
```java
Database db = Database
    .build()
    .url("jdbc:postgresql://localhost:5432/original")
    .driver("org.postgresql.Driver")
    .username("postgres")
    .password("helloworld");
````

Specify table and its columns to be copied.
```java
Source source = Source.build()
    .table("cstmr")
    .column(DataType.String)
    .column(DataType.String)
    .column(DataType.String)
    .column(DataType.String)
    .column(DataType.String)
    .column(DataType.Timestamp)
    .column(DataType.String)
    .database(db);
````

## Destination

Specify the connection details for the destination
```java
Database db2 = Database
    .build()
    .url("jdbc:postgresql://localhost:5432/destination")
    .driver("org.postgresql.Driver")
    .username("postgres")
    .password("helloworld");
````

Specify destination target
```java
Destination destination = Destination.build()
    .table("customer")
    .database(db2);
````

## Copying the database table

Database can be very large and depending on the size you want to adjust
the reporting to your needs. Just add a listener like below or don't use
a listener at all.

```java
Migrator migrator = new Migrator();
migrator.addListener(new MigratorListener() {
    public void starting(String table, int max) {
        System.out.println("Starting copying " + max  + " rows from " + table);
    }

    public void rowCopied(int rowIndex, String table) {
        System.out.println("Copied row " + rowIndex + " from " + table);
    }

    public void finished(String table) {
        System.out.println("Finished copying from " + table);
    }

    public void failed(String table, Exception e) {
        System.out.println("Failed copying from " + table);
    }
});
migrator.copyTable(source, destination);
````