package no.laukvik.migrator;

import java.sql.SQLException;

public class MigrateException extends Throwable {

    public MigrateException(SQLException e) {
        super(e);
    }
}
