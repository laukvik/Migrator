package no.nav.migrator;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MigratorTest {

    @Test
    void validateTableName_valid() {
        Migrator m = new Migrator();
        assertTrue(m.validateTableName("abcdefghijklmnopqrstuvwxyz"));
    }

    @Test
    void validateTableName_invalid() {
        Migrator m = new Migrator();
        assertThrows(IllegalArgumentException.class, () -> m.validateTableName("delete \n\rfrom;"));
    }

    @Test
    void validateTableName_too_long() {
        Migrator m = new Migrator();
        assertThrows(IllegalArgumentException.class, () -> m.validateTableName("012345678901234567890123456789012345678901234501234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567896789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"));
    }
}