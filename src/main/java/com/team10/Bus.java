package com.team10;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType; // Diesel, Hybrid, Electricity
    private String assignedDriverID;
    public static final double maxFuel = 600;
    public static final int maxCapacity = 80;
    public static final int minCapacity = 10;
    //constructors
    public Bus(String busID, int capacity, double fuelLevel, String fuelType, String assignedDriverID){
        if (!validBusID(busID)){
            throw new IllegalArgumentException("Invalid Bus ID");
        }
        if (!capacityMaxMin(capacity)){
            throw new IllegalArgumentException("Invalid Capacity");
        }

        if (!validFuelLevel(fuelLevel)){
            throw new IllegalArgumentException("Invalid fuel level");
        }

        if (!validFuelType(fuelType)){
            throw new IllegalArgumentException("Invalid fuel type");
        }

        

        
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
        this.assignedDriverID = "unassigned driver";
    }

    public String getBusID(){
        return busID;
    }
    public int getCapacity(){
        return capacity;
    }
    public double getFuelLevel(){
        return fuelLevel;
    }
    public String getFuelType(){
        return fuelType;
    }
    public String getAssignedDriver(){
        return assignedDriverID;
    }

    // validating busID
    public static boolean validBusID(String id){
        if (id == null || id.length() != 8){
            System.out.println("BusID should be 8 units long.");
            return false;
        }
        char[] chars = id.toCharArray();
        for (int i = 0; i < chars.length; i++){
            char c = chars[i];
            if (!Character.isDigit(c)){
                System.out.println("BusID should only contain numerical values.");
                return false;
            }
        }
        return true;
    }

    public static boolean capacityMaxMin(int capacity){
        if (capacity < minCapacity){
            System.out.println("Capacity too low.");
            return false;
        }
        else if (capacity > maxCapacity){
            System.out.println("Capacity too high.");
            return false;
        }
        return true;
    }

    public static boolean validFuelLevel(double fuelLevel){
        if (fuelLevel < 0){
            System.out.println("Fuel level must be larger than 0.");
            return false;
        }
        else if (fuelLevel > maxFuel){
            System.out.println("Fuel level must be less than 600.");
            return false;
        }
        return true;
    }  

    //validate fuel type
    public static boolean validFuelType(String fuelType){
        if (fuelType.equals("Diesel")|| fuelType.equals("Hybrid")|| fuelType.equals("Electricity")){
            return true;
        }
        System.out.println("Fuel Type must be Diesel, Hybrid or Electricity");
        return false;
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



}
