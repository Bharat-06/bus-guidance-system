package com.team10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class DriverIntegrationTest {

    private DriverRepository repo;

 

    // DI-TC1: Valid driver is stored correctly in TXT file
    @Test
    void DI_TC1_validDriverStoredCorrectlyInFile() throws IOException {
        Driver driver = new Driver(
                "23@#abcdAB", "Alice", 3, "Light",
                "12|Main|Melbourne|VIC|Australia", "10-05-1990"
        );
        repo.add(driver);

        String fileContent = Files.readString(Paths.get("drivers.txt"));
        assertTrue(fileContent.contains("23@#abcdAB"));
        assertTrue(fileContent.contains("Alice"));
        assertEquals(1, repo.count());
    }

    // DI-TC2: Invalid driver is rejected and not stored in TXT file
    @Test
    void DI_TC2_invalidDriverRejectedAndNotStored() {
        assertThrows(IllegalArgumentException.class, () -> {
            Driver driver = new Driver(
                    "INVALIDID!", "Bob", 2, "Light",
                    "5|High|Sydney|NSW|Australia", "20-03-1995"
            );
            repo.add(driver);
        });
        assertEquals(0, repo.count());
    }

    // DI-TC3: Driver update is persisted correctly to TXT file
    @Test
    void DI_TC3_driverUpdatePersistedToFile() throws IOException {
        Driver driver = new Driver(
                "34@#abcdCD", "Carol", 3, "Light",
                "12|Main|Melbourne|VIC|Australia", "10-05-1990"
        );
        repo.add(driver);

        repo.update("34@#abcdCD", null, null, "99|King St|Brisbane|QLD|Australia", null);

        String fileContent = Files.readString(Paths.get("drivers.txt"));
        assertTrue(fileContent.contains("99|King St|Brisbane|QLD|Australia"));

        Driver updated = repo.retrieve("34@#abcdCD");
        assertEquals("99|King St|Brisbane|QLD|Australia", updated.getAddress());
    }

    // DI-TC4: Driver count is updated correctly after each add
    @Test
    void DI_TC4_driverCountUpdatedCorrectlyAfterAdd() {
        Driver d1 = new Driver(
                "45@#abcdEF", "Alice", 3, "Light",
                "12|Main|Melbourne|VIC|Australia", "10-05-1990"
        );
        Driver d2 = new Driver(
                "56@#abcdGH", "Bob", 5, "Medium",
                "5|High|Sydney|NSW|Australia", "20-03-1995"
        );
        Driver d3 = new Driver(
                "67@#abcdIJ", "Carol", 7, "Heavy",
                "8|Low|Perth|WA|Australia", "01-01-1988"
        );

        repo.add(d1);
        assertEquals(1, repo.count());

        repo.add(d2);
        assertEquals(2, repo.count());

        repo.add(d3);
        assertEquals(3, repo.count());
    }
}