package no.laukvik.migrator;


public class Destination {
    private String name;
    private Database database;

    private Destination() {
    }

    public static Destination build(){
        return new Destination();
    }

    public Destination table(String name) {
        this.name = name;
        return this;
    }

    public Destination database(Database database) {
        this.database = database;
        return this;
    }

    public String getName() {
        return name;
    }

    public Database getDatabase() {
        return database;
    }
}
