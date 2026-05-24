package com.team10;
    

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
}
