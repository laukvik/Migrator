package no.nav.migrator;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Migrator {

    private Source source;
    private Destination destination;

    private int max;
    private int rowCounter;

    private List<MigratorListener> listeners;

    public Migrator() {
        listeners = new ArrayList<>();
    }

    public void addListener(MigratorListener listener){
        this.listeners.add(listener);
    }

    public void removeListener(MigratorListener listener){
        this.listeners.remove(listener);
    }

    public void copyTable(Source source, Destination destination) throws MigrateException {
        this.source = source;
        this.destination = destination;
        this.max = 0;
        this.rowCounter = 0;
        ResultSet rsFrom = null;
        try {
            rsFrom = getFromResultSet();
        } catch (SQLException e) {
            for (MigratorListener l : listeners){
                l.failed(source.getName(), e);
            }
            throw new MigrateException(e);
        }
        ResultSet rsTo = null;
        try {
            rsTo = getToResultSet();
        } catch (SQLException e) {
            for (MigratorListener l : listeners){
                l.failed(source.getName(), e);
            }
            throw new MigrateException(e);
        }
        try {
            rsTo.moveToInsertRow();
        } catch (SQLException e) {
            for (MigratorListener l : listeners){
                l.failed(source.getName(), e);
            }
            throw new MigrateException(e);
        }
        for (MigratorListener l : listeners){
            l.starting(source.getName(), max);
        }

        try {
            iterateRows(rsFrom, rsTo);
        } catch (SQLException e) {
            for (MigratorListener l : listeners){
                l.failed(source.getName(), e);
            }
            throw new MigrateException(e);
        }
        for (MigratorListener l : listeners){
            l.finished(source.getName());
        }
    }

    void iterateRows(ResultSet sourceRs, ResultSet destinationRs) throws SQLException {
        while (sourceRs.next()) {
            copyRow(sourceRs, destinationRs);
            for (MigratorListener l : listeners){
                l.rowCopied(rowCounter, source.getName());
            }
        }
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
        for (DataType dt : source.getColumns()){
            columnIndex++;
            switch (dt){
                case String: copyString(rsFrom, rsTo, columnIndex); break;
                case Date: copyDate(rsFrom, rsTo, columnIndex); break;
                case Time: copyTime(rsFrom, rsTo, columnIndex); break;
                case Timestamp: copyTimestamp(rsFrom, rsTo, columnIndex); break;
                case Integer: copyInteger(rsFrom, rsTo, columnIndex); break;
                case Double: copyDouble(rsFrom, rsTo, columnIndex); break;
                case Float: copyFloat(rsFrom, rsTo, columnIndex); break;
                case Long: copyLong(rsFrom, rsTo, columnIndex); break;
                case Short: copyShort(rsFrom, rsTo, columnIndex); break;
                case BigDecimal: copyBigDecimal(rsFrom, rsTo, columnIndex); break;
                case Boolean: copyBoolean(rsFrom, rsTo, columnIndex); break;
                case Byte: copyByte(rsFrom, rsTo, columnIndex); break;
                case Blob: copyBlob(rsFrom, rsTo, columnIndex); break;
                case Clob: copyClob(rsFrom, rsTo, columnIndex); break;
                case Array: copyArray(rsFrom, rsTo, columnIndex); break;
                case SqlXml: copySqlXml(rsFrom, rsTo, columnIndex); break;
            }
        }
        rsTo.insertRow();
    }

    void copySqlXml(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        Object v = from.getObject(columnIndex);
        if (from.wasNull()) {
            to.updateNull(columnIndex);
        } else {
            to.updateObject(columnIndex, v);
        }
    }

    void copyArray(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        Array v = from.getArray(columnIndex);
        if (from.wasNull()) {
            to.updateNull(columnIndex);
        } else {
            to.updateArray(columnIndex, v);
        }
    }

    void copyClob(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        Clob v = from.getClob(columnIndex);
        if (from.wasNull()) {
            to.updateNull(columnIndex);
        } else {
            to.updateClob(columnIndex, v);
        }
    }

    void copyBlob(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        Blob v = from.getBlob(columnIndex);
        if (from.wasNull()) {
            to.updateNull(columnIndex);
        } else {
            to.updateBlob(columnIndex, v);
        }
    }

    void copyByte(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        byte v = from.getByte(columnIndex);
        if (from.wasNull()) {
            to.updateNull(columnIndex);
        } else {
            to.updateByte(columnIndex, v);
        }
    }

    void copyBoolean(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        boolean v = from.getBoolean(columnIndex);
        if (from.wasNull()) {
            to.updateNull(columnIndex);
        } else {
            to.updateBoolean(columnIndex, v);
        }
    }

    void copyShort(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        short v = from.getShort(columnIndex);
        if (from.wasNull()) {
            to.updateNull(columnIndex);
        } else {
            to.updateShort(columnIndex, v);
        }
    }

    void copyLong(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        long v = from.getLong(columnIndex);
        if (from.wasNull()) {
            to.updateNull(columnIndex);
        } else {
            to.updateLong(columnIndex, v);
        }
    }

    void copyFloat(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        float v = from.getFloat(columnIndex);
        if (from.wasNull()) {
            to.updateNull(columnIndex);
        } else {
            to.updateFloat(columnIndex, v);
        }
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

    void copyBigDecimal(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        BigDecimal v = from.getBigDecimal(columnIndex);
        if (from.wasNull()) {
            to.updateNull(columnIndex);
        } else {
            to.updateBigDecimal(columnIndex, v);
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

    void copyTime(ResultSet from, ResultSet to, int columnIndex) throws SQLException {
        Time v = from.getTime(columnIndex);
        if (v == null) {
            to.updateNull(columnIndex);
        } else {
            to.updateTime(columnIndex, v);
        }
    }

}
