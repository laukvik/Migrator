# Readme

Copies all rows from a source table to a destination table. Both source and 
destination tables must exist before copying.


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

Specify table and its columns to be copied
```java
Source source = Source.build()
    .name("inntektsmelding")
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
    .name("inntektsmelding")
    .database(db2);
````

## Start

Log every 10 lines of rows being copied
```java
Migrator migrator = new Migrator();
migrator.addListener(new MigratorListener() {
    public void rowCopied(int rowIndex, String table) {
        System.out.println("Copied row " + rowIndex + " from " + table);
    }
});
migrator.copyTable(source, destination, 10);
````