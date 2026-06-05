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

    // Setter for assigned driver
    public void setAssignedDriverID(String assignedDriverID) {
        this.assignedDriverID = assignedDriverID;
    }

    // Assign a driver to this bus, enforces B3, B4, B5 using the Driver object
    public boolean assignDriver(Driver driver) {
        if (driver == null) {
            return false;
        }

        // Drivers older than 50 cannot drive buses with capacity >= 50
        if (driver.getAge() > 50 && this.capacity >= 50) {
            return false;
        }

        // Only drivers with at least 5 years of experience can drive electric buses
        if ("Electricity".equals(this.fuelType) && driver.getExperienceYears() < 5) {
            return false;
        }


        // Only Heavy or PublicTransport licence for electric and hybrid buses
        if (("Electricity".equals(this.fuelType) || "Hybrid".equals(this.fuelType))
                && !("Heavy".equals(driver.getLicenseType()) || "PublicTransport".equals(driver.getLicenseType()))) {
            return false;
        }

        // All checks passed – assign the driver
        this.assignedDriverID = driver.getDriverID();
        return true;
    }

// Validates busID confirming length and only having numerical values
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
// Validates buses maximum and minimum capacity
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
// Validates the buses fuel level making sure it isnt negative or too much
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
// validates buses fuel type
    public static boolean validFuelType(String fuelType) {
        if ("Diesel".equals(fuelType) || "Hybrid".equals(fuelType) || "Electricity".equals(fuelType)) {
            return true;
        }
        System.out.println("Fuel type must be Diesel, Hybrid, or Electricity.");
        return false;
    }
}


