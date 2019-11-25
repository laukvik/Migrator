package no.laukvik.migrator;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String name;
    private List<DataType> columns;
    private Database database;

    public Table() {
        columns = new ArrayList<>();
    }

    public Table name(String name) {
        this.name = name;
        return this;
    }

    public Table database(Database database) {
        this.database = database;
        return this;
    }

    public Table column(DataType datatype) {
        this.columns.add(datatype);
        return this;
    }

    public Table columns(List<DataType> columns) {
        this.columns = columns;
        return this;
    }

    public Database getDatabase() {
        return database;
    }



    public List<DataType> getColumns() {
        return columns;
    }

    public void setColumns(List<DataType> columns) {
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
