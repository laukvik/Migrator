package no.nav.migrator;

public interface MigratorListener {
    void starting(String table, int max);
    void rowCopied(int rowIndex, int maxRows, String table);
    void finished(String table);
    void failed(String table, Exception e);
}
