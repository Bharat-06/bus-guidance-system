package com.team10;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class BusRepository {

    public static final String FILE_PATH = "buses.txt";

    
     // Loads all buses from the TXT file in busID|capacity|fuelLevel|fuelType format
    public static List<Bus> busList(){
        List<Bus> buses = new ArrayList<>();

        File file = new File(FILE_PATH);

        // If file doesn't exist returns empty list
        if (!file.exists()) {
            return buses;
        }

        try (BufferedReader bufferRead = new BufferedReader(new FileReader(file))){

            String line;

            while ((line = bufferRead.readLine()) != null){

                line = line.trim();

                // Skip empty lines
                if (line.isEmpty()){
                    continue;
                }

                String[] parts = line.split("\\|");

                // check correct format
                if (parts.length < 4){
                    continue;
                }

                String busID = parts[0];
                int capacity = Integer.parseInt(parts[1]);
                double fuelLevel = Double.parseDouble(parts[2]);
                String fuelType = parts[3];

                buses.add(new Bus(busID, capacity, fuelLevel, fuelType));
            }

        } 
        catch (IOException e){
            System.out.println("Error reading buses file");
        }

        return buses;
    }

    
    //Saves all buses back into the TXT file.
    
    public static void saveBuses(List<Bus> buses){
// fucked overrides bus for no dumb fucking reason
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH, false))) {

            for (Bus b : buses){
                bufferedWriter.write(b.getBusID() + "|" + b.getCapacity() + "|" + b.getFuelLevel() + "|" + b.getFuelType());
                bufferedWriter.newLine();
            }

        } 
        catch (IOException e){
            System.out.println("Error saving buses file: " + e.getMessage());
        }
    }

    //adding new bus
    public static boolean add(Bus bus){

        // validate fields
        if (!Bus.validBusID(bus.getBusID())){
            return false;
        }
        if (!Bus.validFuelType(bus.getFuelType())){
            return false;
        }
        if (bus.getCapacity() <= 0){
            return false;
        }
        if (bus.getFuelLevel() < 0){
            return false;
        }

        // Check duplicate bus ID
        List<Bus> buses = busList();

        for (Bus b : buses){
            if (b.getBusID().equals(bus.getBusID())){
                return false;
            }
        }

        buses.add(bus);

        saveBuses(buses);

        return true;
    }

    //looks for bus using busID
    public static Bus retrieve(String busID){

        List<Bus> buses = busList();

        for (Bus b : buses){
            if (b.getBusID().equals(busID)){
                return b;
            }
        }

        return null;
    }

    //updating bus info
    public static boolean update(String busID, int newCapacity, double newFuelLevel, String newFuelType){

        List<Bus> buses = busList();//SHOULD COME FROM FILE LIST

        for (int i = 0; i < buses.size(); i++){
            Bus existingBus = buses.get(i);
            if (existingBus.getBusID().equals(busID)){

                // Capacity cant increase
                if (!Bus.capacityUpdate(existingBus.getCapacity(), newCapacity)){
                    return false;
                }

                // Validate fields
                if (newCapacity <= 0){
                    return false;
                }
                if (newFuelLevel < 0){
                    return false;
                }
                if (!Bus.validFuelType(newFuelType)){
                    return false;
                }

                // Replace updated bus
                Bus updatedBus = new Bus(busID, newCapacity, newFuelLevel, newFuelType);

                buses.set(i, updatedBus);
                saveBuses(buses);
                return true;
            }
        }

        // Bus not found
        return false;
    }


    //Returns number of stored buses.
    public static int count(){
        return busList().size();
    }
}