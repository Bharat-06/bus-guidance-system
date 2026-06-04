package com.team10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class BusIntegrationTest {

    @BeforeEach
    void setUp() {
        new File("buses.txt").delete();
    }

    // BI-TC1: Valid bus is stored correctly in TXT file
    @Test
    void BI_TC1_validBusStoredCorrectlyInFile() throws IOException {
        Bus bus = new Bus("12345678", 40, 0.8, "Diesel");
        assertTrue(BusRepository.add(bus));

        // Read the TXT file directly and check it contains the bus
        String fileContent = Files.readString(Paths.get("buses.txt"));
        assertTrue(fileContent.contains("12345678"));
        assertTrue(fileContent.contains("Diesel"));

        // Also verify count increased
        assertEquals(1, BusRepository.count());
    }

    // BI-TC2: Invalid bus is rejected and not stored in TXT file
    @Test
    void BI_TC2_invalidBusRejectedAndNotStored() {
        // Attempt to add a bus with a non-numeric busID
        Bus bus = new Bus("1234567A", 40, 0.8, "Diesel");
        assertFalse(BusRepository.add(bus));

        // File should not exist or count must be 0
        assertEquals(0, BusRepository.count());
    }

    // BI-TC3: Bus update is persisted correctly to TXT file
    @Test
    void BI_TC3_busUpdatePersistedToFile() throws IOException {
        // Add a valid bus first
        Bus bus = new Bus("12345678", 50, 0.8, "Diesel");
        BusRepository.add(bus);

        // Decrease capacity from 50 to 30
        assertTrue(BusRepository.update("12345678", 30, 0.8, "Diesel"));

        // Read the TXT file and verify updated capacity is persisted
        String fileContent = Files.readString(Paths.get("buses.txt"));
        assertTrue(fileContent.contains("30"));

        // Also verify via retrieve
        Bus updated = BusRepository.retrieve("12345678");
        assertEquals(30, updated.getCapacity());
    }

    // BI-TC4: Bus count is updated correctly after each add
    @Test
    void BI_TC4_busCountUpdatedCorrectlyAfterAdd() {
        Bus b1 = new Bus("11111111", 40, 0.8, "Diesel");
        Bus b2 = new Bus("22222222", 30, 0.6, "Hybrid");
        Bus b3 = new Bus("33333333", 20, 0.9, "Electricity");

        BusRepository.add(b1);
        assertEquals(1, BusRepository.count());

        BusRepository.add(b2);
        assertEquals(2, BusRepository.count());

        BusRepository.add(b3);
        assertEquals(3, BusRepository.count());
    }
}
