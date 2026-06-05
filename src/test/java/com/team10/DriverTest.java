package com.team10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;

public class DriverTest {

    private DriverRepository repo;

    // clean the drivers file before each test so we start fresh
    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(DriverRepository.FILE_PATH);
        repo = new DriverRepository();
    }

    // D1 Testing

    @Test
    void D1_TC1_validDriverID_shouldPass() {
        // normal case: a correctly formatted ID should be accepted
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia");
        assertEquals("23@#abcdAB", d.getDriverID());
    }

    @Test
    void D1_TC2_tooShortID_shouldThrow() {
        // invalid: ID with only 8 characters, must be 10
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    @Test
    void D1_TC3_tooLongID_shouldThrow() {
        // invalid: ID with 12 characters, must be exactly 10
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdABXY", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    @Test
    void D1_TC4_firstCharNot2to9_shouldThrow() {
        // invalid: first digit is 1, but must be between 2 and 9
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("13@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    @Test
    void D1_TC5_lessThan2SpecialChars_shouldThrow() {
        // invalid: only 1 special character (positions 3-8 need at least 2)
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23abcdefAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    @Test
    void D1_TC6_lastTwoNotUppercase_shouldThrow() {
        // invalid: last two characters are lowercase, need uppercase
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdab", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    // D2 Testing

    @Test
    void D2_TC1_validAddress_shouldPass() {
        // normal case: correct address with 5 pipe-separated parts
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia");
        assertEquals("12|Main|Melbourne|VIC|Australia", d.getAddress());
    }

    @Test
    void D2_TC2_missingSegment_shouldThrow() {
        // invalid: only 4 parts, need exactly 5
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC"));
    }

    @Test
    void D2_TC3_wrongDelimiter_shouldThrow() {
        // invalid: uses spaces instead of pipe symbols
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12 Main Melbourne VIC Australia"));
    }

    @Test
    void D2_TC4_emptySegment_shouldThrow() {
        // edge case: empty segment between two pipes
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12||Melbourne|VIC|Australia"));
    }

    // D3 testing

    @Test
    void D3_TC1_validBirthdate_shouldPass() {
        // normal case: correct dd-MM-yyyy format
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia");
        assertEquals("10-05-1990", d.getBirthdate());
    }

    @Test
    void D3_TC2_wrongFormat_shouldThrow() {
        // invalid: yyyy/MM/dd instead of dd-MM-yyyy
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdAB", "Alice", "1990/05/10", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    @Test
    void D3_TC3_invalidDate_shouldThrow() {
        // invalid: day 32 and month 13 do not exist
        assertThrows(IllegalArgumentException.class, () ->
            new Driver("23@#abcdAB", "Alice", "32-13-2000", 5, "Light", "12|Main|Melbourne|VIC|Australia"));
    }

    // D4 testing

    @Test
    void D4_TC1_licenseUpdateAllowedWhenExperienceUnder10() {
        // normal: 8 years experience, changing from Light to Medium should work
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 8, "Light", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        boolean result = repo.update("23@#abcdAB", null, "Medium", null, null);
        assertTrue(result);
        assertEquals("Medium", repo.retrieve("23@#abcdAB").getLicenseType());
    }

    @Test
    void D4_TC2_licenseUpdateBlockedWhenExperienceOver10() {
        // invalid: 12 years experience, trying to change licence should be rejected
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1970", 12, "Light", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        boolean result = repo.update("23@#abcdAB", null, "Medium", null, null);
        assertFalse(result);
        assertEquals("Light", repo.retrieve("23@#abcdAB").getLicenseType());
    }

    @Test
    void D4_TC4_licenseUpdateAllowedAtExactly10Years() {
        // edge: exactly 10 years is allowed because rule says "more than 10"
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 10, "Light",
                            "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        boolean result = repo.update("23@#abcdAB", null, "Medium", null, null);
        assertTrue(result);
        assertEquals("Medium", repo.retrieve("23@#abcdAB").getLicenseType());
    }

    @Test
    void D4_TC5_sameLicenseUpdateAlwaysAllowed() {
        // edge: changing to the same licence is fine even with high experience
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1970", 15, "Light", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        boolean result = repo.update("23@#abcdAB", null, "Light", null, null);
        assertTrue(result);
        assertEquals("Light", repo.retrieve("23@#abcdAB").getLicenseType());
    }

    // D5 testing

    @Test
    void D5_TC1_driverIDNeverChangesDuringUpdate() {
        // verify driverID stays the same after updating other fields
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        repo.update("23@#abcdAB", 6, "Medium", "99|King|Sydney|NSW|Australia", "15-06-1992");
        Driver updated = repo.retrieve("23@#abcdAB");
        assertEquals("23@#abcdAB", updated.getDriverID());
    }

    @Test
    void D5_TC2_nameNeverChangesDuringUpdate() {
        // verify name stays the same after updating other fields
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        repo.update("23@#abcdAB", 6, "Medium", "99|King|Sydney|NSW|Australia", "15-06-1992");
        Driver updated = repo.retrieve("23@#abcdAB");
        assertEquals("Alice", updated.getName());
    }

    @Test
    void D5_TC3_nameCannotBePassedToUpdate() {
        // the update method has no 'name' parameter, so name is safe from changes
        Driver d = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Light", "12|Main|Melbourne|VIC|Australia");
        repo.add(d);
        assertTrue(repo.update("23@#abcdAB", null, null, null, null));
        assertEquals("Alice", repo.retrieve("23@#abcdAB").getName());
    }
}