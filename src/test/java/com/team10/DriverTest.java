package com.team10;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DriverTest {

    
    // D1: Driver ID Tests
    
    @Test
    void D1_TC1_validDriverIDShouldPass() {
        // Valid: 10 chars, first 2 digits in 2-9, at least 2 special chars in pos 3-8, last 2 uppercase
        assertTrue(Driver.isValidDriverID("23@#abcdAB"));
    }
  

    @Test
    void D1_TC2_duplicateDriverIDShouldBeRejected() {
        try {
            Files.deleteIfExists(DriverRepository.FILE_PATH);
            
        } catch (Exception e) {
            System.out.println(e);
        }

        DriverRepository repo = new DriverRepository();

        Driver d1 = new Driver(
                "23@#abcdAB",
                "Alice",
                3,
                "Light",
                "12|Main|Melbourne|VIC|Australia",
                "10-05-1990"
        );

        Driver d2 = new Driver(
                "23@#abcdAB",
                "Bob",
                2,
                "Light",
                "5|High|Sydney|NSW|Australia",
                "20-03-1995"
        );

        

        assertTrue(repo.add(d1));
        assertFalse(repo.add(d2));
            
            
    }
   
    @Test
    void D1_TC3_idTooShortShouldFail() {
        // 8 chars — must be exactly 10
        assertFalse(Driver.isValidDriverID("23@#abAB"));
    }

    @Test
    void D1_TC4_idTooLongShouldFail() {
        // 12 chars — must be exactly 10
        assertFalse(Driver.isValidDriverID("23@#abcdABXY"));
    }

    @Test
    void D1_TC5_firstCharNotInRange2to9ShouldFail() {
        // First digit is 1, which is outside 2-9
        assertFalse(Driver.isValidDriverID("13@#abcdAB"));
    }

    @Test
    void D1_TC6_fewerThan2SpecialCharsInPositions3to8ShouldFail() {
        // No special chars in positions 3-8
        assertFalse(Driver.isValidDriverID("23abcdefAB"));
    }

    
    // D2: Address Format Tests
     
    @Test
    void D2_TC1_validAddressShouldPass() {
        assertTrue(Driver.isValidAddress("12|Main St|Sydney|NSW|Australia"));
    }

    @Test
    void D2_TC2_addressMissingSegmentShouldFail() {
        // Only 4 pipe-separated segments instead of 5
        assertFalse(Driver.isValidAddress("12|Main St|Sydney|NSW"));
    }

    @Test
    void D2_TC3_addressWithWrongDelimiterShouldFail() {
        // Uses spaces instead of pipe characters
        assertFalse(Driver.isValidAddress("12 Main St Sydney NSW Australia"));
    }

    
    // D3: Birthdate Format Tests
    
    @Test
    void D3_TC1_validBirthdateShouldPass() {
        assertTrue(Driver.isValidBirthdate("15-06-1990"));
    }

    @Test
    void D3_TC2_wrongDateFormatShouldFail() {
        // Format is yyyy/MM/dd, not dd-MM-yyyy
        assertFalse(Driver.isValidBirthdate("1990/06/15"));
    }

    @Test
    void D3_TC3_invalidDateValueShouldFail() {
        // Day 32 and month 13 do not exist
        assertFalse(Driver.isValidBirthdate("32-13-2000"));
    }

    
    // D4: License Update Restriction Tests
    
    @Test
    void D4_TC1_licenseUpdateAllowedUnder10YearsExperience() {
        // 8 years experience — license change from Light to Medium should succeed
        Driver d = new Driver("23@#abcdAB", "Alice", 8, "Light",
                "12|Main|Melbourne|VIC|Australia", "10-05-1990");

        assertDoesNotThrow(() -> d.update(null, null, null, "Medium", null, null));
    }

    @Test
    void D4_TC2_licenseUpdateBlockedOver10YearsExperience() {
        // 11 years experience — changing license type should be rejected
        Driver d = new Driver("23@#abcdAB", "Alice", 11, "Light",
                "12|Main|Melbourne|VIC|Australia", "10-05-1990");
//false
        assertThrows(IllegalArgumentException.class,
                () -> d.update(null, null, null, "Medium", null, null));
    }

    @Test
    void D4_TC3_licenseUpdateBlockedAtExactly11YearsExperience() {
        // Exactly 11 years — still over 10, so change should be rejected
        Driver d = new Driver("23@#abcdAB", "Alice", 11, "Heavy",
                "12|Main|Melbourne|VIC|Australia", "10-05-1990");

        assertThrows(IllegalArgumentException.class,
                () -> d.update(null, null, null, "Light", null, null));
    }

    
    // D5: Immutable Fields Tests
    
    @Test
    void D5_TC1_driverIDCannotBeUpdated() {
        Driver d = new Driver("23@#abcdAB", "Alice", 5, "Light",
                "12|Main|Melbourne|VIC|Australia", "10-05-1990");

        assertThrows(IllegalArgumentException.class,
                () -> d.update("99@#wxyzXY", null, null, null, null, null));
    }

    @Test
    void D5_TC2_driverNameCannotBeUpdated() {
        Driver d = new Driver("23@#abcdAB", "Alice", 5, "Light",
                "12|Main|Melbourne|VIC|Australia", "10-05-1990");

        assertThrows(IllegalArgumentException.class,
                () -> d.update(null, "Bob", null, null, null, null));
    }

    @Test
    void D5_TC3_otherFieldsCanBeUpdatedNormally() {
        // Address and licenseType updates should succeed for a driver with 5 years experience
        Driver d = new Driver("23@#abcdAB", "Alice", 5, "Light",
                "12|Main|Melbourne|VIC|Australia", "10-05-1990");
// assert true
        assertDoesNotThrow(() ->
                d.update(null, null, null, "Medium",
                        "7|King St|Brisbane|QLD|Australia", null));
    }
}