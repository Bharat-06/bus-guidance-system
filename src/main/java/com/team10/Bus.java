package com.team10;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType; // Diesel, Hybrid, Electricity
    private String assignedDriverID; // Driver ID assigned to this bus, null or empty means unassigned

    public static final double maxFuel = 600;
    public static final int maxCapacity = 80;
    public static final int minCapacity = 10;

    // Constructors
    public Bus(String busID, int capacity, double fuelLevel, String fuelType, String assignedDriverID) {
        if (!validBusID(busID)) {
            throw new IllegalArgumentException("Invalid Bus ID");
        }
        if (!capacityMaxMin(capacity)) {
            throw new IllegalArgumentException("Invalid Capacity");
        }
        if (!validFuelLevel(fuelLevel)) {
            throw new IllegalArgumentException("Invalid fuel level");
        }
        if (!validFuelType(fuelType)) {
            throw new IllegalArgumentException("Invalid fuel type");
        }

        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
        this.assignedDriverID = assignedDriverID; // Can be null or a valid driver ID
    }

    // Convenience constructor (no assigned driver)
    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        this(busID, capacity, fuelLevel, fuelType, null);
    }

    // Getters
    public String getBusID() {
        return busID;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getAssignedDriverID() {
        return assignedDriverID;
    }

    // Setter for assigned driver (optional, but assignDriver is preferred)
    public void setAssignedDriverID(String assignedDriverID) {
        this.assignedDriverID = assignedDriverID;
    }

    // Assign a driver to this bus – enforces B3, B4, B5 using the Driver object
    public boolean assignDriver(Driver driver) {
        if (driver == null) {
            return false;
        }

        // B3: Driver Age Restriction
        // Drivers older than 50 cannot drive buses with capacity >= 50
        if (driver.getAge() > 50 && this.capacity >= 50) {
            return false;
        }

        // B4: Electric Bus Restriction
        // Only drivers with at least 5 years of experience can drive electric buses
        if ("Electricity".equals(this.fuelType) && driver.getExperienceYears() < 5) {
            return false;
        }

        // B5: Driver Licence Restriction
        // Only Heavy or PublicTransport licence for electric and hybrid buses
        if (("Electricity".equals(this.fuelType) || "Hybrid".equals(this.fuelType))
                && !("Heavy".equals(driver.getLicenseType()) || "PublicTransport".equals(driver.getLicenseType()))) {
            return false;
        }

        // All checks passed – assign the driver
        this.assignedDriverID = driver.getDriverID();
        return true;
    }

    // ------------------- Static Validation Methods -------------------

    public static boolean validBusID(String id) {
        if (id == null || id.length() != 8) {
            System.out.println("BusID should be 8 characters long.");
            return false;
        }
        for (int i = 0; i < id.length(); i++) {
            if (!Character.isDigit(id.charAt(i))) {
                System.out.println("BusID should only contain numerical values.");
                return false;
            }
        }
        return true;
    }

    public static boolean capacityMaxMin(int capacity) {
        if (capacity < minCapacity) {
            System.out.println("Capacity too low. Minimum is " + minCapacity);
            return false;
        }
        if (capacity > maxCapacity) {
            System.out.println("Capacity too high. Maximum is " + maxCapacity);
            return false;
        }
        return true;
    }

    public static boolean validFuelLevel(double fuelLevel) {
        if (fuelLevel < 0) {
            System.out.println("Fuel level cannot be negative.");
            return false;
        }
        if (fuelLevel > maxFuel) {
            System.out.println("Fuel level cannot exceed " + maxFuel);
            return false;
        }
        return true;
    }

    public static boolean validFuelType(String fuelType) {
        if ("Diesel".equals(fuelType) || "Hybrid".equals(fuelType) || "Electricity".equals(fuelType)) {
            return true;
        }
        System.out.println("Fuel type must be Diesel, Hybrid, or Electricity.");
        return false;
    }
}


    // // checking driver age to see if they can drive specific bus
    // // fixed reference date to match test case
    // private static final LocalDate test_date = LocalDate.of(2025, 6, 2);

    // public static boolean isDriverAgeValidForBus(String birthdate, int capacity){
    //     return isDriverAgeValidForBus(birthdate, capacity, test_date);
    // }

    // public static boolean isDriverAgeValidForBus(String birthdate, int capacity, LocalDate today){
    //     try {
    //         int age = Driver.getAge(birthdate, today);
    //         if (age > 50 && capacity >= 50){
    //             return false;
    //         }
    //         return true;
    //     } 
    //     catch (Exception e){
    //         return false;
    //     }
    // }

    // // check if driver can drive electric bus
    // public static boolean isDriverExperienceValidForElectric(int experienceYears, String fuelType) {
    //     if (fuelType.equals("Electricity") && experienceYears < 5){
    //         return false;
    //     }
    //     return true;
    // }

    // // drivers with heavy or public transport license operating electric and hybrid buses
    // public static boolean isLicenseValidForBus(String licenseType, String fuelType) {
    //     if (fuelType.equals("Electricity") || fuelType.equals("Hybrid")) {
    //         return licenseType.equals("Heavy") || licenseType.equals("PublicTransport");
    //     }
    //     return true;
    // }

    

    // public static boolean capacityCheck(int oldCapacity, int newCapacity) {
    //     return newCapacity <= oldCapacity;
    // }




