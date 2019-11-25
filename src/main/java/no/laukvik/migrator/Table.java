package no.laukvik.migrator;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String name;
    private List<DataType> columns;
    private Database database;

    private Table() {
        columns = new ArrayList<>();
    }

    public static Table build(){
        return new Table();
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

    public Database getDatabase() {
        return database;
    }

    public List<DataType> getColumns() {
        return columns;
    }

    public String getName() {
        return name;
    }

}
