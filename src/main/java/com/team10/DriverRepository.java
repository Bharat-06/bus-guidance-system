
package com.team10;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

// DriverRepository handles all file-based storage operations for Driver records.

public class DriverRepository {

    private final String filePath;
    private static final int FIELD_COUNT = 6;


    public DriverRepository(String filePath) {
        this.filePath = filePath;
        initialiseFile();
    }

    
    private void initialiseFile() {
        try {
            File file = new File(filePath);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialise driver storage file: " + filePath, e);
        }
    }

    // Add Driver
   
    // Adds a new driver to the TXT file and rejects duplicate driverIDs.
    
    public void add(Driver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null.");
        }

        // Ensures all drivers are unique
        if (retrieve(driver.getDriverID()) != null) {
            throw new IllegalArgumentException(
                    "D1: A driver with ID '" + driver.getDriverID() + "' already exists.");
        }

        // append new driver record to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(serialise(driver));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to add driver to file.", e);
        }
    }

    // Find a driver based on driver ID

    public Driver retrieve(String driverID) {
        if (driverID == null || driverID.isEmpty()) return null;

        for (String line : readAllLines()) {
            if (line.trim().isEmpty()) continue;
            Driver driver = deserialise(line);
            if (driver != null && driver.getDriverID().equals(driverID)) {
                return driver;
            }
        }
        return null;
    }

    // Retreive all driver details stored in file
    public List<Driver> retrieveAll() {
        List<Driver> drivers = new ArrayList<>();
        for (String line : readAllLines()) {
            if (line.trim().isEmpty()) continue;
            Driver driver = deserialise(line);
            if (driver != null) drivers.add(driver);
        }
        return drivers;
    }

    // Update driver details
    public void update(String driverID, Integer newExperience, String newLicenseType,
                       String newAddress, String newBirthdate) {

        List<String> lines = readAllLines();
        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            Driver driver = deserialise(line);
            if (driver != null && driver.getDriverID().equals(driverID)) {
                // Driver.update() enforces D4 and D5 automatically
                driver.update(null, null, newExperience, newLicenseType, newAddress, newBirthdate);
                lines.set(i, serialise(driver));
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException(
                    "Update failed: No driver found with ID '" + driverID + "'.");
        }

        writeAllLines(lines);
    }

    // Returns the number of valid driver records in the file.

    public int count() {
        int count = 0;
        for (String line : readAllLines()) {
            if (deserialise(line) != null) count++;
        }
        return count;
    }

    // HELPER FUNCTIONS
    // Reads all lines from the TXT file.
    private List<String> readAllLines() {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read driver storage file.", e);
        }
    }


    // Overwrites the TXT file with the updated details.

    private void writeAllLines(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to driver storage file.", e);
        }
    }

    // converts a Driver to a comma-separated string for file storage.

    private String serialise(Driver driver) {
        return driver.toString();
    }


    // Parses a comma-separated line back into a Driver object.

    private Driver deserialise(String line) {
        if (line == null || line.trim().isEmpty()) return null;

        String[] parts = line.split(",", FIELD_COUNT);
        if (parts.length != FIELD_COUNT) {
            System.err.println("Warning: Skipping malformed driver record: " + line);
            return null;
        }

        try {
            String driverID      = parts[0].trim();
            String name          = parts[1].trim();
            int experienceYears  = Integer.parseInt(parts[2].trim());
            String licenseType   = parts[3].trim();
            String address       = parts[4].trim();
            String birthdate     = parts[5].trim();

            return new Driver(driverID, name, experienceYears, licenseType, address, birthdate);
        } catch (Exception e) {
            System.err.println("Warning: Could not parse driver record: " + line);
            return null;
        }
    }
}