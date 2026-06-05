package com.team10;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;
public class BusIntegrationTest {
    private BusRepository repo;
    @BeforeEach
    void setUp() {
        new File("buses.txt").delete();
        repo = new BusRepository();
    }
    @Test
    void BI_TC1_validBusStored() {
        // Valid bus is stored correctly in TXT file
        Bus bus = new Bus("12345678", 40, 0.8, "Diesel", null);
        assertTrue(repo.add(bus));

        String fileContent;

        try {
            fileContent = Files.readString(Paths.get("buses.txt"));
        } 
        catch (IOException e) {
            fail("Failed to read buses.txt: " + e.getMessage());
            return;
        }

        assertTrue(fileContent.contains("12345678"));
        assertEquals(1, repo.count());
}
    
    @Test
    void BI_TC2_invalidBusRejected() {
        // Invalid bus is rejected and not stored in TXT file
        assertThrows(IllegalArgumentException.class, () -> {
            new Bus("1234567A", 40, 0.8, "Diesel", null);
        });
        assertEquals(0, repo.count());
    }

    @Test
    void BI_TC3_busUpdatePersistedToFile() {
        // Bus update is persisted correctly to TXT file
        Bus bus = new Bus("12345678", 50, 0.8, "Diesel", null);
        repo.add(bus);
        

        assertTrue(repo.update("12345678", 30, 0.8, "Diesel"));

        String fileContent = assertDoesNotThrow(() ->
            Files.readString(Paths.get("buses.txt"))
        );

        assertTrue(fileContent.contains("30"));

        Bus updated = repo.retrieve("12345678");
        assertEquals(30, updated.getCapacity());
    }

    @Test
    void BI_TC4_busCountUpdatedCorrectlyAfterAdd() {
        // Bus count is updated correctly after each add
        Bus b1 = new Bus("11111111", 40, 0.8, "Diesel", null);
        Bus b2 = new Bus("22222222", 30, 0.6, "Hybrid", null);
        Bus b3 = new Bus("33333333", 20, 0.9, "Electricity", null);
        repo.add(b1);
        assertEquals(1, repo.count());
        repo.add(b2);
        assertEquals(2, repo.count());
        repo.add(b3);
        assertEquals(3, repo.count());
    }
}
