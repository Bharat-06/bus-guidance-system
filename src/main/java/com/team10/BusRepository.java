package com.team10;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class BusRepository {

    private static final Path FILE_PATH =
            Paths.get("buses.txt");

    private static final int FIELD_COUNT = 4;

    // Add Bus
    public boolean add(Bus bus) {

        if (bus == null) {
            System.out.println("Bus cannot be null");
            return false;
        }

        // Prevent duplicate bus IDs
        if (retrieve(bus.getBusID()) != null) {
            System.out.println(
                    "A bus with ID '" + bus.getBusID() + "' already exists.");
            return false;
        }

        try {
            Files.writeString(
                    FILE_PATH,
                    serialise(bus) + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        }
        catch (IOException e) {
            System.out.println("Failed to add bus to file: " + e);
            return false;
        }

        return true;
    }

    // Retrieve Bus by ID
    public Bus retrieve(String busID) {

        if (busID == null || busID.isEmpty()) {
            return null;
        }

        for (String line : readAllLines()) {

            if (line.trim().isEmpty()) {
                continue;
            }

            Bus bus = deserialise(line);

            if (bus != null &&
                    bus.getBusID().equals(busID)) {
                return bus;
            }
        }

        return null;
    }

    // Retrieve all buses
    public List<Bus> retrieveAll() {

        List<Bus> buses = new ArrayList<>();

        for (String line : readAllLines()) {

            if (line.trim().isEmpty()) {
                continue;
            }

            Bus bus = deserialise(line);

            if (bus != null) {
                buses.add(bus);
            }
        }

        return buses;
    }

    // Update Bus
    public boolean update(
            String busID,
            int newCapacity,
            double newFuelLevel,
            String newFuelType) {

        List<String> lines = readAllLines();

        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i).trim();

            if (line.isEmpty()) {
                continue;
            }

            Bus existingBus = deserialise(line);

            if (existingBus != null &&
                    existingBus.getBusID().equals(busID)) {

                // B2: Capacity cannot increase
                if (newCapacity > existingBus.getCapacity()) {
                    return false;
                }

                Bus updatedBus = new Bus(
                        busID,
                        newCapacity,
                        newFuelLevel,
                        newFuelType,
                        existingBus.getAssignedDriver()
                );

                lines.set(i, serialise(updatedBus));

                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println(
                    "No bus found with ID '" + busID + "'");
            return false;
        }

        writeAllLines(lines);

        return true;
    }

    // Count buses
    public int count() {

        int count = 0;

        for (String line : readAllLines()) {

            if (deserialise(line) != null) {
                count++;
            }
        }

        return count;
    }


    private List<String> readAllLines() {

        try {

            if (!Files.exists(FILE_PATH)) {
                return new ArrayList<>();
            }

            return Files.readAllLines(FILE_PATH);

        }
        catch (IOException e) {

            throw new RuntimeException(
                    "Failed to read bus storage file.",
                    e);
        }
    }

    private void writeAllLines(List<String> lines) {

        try {

            Files.write(
                    FILE_PATH,
                    lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        }
        catch (IOException e) {

            throw new RuntimeException(
                    "Failed to write to bus storage file.",
                    e);
        }
    }

    private String serialise(Bus bus) {

        return bus.getBusID() + "," +
                bus.getCapacity() + "," +
                bus.getFuelLevel() + "," +
                bus.getFuelType();
    }

    private Bus deserialise(String line) {

        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",", FIELD_COUNT);

        if (parts.length != FIELD_COUNT) {
            System.err.println(
                    "Warning: Skipping malformed bus record: " + line);
            return null;
        }

        try {

            String busID = parts[0].trim();
            int capacity =
                    Integer.parseInt(parts[1].trim());
            double fuelLevel =
                    Double.parseDouble(parts[2].trim());
            String fuelType =
                    parts[3].trim();

            return new Bus(
                    busID,
                    capacity,
                    fuelLevel,
                    fuelType,
                    null
            );

        }
        catch (Exception e) {

            System.err.println(
                    "Warning: Could not parse bus record: " + line);

            return null;
        }
    }
}