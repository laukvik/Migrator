package no.laukvik.migrator;

import java.sql.*;
import java.util.logging.Logger;

public class Migrator {

    private Logger LOG = Logger.getLogger(Migrator.class.getName());

    private Table from;
    private Table to;

    private int max;
    private int rowCounter;

    public Migrator() {
    }

    public void copyTable(Table from, Table to) throws SQLException {
        this.from = from;
        this.to = to;
        ResultSet rsFrom = getFromResultSet();
        ResultSet rsTo = getToResultSet();
        rsTo.moveToInsertRow();
        while (rsFrom.next()){
            copyRow(rsFrom, rsTo);
        }
    }

    int getRowCount(Statement stmt, String table) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + table);
        rs.next();
        return rs.getInt(1);
    }

    ResultSet getFromResultSet() throws SQLException {
        Connection conn = from.getDatabase().getConnection();
        Statement stmt = conn.createStatement();
        max = getRowCount(stmt, from.getName());
        return stmt.executeQuery("SELECT * FROM " + from.getName());
    }

    ResultSet getToResultSet() throws SQLException {
        Connection conn = to.getDatabase().getConnection();
        Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        return st.executeQuery("SELECT * FROM " + to.getName());
    }

    void copyRow(ResultSet rsFrom, ResultSet rsTo) throws SQLException {
        int columnIndex = 0;
        rowCounter++;
        LOG.info("Copying row " + rowCounter + "/" + max);
        for (DataType dt : from.getColumns()){
            columnIndex++;
            switch (dt){
                case String: copyString(rsFrom, rsTo, columnIndex); break;
                case Date: copyDate(rsFrom, rsTo, columnIndex); break;
                case Timestamp: copyTimestamp(rsFrom, rsTo, columnIndex); break;
            }
        }
        rsTo.insertRow();
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
