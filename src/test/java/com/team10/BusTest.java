package com.team10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class BusTest {

    private BusRepository repo;

    @BeforeEach
    void setUp() {
        new File("buses.txt").delete();
        repo = new BusRepository();
    }


    @Test
    void B1_TC1_allParametersValid() {
        // Exactly 8 numeric digits - should be accepted
        System.out.println("Test: B1 TC1");
        assertDoesNotThrow(() -> new Bus("12345678", 40, 300.0, "Diesel", null));
        
    }

    @Test
    void BI_TC2_invalidBusNotStored() {
        // invalid bus ID should not be stored
        System.out.println("Test: B1 TC2");
        assertThrows(IllegalArgumentException.class, () -> {
            Bus bus = new Bus("1234567A", 40, 0.8, "Diesel", null);
            repo.add(bus);
        });
        assertEquals(0, repo.count());
    }

    @Test
    void B1_TC3_busIDTooShort() {
        // checks if the bus ID is 8 digits, in this test case it is 7
        System.out.println("Test: B1 TC3");
        assertThrows(IllegalArgumentException.class, () -> {
            new Bus("1234567", 40, 0.8, "Diesel", null);
        });
        
    }

    @Test
    void B1_TC4_busIDTooLong() {
        // checks if the bus ID is 8 digits, in this test case it is 9
        System.out.println("Test: B1 TC4");
        assertThrows(IllegalArgumentException.class, () -> {
            new Bus("123456789", 40, 0.8, "Diesel", null);
        });
    }

    @Test
    void B1_TC5_duplicateBusID() {
        // First add succeeds, second add with same ID must fail. In this test case both buses share "12345678" as the same ID
        System.out.println("Test: B1 TC5");
        Bus bus1 = new Bus("12345678", 40, 0.8, "Diesel", null);
        Bus bus2 = new Bus("12345678", 50, 0.5, "Diesel", null);

        assertTrue(repo.add(bus1));
        assertFalse(repo.add(bus2));

    }

    @Test
    void B2_TC1_capacityDecrease() {
        // Reducing capacity from 50 to 40 should succeed
        System.out.println("Test: B2 TC1");
        Bus bus = new Bus("11111111", 50, 0.8, "Diesel", null);
        repo.add(bus);
        assertTrue(repo.update("11111111", 40, 0.8, "Diesel"));
        assertEquals(40, repo.retrieve("11111111").getCapacity());
    }

    @Test
    void B2_TC2_capacityIncrease() {
        // Increasing capacity from 40 to 50 should fail
        System.out.println("Test: B2 TC2");
        Bus bus = new Bus("22222222", 40, 0.8, "Diesel", null);
        repo.add(bus);
        assertFalse(repo.update("22222222", 50, 0.8, "Diesel"));
    }

    @Test
    void B2_TC3_capacityUnchanged() {
        // Same capacity should succeed, in this case 40 is updated 40
        System.out.println("Test: B2 TC3");
        Bus bus = new Bus("33333333", 40, 0.8, "Diesel", null);
        repo.add(bus);
        assertTrue(repo.update("33333333", 40, 0.8, "Diesel"));
    }

    @Test
    void B2_TC4_capacityBelowMin() {
        // capacity of 5 should fail, as the minimum is 10
        System.out.println("Test: B2 TC4");
        assertThrows(IllegalArgumentException.class, () -> {
             new Bus("12345678", 5, 0.8, "Diesel", null);
             });
    }

    @Test
    void B2_TC5_capacityAboveMax() {
        // capacity of 100 should fail, as the maximum is 80
        System.out.println("Test: B2 TC5");
        assertThrows(IllegalArgumentException.class, () -> {
             new Bus("12345678", 100, 0.8, "Diesel", null);
             });
    }

    @Test
    void B2_TC6_tenCapacity() {
        // capacity of 10 should pass as it is the minimum
        System.out.println("Test: B2 T6");
        assertDoesNotThrow(() -> new Bus("12345678", 10, 0, "Diesel", null));
    }

    @Test
    void B2_TC7_eightyCapacity() {
        // capacity of 80 should pass as it is the maximum
        System.out.println("Test: B2 T7");
        assertDoesNotThrow(() -> new Bus("12345678", 80, 0, "Diesel", null));
    }


    @Test
    void B3_TC1_negativeFuelLevel() {
        // Negative fuel level should be rejected, in this case -1.0
        System.out.println("Test: B3 TC1");
         assertThrows(IllegalArgumentException.class, () -> {
            new Bus("12345678", 40, -1.0, "Diesel", null);
            });
    }

    @Test
    void B3_TC2_zeroFuelLevel() {
        // Fuel level of 0 should pass
        System.out.println("Test: B3 TC2");
        assertDoesNotThrow(() -> new Bus("12345678", 40, 0, "Diesel", null));
    }

    @Test
    void B3_TC3_fuelLevelMax() {
        // Fuel level of 600 should be accepted
        System.out.println("Test: B3 TC3");
        assertDoesNotThrow(() -> new Bus("12345678", 40, 600.0, "Diesel", null));
    }

    @Test
    void B3_TC4_overMaxFuelLevel() {
        // Fuel level above max 600 should be rejected, in this case 601
        System.out.println("Test: B3 TC4");
         assertThrows(IllegalArgumentException.class, () -> {
            new Bus("12345678", 40, 601.0, "Diesel", null);
            });
    }

    // Fuel Type Validation Tests

    @Test
    void B4_TC1_invalidFuelType() {
        // entering water as a fuel type should fail
        System.out.println("Test: B4 TC1");
        assertThrows(IllegalArgumentException.class, () -> {
             new Bus("12345678", 40, 0.8, "Water", null);
             });
    }


    
}
