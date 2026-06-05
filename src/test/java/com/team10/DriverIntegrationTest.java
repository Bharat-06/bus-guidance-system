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
    void DI_TC1_validDriverStoredCorrectly() {
        Driver driver = new Driver(
            "23@#abcdAB", "Alice", "10-05-1990", 5, "Light",
            "12|Main|Melbourne|VIC|Australia"
        );
        assertTrue(repo.add(driver));
    
        try {
            String content = Files.readString(DriverRepository.FILE_PATH).trim();
            // Match actual toString() order: ID,name,exp,license,address,birthdate
            String expectedLine = "23@#abcdAB,Alice,5,Light,12|Main|Melbourne|VIC|Australia,10-05-1990";
            assertTrue(content.contains(expectedLine), 
                       "The file format should exactly match the serialized driver structure.");
        } catch (IOException e) {
            fail("Could not read drivers file: " + e.getMessage());
        }
        assertEquals(1, repo.count());
    }

    // 2. Invalid driver (constructor fails) is rejected
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

    // 3. Duplicate driver ID is rejected by repository 
    @Test
    void DI_TC3_duplicateDriverRejectedByRepository() {
        Driver d1 = new Driver(
            "23@#abcdAB", "Alice", "10-05-1990", 5, "Light",
            "12|Main|Melbourne|VIC|Australia"
        );
        assertTrue(repo.add(d1));
    
        String contentAfterFirst;
        try {
            contentAfterFirst = Files.readString(DriverRepository.FILE_PATH);
        } catch (IOException e) {
            fail("Could not read drivers file: " + e.getMessage());
            return;
        }

        Driver d2 = new Driver(
            "23@#abcdAB", "Bob", "20-03-1995", 2, "Light",
            "5|High|Sydney|NSW|Australia"
        );
        boolean result = repo.add(d2);
        assertFalse(result);
        assertEquals(1, repo.count());

        try {
            String contentAfterSecond = Files.readString(DriverRepository.FILE_PATH);
            assertEquals(contentAfterFirst, contentAfterSecond);
        } catch (IOException e) {
            fail("Could not read drivers file: " + e.getMessage());
        }
    }
    // 4. Save updates to file
    void DI_TC4_updateSavedToFile() {
        Driver driver = new Driver(
            "34@#abcdCD", "Carol", "10-05-1990", 3, "Light",
            "12|Main|Melbourne|VIC|Australia"
        );
        repo.add(driver);
    
        boolean updated = repo.update("34@#abcdCD", null, "Medium",
                                      "99|King St|Brisbane|QLD|Australia", null);
        assertTrue(updated);
    
        try {
            String content = Files.readString(DriverRepository.FILE_PATH);
            assertTrue(content.contains("99|King St|Brisbane|QLD|Australia"));
            assertTrue(content.contains("Medium"));
            // Old exact record order must match toString()
            assertFalse(content.contains("34@#abcdCD,Carol,3,Light,12|Main|Melbourne|VIC|Australia,10-05-1990"),
                        "The old record state should be overwritten.");
        } catch (IOException e) {
            fail("Could not read drivers file: " + e.getMessage());
        }
    
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