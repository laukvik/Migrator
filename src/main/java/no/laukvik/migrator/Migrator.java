package no.laukvik.migrator;

import java.sql.*;
import java.util.logging.Logger;

public class Migrator {

    private Logger LOG = Logger.getLogger(Migrator.class.getName());

    private Source source;
    private Destination destination;

    private int max;
    private int rowCounter;
    private int logEvery = 10;

    public Migrator() {
    }

    public void copyTable(Source source, Destination destination, int logEvery) throws SQLException {
        this.source = source;
        this.destination = destination;
        this.max = 0;
        this.rowCounter = 0;
        this.logEvery = logEvery;
        LOG.info("Logging every " + logEvery + " row.");
        ResultSet rsFrom = getFromResultSet();
        LOG.info("From table ok.");
        ResultSet rsTo = getToResultSet();
        LOG.info("Destination table ok.");
        rsTo.moveToInsertRow();
        LOG.info("Start copying " + max + " rows...");
        while (rsFrom.next()){
            copyRow(rsFrom, rsTo);
        }
        LOG.info("Finished copying rows...");
    }

    int getRowCount(Statement stmt, String table) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + table);
        rs.next();
        return rs.getInt(1);
    }

    ResultSet getFromResultSet() throws SQLException {
        Connection conn = source.getDatabase().getConnection();
        Statement stmt = conn.createStatement();
        max = getRowCount(stmt, source.getName());
        return stmt.executeQuery("SELECT * FROM " + source.getName());
    }

    ResultSet getToResultSet() throws SQLException {
        Connection conn = destination.getDatabase().getConnection();
        Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        return st.executeQuery("SELECT * FROM " + destination.getName());
    }

    void copyRow(ResultSet rsFrom, ResultSet rsTo) throws SQLException {
        int columnIndex = 0;
        rowCounter++;
        if (rowCounter % logEvery == 0){
            LOG.info("Copying row " + rowCounter + "/" + max);
        }
        for (DataType dt : source.getColumns()){
            columnIndex++;
            switch (dt){
                case String: copyString(rsFrom, rsTo, columnIndex); break;
                case Date: copyDate(rsFrom, rsTo, columnIndex); break;
                case Timestamp: copyTimestamp(rsFrom, rsTo, columnIndex); break;
                case Integer: copyInteger(rsFrom, rsTo, columnIndex); break;
                case Double: copyDouble(rsFrom, rsTo, columnIndex); break;
            }
        }
        rsTo.insertRow();
    }

    void copyDouble(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        double v = from.getDouble(columnIndex);
        if (from.wasNull()) {
            to.updateNull(columnIndex);
        } else {
            to.updateDouble(columnIndex, v);
        }
    }

    void copyInteger(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        int v = from.getInt(columnIndex);
        if (from.wasNull()) {
            to.updateNull(columnIndex);
        } else {
            to.updateInt(columnIndex, v);
        }
    }

    void copyString(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        String v = from.getString(columnIndex);
        if (v == null) {
            to.updateNull(columnIndex);
        } else {
            to.updateString(columnIndex, v);
        }
    }

    void copyDate(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        Date v = from.getDate(columnIndex);
        if (v == null) {
            to.updateNull(columnIndex);
        } else {
            to.updateDate(columnIndex, v);
        }
    }

    void copyTimestamp(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        Timestamp v = from.getTimestamp(columnIndex);
        if (v == null) {
            to.updateNull(columnIndex);
        } else {
            to.updateTimestamp(columnIndex, v);
        }
    }

}
