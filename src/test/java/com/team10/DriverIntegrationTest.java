package com.team10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;

public class DriverIntegrationTest {

    private DriverRepository repo;

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(DriverRepository.FILE_PATH);
        repo = new DriverRepository();
    }

    // 1. Valid driver is stored correctly
    @Test
    void DI_TC1_validDriverStoredCorrectly() throws IOException {
        Driver driver = new Driver(
            "23@#abcdAB", "Alice", "10-05-1990", 5, "Light",
            "12|Main|Melbourne|VIC|Australia"
        );
        assertTrue(repo.add(driver));

        String content = Files.readString(DriverRepository.FILE_PATH);
        assertTrue(content.contains("23@#abcdAB"));
        assertTrue(content.contains("Alice"));
        assertTrue(content.contains("5"));
        assertTrue(content.contains("Light"));
        assertTrue(content.contains("12|Main|Melbourne|VIC|Australia"));
        assertTrue(content.contains("10-05-1990"));
        assertEquals(1, repo.count());
    }

    // 2. Invalid driver (constructor fails) is rejected – never reaches repo
    @Test
    void DI_TC2_invalidDriverRejectedByConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver(
                "INVALID", "Bob", "20-03-1995", 2, "Light",
                "5|High|Sydney|NSW|Australia"
            );
        });
        assertEquals(0, repo.count());
        assertFalse(Files.exists(DriverRepository.FILE_PATH));
    }

    // 3. Duplicate driver ID is rejected by repository (D1) – file unchanged
    @Test
    void DI_TC3_duplicateDriverRejectedByRepository() throws IOException {
        Driver d1 = new Driver(
            "23@#abcdAB", "Alice", "10-05-1990", 5, "Light",
            "12|Main|Melbourne|VIC|Australia"
        );
        assertTrue(repo.add(d1));

        // Read file content after first add
        String contentAfterFirst = Files.readString(DriverRepository.FILE_PATH);

        // Try to add another driver with same ID
        Driver d2 = new Driver(
            "23@#abcdAB", "Bob", "20-03-1995", 2, "Light",
            "5|High|Sydney|NSW|Australia"
        );
        boolean result = repo.add(d2);
        assertFalse(result);
        assertEquals(1, repo.count());

        // File content must be unchanged
        String contentAfterSecond = Files.readString(DriverRepository.FILE_PATH);
        assertEquals(contentAfterFirst, contentAfterSecond);
    }

    // 4. Update is persisted correctly (old values removed, new values written)
    @Test
    void DI_TC4_updatePersistedToFile() throws IOException {
        Driver driver = new Driver(
            "34@#abcdCD", "Carol", "10-05-1990", 3, "Light",
            "12|Main|Melbourne|VIC|Australia"
        );
        repo.add(driver);

        boolean updated = repo.update("34@#abcdCD", null, "Medium",
                                      "99|King St|Brisbane|QLD|Australia", null);
        assertTrue(updated);

        String content = Files.readString(DriverRepository.FILE_PATH);
        assertTrue(content.contains("99|King St|Brisbane|QLD|Australia"));
        assertTrue(content.contains("Medium"));
        assertFalse(content.contains("12|Main|Melbourne|VIC|Australia"));
        assertFalse(content.contains("Light"));

        Driver retrieved = repo.retrieve("34@#abcdCD");
        assertEquals("99|King St|Brisbane|QLD|Australia", retrieved.getAddress());
        assertEquals("Medium", retrieved.getLicenseType());
    }

    // 5. Record count is updated correctly after each add
    @Test
    void DI_TC5_countUpdatedCorrectly() {
        Driver d1 = new Driver(
            "45@#abcdEF", "Alice", "10-05-1990", 3, "Light",
            "12|Main|Melbourne|VIC|Australia"
        );
        Driver d2 = new Driver(
            "56@#abcdGH", "Bob", "20-03-1995", 5, "Medium",
            "5|High|Sydney|NSW|Australia"
        );
        Driver d3 = new Driver(
            "67@#abcdIJ", "Carol", "01-01-1988", 7, "Heavy",
            "8|Low|Perth|WA|Australia"
        );

        repo.add(d1);
        assertEquals(1, repo.count());

        repo.add(d2);
        assertEquals(2, repo.count());

        repo.add(d3);
        assertEquals(3, repo.count());
    }
}