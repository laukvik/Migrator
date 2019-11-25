package no.laukvik.migrator;

public interface MigratorListener {
    void rowCopied(int rowIndex, String table);
}
