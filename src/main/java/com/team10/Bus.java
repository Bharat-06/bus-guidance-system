package com.team10;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType; // Diesel, Hybrid, Electricity

    //constructors
    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
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

    // validating busID
    public static boolean validBusID(String id) {
        if (id == null || id.length() != 8){
            return false;
        }
        char[] chars = id.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }


    //validate fuel type
    public static boolean validFuelType(String fuelType){
        if (fuelType.equals(null)){
            return false;
        }
        else if (fuelType.equals("Diesel")|| fuelType.equals("Hybrid")|| fuelType.equals("Electricity")){
            return true;
        }
        return false;
    }


    // checking driver age to see if they can drive specific bus
    public static boolean isDriverAgeValidForBus(String birthdate, int capacity) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    try {
        LocalDate dob = LocalDate.parse(birthdate, formatter);
        int age = Period.between(dob, LocalDate.now()).getYears();
        if (age > 50 && capacity >= 50) {
            return false;
        }
        return true;
    } 
    catch (Exception e) {
        return false;
    }
}

    // check if driver can drive electric bus
    public static boolean isDriverExperienceValidForElectric(int experienceYears, String fuelType) {
        if (fuelType.equals("Electricity") && experienceYears < 5){
            return false;
        }
        return true;
    }

    // drivers with heavy or public transport license operating electric and hybrid buses
    public static boolean isLicenseValidForBus(String licenseType, String fuelType) {
        if (fuelType.equals("Electricity") || fuelType.equals("Hybrid")) {
            return licenseType.equals("Heavy") || licenseType.equals("PublicTransport");
        }
        return true;
    }
}
