package com.team10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;

public class DriverTest {

    private DriverRepository repo;

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(DriverRepository.FILE_PATH);
        repo = new DriverRepository();
    }

    // ==================== D1: Driver ID Rules ====================

    @Test
    void D1_TC1_validDriverID_shouldPass() {
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia");
        assertEquals("23@#abcdAB", d.getDriverID());
    }

    @Test
    void D1_TC2_tooShortID_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    @Test
    void D1_TC3_tooLongID_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdABXY", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    @Test
    void D1_TC4_firstCharNot2to9_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("13@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    @Test
    void D1_TC5_lessThan2SpecialChars_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23abcdefAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    @Test
    void D1_TC6_lastTwoNotUppercase_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdab", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    // D2: Address Format 

    @Test
    void D2_TC1_validAddress_shouldPass() {
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia");
        assertEquals("12|Main|Melbourne|VIC|Australia", d.getAddress());
    }

    @Test
    void D2_TC2_missingSegment_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC"));
    }

    @Test
    void D2_TC3_wrongDelimiter_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12 Main Melbourne VIC Australia"));
    }

    @Test
    void D2_TC4_emptySegment_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12||Melbourne|VIC|Australia"));
    }

    // ==================== D3: Birthdate Format ====================

    @Test
    void D3_TC1_validBirthdate_shouldPass() {
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia");
        assertEquals("10-05-1990", d.getBirthdate());
    }

    @Test
    void D3_TC2_wrongFormat_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdAB", "Alice", "1990/05/10", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    @Test
    void D3_TC3_invalidDate_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdAB", "Alice", "32-13-2000", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    @Test
    void D3_TC4_feb29NonLeapYear_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdAB", "Alice", "29-02-2001", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    // ==================== D4: License Update Restriction ====================
    // Rule: If experience > 10 years, license type cannot be changed.
    // Test via DriverRepository.update()

    @Test
    void D4_TC1_licenseUpdateAllowedWhenExperienceUnder10() {
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 8, "Light", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        boolean result = repo.update("23@#abcdAB", null, "Medium", null, null);
        assertTrue(result);
        assertEquals("Medium", repo.retrieve("23@#abcdAB").getLicenseType());
    }

    @Test
    void D4_TC2_licenseUpdateBlockedWhenExperienceOver10() {
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1970", 12, "Light", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        boolean result = repo.update("23@#abcdAB", null, "Medium", null, null);
        assertFalse(result);
        assertEquals("Light", repo.retrieve("23@#abcdAB").getLicenseType());
    }

    @Test
    void D4_TC3_licenseUpdateBlockedAtExactly11Years() {
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1970", 11, "Heavy", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        boolean result = repo.update("23@#abcdAB", null, "Light", null, null);
        assertFalse(result);
        assertEquals("Heavy", repo.retrieve("23@#abcdAB").getLicenseType());
    }

    @Test
    void D4_TC4_sameLicenseUpdateAlwaysAllowed() {
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1970", 15, "Light", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        boolean result = repo.update("23@#abcdAB", null, "Light", null, null);
        assertTrue(result);
        assertEquals("Light", repo.retrieve("23@#abcdAB").getLicenseType());
    }

    // ==================== D5: Immutable Fields (ID and Name) ====================
    // DriverRepository.update() never accepts driverID or name parameters.
    // We test that these fields remain unchanged after any update.

    @Test
    void D5_TC1_driverIDNeverChangesDuringUpdate() {
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        repo.update("23@#abcdAB", 6, "Medium", "99|King|Sydney|NSW|Australia", "15-06-1992");
        Driver updated = repo.retrieve("23@#abcdAB");
        assertEquals("23@#abcdAB", updated.getDriverID());
    }

    @Test
    void D5_TC2_nameNeverChangesDuringUpdate() {
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        repo.update("23@#abcdAB", 6, "Medium", "99|King|Sydney|NSW|Australia", "15-06-1992");
        Driver updated = repo.retrieve("23@#abcdAB");
        assertEquals("Alice", updated.getName());
    }

    @Test
    void D5_TC3_nameCannotBePassedToUpdate() {
        // The update method signature has no 'name' parameter, so it's impossible to change.
        // This test is a compile-time guarantee demonstration.
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        // If there were a name parameter, we would try to pass it, but there isn't.
        assertTrue(repo.update("23@#abcdAB", null, null, null, null));
        assertEquals("Alice", repo.retrieve("23@#abcdAB").getName());
    }
}