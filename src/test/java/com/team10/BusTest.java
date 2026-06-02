package com.team10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class BusTest {

    
    // Clear the buses.txt file before each test so
    // duplicate-ID checks always start from a clean state.
    

    @BeforeEach
    void clearBusFile() {
        new File("buses.txt").delete();
    }

    
    // B1: Bus ID Validation Tests
    

    @Test
    void B1_TC1_valid8DigitNumericBusIDShouldPass() {
        // Exactly 8 numeric digits — should be accepted
        Bus bus = new Bus("12345678", 40, 0.8, "Diesel");
        assertTrue(BusRepository.add(bus));
    }

    @Test
    void B1_TC2_duplicateBusIDShouldBeRejected() {
        // First add succeeds; second add with the same ID must fail
        Bus bus1 = new Bus("12345678", 40, 0.8, "Diesel");
        Bus bus2 = new Bus("12345678", 50, 0.5, "Diesel");

        assertTrue(BusRepository.add(bus1));
        assertFalse(BusRepository.add(bus2));
    }

    @Test
    void B1_TC3_busIDWithLettersShouldFail() {
        // Contains a letter — validBusID must return false
        assertFalse(Bus.validBusID("1234567A"));
    }

    @Test
    void B1_TC4_busIDTooShortShouldFail() {
        // 7 digits — must be exactly 8
        assertFalse(Bus.validBusID("1234567"));
    }

    @Test
    void B1_TC5_busIDTooLongShouldFail() {
        // 9 digits — must be exactly 8
        assertFalse(Bus.validBusID("123456789"));
    }

    
    // B2: Capacity Update Restriction Tests
    // Uses Bus.capacityUpdate(oldCapacity, newCapacity)
    // and BusRepository.update() to verify end-to-end.
    

    @Test
    void B2_TC1_capacityDecreaseAllowed() {
        // Reducing from 50 to 40 — capacityUpdate must return true
        assertTrue(Bus.capacityUpdate(50, 40));

        // Also verify through the repository that the change persists
        Bus bus = new Bus("11111111", 50, 0.8, "Diesel");
        BusRepository.add(bus);
        assertTrue(BusRepository.update("11111111", 40, 0.8, "Diesel"));
        assertEquals(40, BusRepository.retrieve("11111111").getCapacity());
    }

    @Test
    void B2_TC2_capacityIncreaseShouldBeRejected() {
        // Increasing from 40 to 50 — capacityUpdate must return false
        assertFalse(Bus.capacityUpdate(40, 50));

        // Also verify through the repository that the update is rejected
        Bus bus = new Bus("22222222", 40, 0.8, "Diesel");
        BusRepository.add(bus);
        assertFalse(BusRepository.update("22222222", 50, 0.8, "Diesel"));
    }

    @Test
    void B2_TC3_capacityUnchangedShouldBeAllowed() {
        // Same value — capacityUpdate must return true (newCapacity <= oldCapacity)
        assertTrue(Bus.capacityUpdate(40, 40));

        // Also verify through the repository
        Bus bus = new Bus("33333333", 40, 0.8, "Diesel");
        BusRepository.add(bus);
        assertTrue(BusRepository.update("33333333", 40, 0.8, "Diesel"));
    }

    
    // B3: Driver Age Restriction Tests
    // Uses Bus.isDriverAgeValidForBus(birthdate, capacity)
    // Rule: drivers over 50 cannot operate buses with capacity >= 50.
    

    @Test
    void B3_TC1_driverOver50RejectedForLargeBus() {
        // Age 52, capacity 55 — must return false
        // Birthdate producing age 52: born 1973
        assertFalse(Bus.isDriverAgeValidForBus("15-06-1973", 55));
    }

    @Test
    void B3_TC2_driverOver50AllowedForSmallBus() {
        // Age 52, capacity 40 — under the 50-capacity threshold, must return true
        assertTrue(Bus.isDriverAgeValidForBus("15-06-1973", 40));
    }

    @Test
    void B3_TC3_driverExactly50AllowedForLargeBus() {
        // Age exactly 50 — restriction is strictly "over 50", must return true
        // Birthdate producing age 50: born 1975-06-15 (today is 2025-06-02, so not yet had birthday this year)
        // Use 02-06-1975 to ensure the person has already turned 50 in 2025
        assertTrue(Bus.isDriverAgeValidForBus("02-06-1976", 60));
    }

    
    // B4: Electric Bus Experience Restriction Tests
    // Uses Bus.isDriverExperienceValidForElectric(experienceYears, fuelType)
    // Rule: driver must have >= 5 years experience for Electricity buses.
    

    @Test
    void B4_TC1_driverWithLessThan5YearsRejectedForElectricBus() {
        // 3 years experience, Electricity — must return false
        assertFalse(Bus.isDriverExperienceValidForElectric(3, "Electricity"));
    }

    @Test
    void B4_TC2_driverWithExactly5YearsAllowedForElectricBus() {
        // Exactly 5 years, Electricity — meets the minimum, must return true
        assertTrue(Bus.isDriverExperienceValidForElectric(5, "Electricity"));
    }

    @Test
    void B4_TC3_driverWithMoreThan5YearsAllowedForElectricBus() {
        // 8 years, Electricity — above the threshold, must return true
        assertTrue(Bus.isDriverExperienceValidForElectric(8, "Electricity"));
    }

    
    // B5: Driver Licence Restriction Tests
    // Uses Bus.isLicenseValidForBus(licenseType, fuelType)
    // Rule: Electricity and Hybrid buses require Heavy or PublicTransport licence.
    

    @Test
    void B5_TC1_lightLicenceRejectedForElectricBus() {
        // Light licence, Electricity — must return false
        assertFalse(Bus.isLicenseValidForBus("Light", "Electricity"));
    }

    @Test
    void B5_TC2_mediumLicenceRejectedForHybridBus() {
        // Medium licence, Hybrid — must return false
        assertFalse(Bus.isLicenseValidForBus("Medium", "Hybrid"));
    }

    @Test
    void B5_TC3_heavyLicenceAllowedForElectricBus() {
        // Heavy licence, Electricity — must return true
        assertTrue(Bus.isLicenseValidForBus("Heavy", "Electricity"));
    }

    @Test
    void B5_TC4_publicTransportLicenceAllowedForHybridBus() {
        // PublicTransport licence, Hybrid — must return true
        assertTrue(Bus.isLicenseValidForBus("PublicTransport", "Hybrid"));
    }
}