package no.laukvik.migrator;

import java.util.ArrayList;
import java.util.List;

public class Source {
    private String name;
    private List<DataType> columns;
    private Database database;

    private Source() {
        columns = new ArrayList<>();
    }

    public static Source build(){
        return new Source();
    }

    public Source table(String name) {
        this.name = name;
        return this;
    }

    public Source database(Database database) {
        this.database = database;
        return this;
    }

    public Source column(DataType datatype) {
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
