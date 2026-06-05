package com.team10;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class BusRepository {

    public static final Path FILE_PATH = Paths.get("buses.txt");
    private static final int FIELD_COUNT = 5;

    // Adds a new bus to the file, rejects null buses and duplicate IDs
    public boolean add(Bus bus) {

        if (bus == null) {
            return false;
        }

        if (retrieve(bus.getBusID()) != null) {
            System.out.println("Bus already exists");
            return false;
        }

        try {
            Files.writeString(FILE_PATH, serialise(bus) + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } 
        catch (IOException e) {
            return false;
        }
        return true;
    }

    // Finds and returns a bus by its ID, returns null if not found
    public Bus retrieve(String busID) {

        if (busID == null || busID.isEmpty()) {
            return null;
        }

        for (String line : readAllLines()) {
            Bus bus = deserialise(line);
            if (bus != null && bus.getBusID().equals(busID)) {
                return bus;
            }
        }
        return null;
    }

    // Returns a list of all buses stored in the file
    public List<Bus> retrieveAll() {
        List<Bus> buses = new ArrayList<>();
        for (String line : readAllLines()) {
            Bus bus = deserialise(line);
            if (bus != null) {
                buses.add(bus);
            }
        }
        return buses;
    }

    // Updates an existing bus 
    public boolean update(String busID, int newCapacity, double newFuelLevel, String newFuelType) {
        List<String> lines = readAllLines();
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            Bus existing = deserialise(lines.get(i));
            if (existing == null){
                continue;
            }
            if (!existing.getBusID().equals(busID)){
                continue;
            }

            // Capacity cannot increase during update
            if (newCapacity > existing.getCapacity()) {
                return false;
            }

            try {
                Bus updated = new Bus(
                        busID,
                        newCapacity,
                        newFuelLevel,
                        newFuelType,
                        existing.getAssignedDriverID()
                );
                lines.set(i, serialise(updated));
                found = true;
                break;
            } 
            catch (IllegalArgumentException e) {
                return false;
            }
        }

        if (!found) {
            return false;
        }
        writeAllLines(lines);
        return true;
    }

    // Saves the assigned driver ID to an existing bus record
    public boolean saveAssignment(String busID, String assignedDriverID) {
        List<String> lines = readAllLines();
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            Bus bus = deserialise(lines.get(i));
            if (bus == null){
                continue;
            }
            if (bus.getBusID().equals(busID)) {
                bus.setAssignedDriverID(assignedDriverID);
                lines.set(i, serialise(bus));
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }
        writeAllLines(lines);
        return true;
    }

    // Returns the number of valid bus records in the file
    public int count() {
        int count = 0;
        for (String line : readAllLines()) {
            if (deserialise(line) != null) {
                count++;
            }
        }
        return count;
    }

    // Reads all lines from the file, returns empty list if file doesnt exist
    private List<String> readAllLines() {
        try {
            if (!Files.exists(FILE_PATH)) {
                return new ArrayList<>();
            }
            return Files.readAllLines(FILE_PATH);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    // Overwrites the file with updated lines
    private void writeAllLines(List<String> lines) {
        try {
            Files.write(FILE_PATH, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } 
        catch (IOException e) {
            System.out.println("write failed");
        }
    }

    // Converts a Bus object to a comma separated string for file storage
    private String serialise(Bus bus) {
        String driver = bus.getAssignedDriverID();
        if (driver == null) {
            driver = "";
        }
        return bus.getBusID() + "," + bus.getCapacity() + "," + bus.getFuelLevel() + "," + bus.getFuelType() + "," + driver;
    }

    // Parses a comma separated line back into a Bus object and returns null if not right
    private Bus deserialise(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        String[] parts = line.split(",", FIELD_COUNT);
        if (parts.length != FIELD_COUNT) {
            return null;
        }
        try {
            String busID = parts[0].trim();
            int capacity = Integer.parseInt(parts[1].trim());
            double fuelLevel = Double.parseDouble(parts[2].trim());
            String fuelType = parts[3].trim();
            String driver = parts[4].trim();

            if (driver.isEmpty()) {
                driver = null;
            }
            return new Bus(busID, capacity, fuelLevel, fuelType, driver);
        }
        catch (Exception e) {
            return null;
        }
    }
}