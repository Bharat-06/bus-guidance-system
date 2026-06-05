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

    // Exactly 8 numeric digits should be accepted
    @Test
    void B1_TC1_allParametersValid() {
        System.out.println("Test: B1 TC1");
        assertDoesNotThrow(() -> new Bus("12345678", 40, 300.0, "Diesel", null));
        
    }

    // invalid bus ID should not be stored
    @Test
    void BI_TC2_invalidBusNotStored() {
        System.out.println("Test: B1 TC2");
        assertThrows(IllegalArgumentException.class, () -> {
            Bus bus = new Bus("1234567A", 40, 0.8, "Diesel", null);
            repo.add(bus);
        });
        assertEquals(0, repo.count());
    }

    // checks if the bus ID is 8 digits, in this test case it is 7
    @Test
    void B1_TC3_busIDTooShort() {
        System.out.println("Test: B1 TC3");
        assertThrows(IllegalArgumentException.class, () -> {
            new Bus("1234567", 40, 0.8, "Diesel", null);
        });
        
    }

    // checks if the bus ID is 8 digits, in this test case it is 9
    @Test
    void B1_TC4_busIDTooLong() {
        System.out.println("Test: B1 TC4");
        assertThrows(IllegalArgumentException.class, () -> {
            new Bus("123456789", 40, 0.8, "Diesel", null);
        });
    }

    // First add succeeds, second add with same ID must fail. In this test case both buses share "12345678" as the same ID
    @Test
    void B1_TC5_duplicateBusID() {
        System.out.println("Test: B1 TC5");
        Bus bus1 = new Bus("12345678", 40, 0.8, "Diesel", null);
        Bus bus2 = new Bus("12345678", 50, 0.5, "Diesel", null);

        assertTrue(repo.add(bus1));
        assertFalse(repo.add(bus2));

    }

    // Reducing capacity from 50 to 40 should succeed
    @Test
    void B2_TC1_capacityDecrease() {
        System.out.println("Test: B2 TC1");
        Bus bus = new Bus("11111111", 50, 0.8, "Diesel", null);
        repo.add(bus);
        assertTrue(repo.update("11111111", 40, 0.8, "Diesel"));
        assertEquals(40, repo.retrieve("11111111").getCapacity());
    }

    // Increasing capacity from 40 to 50 should fail
    @Test
    void B2_TC2_capacityIncrease() {
        System.out.println("Test: B2 TC2");
        Bus bus = new Bus("22222222", 40, 0.8, "Diesel", null);
        repo.add(bus);
        assertFalse(repo.update("22222222", 50, 0.8, "Diesel"));
    }

    // Same capacity should pass, in this case 40 is updated 40
    @Test
    void B2_TC3_capacityUnchanged() {
        System.out.println("Test: B2 TC3");
        Bus bus = new Bus("33333333", 40, 0.8, "Diesel", null);
        repo.add(bus);
        assertTrue(repo.update("33333333", 40, 0.8, "Diesel"));
    }

     // capacity of 5 should fail, as the minimum is 10
    @Test
    void B2_TC4_capacityBelowMin() {
        System.out.println("Test: B2 TC4");
        assertThrows(IllegalArgumentException.class, () -> {
             new Bus("12345678", 5, 0.8, "Diesel", null);
             });
    }

    // capacity of 100 should fail, as the maximum is 80
    @Test
    void B2_TC5_capacityAboveMax() {
        System.out.println("Test: B2 TC5");
        assertThrows(IllegalArgumentException.class, () -> {
             new Bus("12345678", 100, 0.8, "Diesel", null);
             });
    }

    // capacity of 10 should pass as it is the minimum
    @Test
    void B2_TC6_tenCapacity() {
        System.out.println("Test: B2 T6");
        assertDoesNotThrow(() -> new Bus("12345678", 10, 0, "Diesel", null));
    }

    // capacity of 80 should pass as it is the maximum
    @Test
    void B2_TC7_eightyCapacity() {
        System.out.println("Test: B2 T7");
        assertDoesNotThrow(() -> new Bus("12345678", 80, 0, "Diesel", null));
    }


    // Negative fuel level should be rejected, in this case -1.0
    @Test
    void B3_TC1_negativeFuelLevel() {
        System.out.println("Test: B3 TC1");
         assertThrows(IllegalArgumentException.class, () -> {
            new Bus("12345678", 40, -1.0, "Diesel", null);
            });
    }

    // Fuel level of 0 should pass
    @Test
    void B3_TC2_zeroFuelLevel() {
        System.out.println("Test: B3 TC2");
        assertDoesNotThrow(() -> new Bus("12345678", 40, 0, "Diesel", null));
    }

    // Fuel level of 600 should be accepted
    @Test
    void B3_TC3_fuelLevelMax() {
        System.out.println("Test: B3 TC3");
        assertDoesNotThrow(() -> new Bus("12345678", 40, 600.0, "Diesel", null));
    }

    // Fuel level above max 600 should be rejected, in this case 601
    @Test
    void B3_TC4_overMaxFuelLevel() {
        System.out.println("Test: B3 TC4");
         assertThrows(IllegalArgumentException.class, () -> {
            new Bus("12345678", 40, 601.0, "Diesel", null);
            });
    }

    // entering water as a fuel type should fail
    @Test
    void B4_TC1_invalidFuelType() {
        System.out.println("Test: B4 TC1");
        assertThrows(IllegalArgumentException.class, () -> {
             new Bus("12345678", 40, 0.8, "Water", null);
             });
    }
    
    // Driver aged 46 can drive a bus with capacity 60
    @Test
    void B3_TC1_driverUnder50_highCapacityBus_shouldAssign() {
        Bus bus = new Bus("12345678", 60, 300.0, "Diesel");
        Driver driver = new Driver("23@#abcdAB", "Alice", "10-05-1980", 5, "Heavy", "12|Main St|Melbourne|VIC|Australia");
        assertTrue(bus.assignDriver(driver));
    }

    // Driver aged 56 on a bus with capacity exactly 50 should fail
    @Test
    void B3_TC2_driverOver50_capacityAtLimit_shouldFail() {
        Bus bus = new Bus("12345678", 50, 300.0, "Diesel");
        Driver driver = new Driver("23@#abcdAB", "Bob", "10-05-1970", 10, "Heavy", "12|Main St|Melbourne|VIC|Australia");
        assertFalse(bus.assignDriver(driver));
    }
    
    // Driver aged 56 on a bus with capacity 49 should be allowed
    @Test
    void B3_TC3_driverOver50_capacityJustBelow50_shouldAssign() {
        Bus bus = new Bus("12345678", 49, 300.0, "Diesel");
        Driver driver = new Driver("23@#abcdAB", "Carol", "10-05-1970", 10, "Heavy", "12|Main St|Melbourne|VIC|Australia");
        assertTrue(bus.assignDriver(driver));
    }
 

    // Driver with 5 years experience can drive electric bus
    @Test
    void B4_TC1_fiveYearsExperience_electricBus_shouldAssign() {
        Bus bus = new Bus("12345678", 40, 300.0, "Electricity");
        Driver driver = new Driver("23@#abcdAB", "Alice", "10-05-1990", 5, "Heavy", "12|Main St|Melbourne|VIC|Australia");
        assertTrue(bus.assignDriver(driver));
    }
    
    // Driver with only 4 years experience on electric bus should fail
    @Test
    void B4_TC2_fourYearsExperience_electricBus_shouldFail() {
        Bus bus = new Bus("12345678", 40, 300.0, "Electricity");
        Driver driver = new Driver("23@#abcdAB", "Dave", "10-05-1990", 4, "Heavy", "12|Main St|Melbourne|VIC|Australia");
        assertFalse(bus.assignDriver(driver));
    }
    
    // Experience restriction only applies to electric, not diesel
    @Test
    void B4_TC3_twoYearsExperience_dieselBus_shouldAssign() {
        Bus bus = new Bus("12345678", 40, 300.0, "Diesel");
        Driver driver = new Driver("23@#abcdAB", "Eve", "10-05-1995", 2, "Light", "12|Main St|Melbourne|VIC|Australia");
        assertTrue(bus.assignDriver(driver));
    }
    // Normal: Heavy licence on electric bus should succeed
    @Test
    void B5_TC1_heavyLicence_electricBus_shouldAssign() {
        Bus bus = new Bus("12345678", 40, 300.0, "Electricity");
        Driver driver = new Driver("23@#abcdAB", "Frank", "10-05-1985", 10, "Heavy", "12|Main St|Melbourne|VIC|Australia");
        assertTrue(bus.assignDriver(driver));
    }
    // PublicTransport licence on hybrid bus should succeed
    @Test
    void B5_TC2_publicTransportLicence_hybridBus_shouldAssign() {
        Bus bus = new Bus("12345678", 40, 300.0, "Hybrid");
        Driver driver = new Driver("23@#abcdAB", "Grace", "10-05-1985", 10, "PublicTransport", "12|Main St|Melbourne|VIC|Australia");
        assertTrue(bus.assignDriver(driver));
    }

    // Light licence on hybrid bus should fail
    @Test
    void B5_TC3_lightLicence_hybridBus_shouldFail() {
        Bus bus = new Bus("12345678", 40, 300.0, "Hybrid");
        Driver driver = new Driver("23@#abcdAB", "Hank", "10-05-1990", 5, "Light", "12|Main St|Melbourne|VIC|Australia");
        assertFalse(bus.assignDriver(driver));
    }

    // Medium licence on electric bus should fail 
    @Test
    void B5_TC4_mediumLicence_electricBus_shouldFail() {
        Bus bus = new Bus("12345678", 40, 300.0, "Electricity");
        Driver driver = new Driver("23@#abcdAB", "Iris", "10-05-1990", 5, "Medium", "12|Main St|Melbourne|VIC|Australia");
        assertFalse(bus.assignDriver(driver));
    }

    // Licence restriction does not apply to diesel buses
    @Test
    void B5_TC5_lightLicence_dieselBus_shouldAssign() {
        Bus bus = new Bus("12345678", 40, 300.0, "Diesel");
        Driver driver = new Driver("23@#abcdAB", "Jack", "10-05-1990", 5, "Light", "12|Main St|Melbourne|VIC|Australia");
        assertTrue(bus.assignDriver(driver));
    }
}
 