package com.team10;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DriverRepository {

    public static final Path FILE_PATH = Paths.get("drivers.txt");
    private static final int FIELD_COUNT = 6;

    // Adds a driver to the file. Rejects null, duplicate driver IDs, and any driver that fails constructor validation.
    public boolean add(Driver driver) {
        if (driver == null) {
            System.out.println("Driver cannot be null");
            return false;
        }
        // Driver ID must be unique across all stored drivers
        if (retrieve(driver.getDriverID()) != null) {
            System.out.println("D1: A driver with ID '" + driver.getDriverID() + "' already exists.");
            return false;
        }
        try {
            Files.writeString(
                    FILE_PATH,
                    serialise(driver) + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.out.println("Failed to add driver to file: " + e);
            return false;
        }
        return true;
    }

    // Finds a driver by ID, or returns null if not found.
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

    // Returns a list of all valid drivers currently stored in the file.
    public List<Driver> retrieveAll() {
        List<Driver> drivers = new ArrayList<>();
        for (String line : readAllLines()) {
            if (line.trim().isEmpty()) continue;
            Driver driver = deserialise(line);
            if (driver != null) drivers.add(driver);
        }
        return drivers;
    }

    // Updates mutable fields of an existing driver. Returns true on success, false on failure.
    // Parameters that are null stay unchanged. 
    public boolean update(String driverID, Integer newExperience, String newLicenseType,
                          String newAddress, String newBirthdate) {
        List<String> lines = readAllLines();
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            Driver existing = deserialise(line);
            if (existing == null || !existing.getDriverID().equals(driverID)) continue;

            // Determine final values: use the new value if provided, otherwise keep the existing one
            int finalExperience;
            if (newExperience != null) {
                finalExperience = newExperience;
            } else {
                finalExperience = existing.getExperienceYears();
            }

            String finalLicenseType;
            if (newLicenseType != null) {
                finalLicenseType = newLicenseType;
            } else {
                finalLicenseType = existing.getLicenseType();
            }

            String finalAddress;
            if (newAddress != null) {
                finalAddress = newAddress;
            } else {
                finalAddress = existing.getAddress();
            }

            String finalBirthdate;
            if (newBirthdate != null) {
                finalBirthdate = newBirthdate;
            } else {
                finalBirthdate = existing.getBirthdate();
            }

            // Reject negative experience values
            if (finalExperience < 0) {
                System.out.println("Experience cannot be negative.");
                return false;
            }

            // Cannot change licence type if the driver has more than 10 years of experience
            if (newLicenseType != null && !newLicenseType.equals(existing.getLicenseType())
                    && existing.getExperienceYears() > 10) {
                System.out.println("D4: Cannot change license type when experience exceeds 10 years.");
                return false;
            }

            // Create the updated driver – the constructor will validate all fields
            try {
                Driver updated = new Driver(
                        existing.getDriverID(),
                        existing.getName(),
                        finalBirthdate,
                        finalExperience,
                        finalLicenseType,
                        finalAddress
                );
                lines.set(i, serialise(updated));
                found = true;
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Update rejected: " + e.getMessage());
                return false;
            }
        }
        if (!found) {
            System.out.println("No driver found with ID '" + driverID + "'");
            return false;
        }
        writeAllLines(lines);
        return true;
    }

    // Counts how many valid driver records are in the file.
    public int count() {
        int count = 0;
        for (String line : readAllLines()) {
            if (deserialise(line) != null) count++;
        }
        return count;
    }

    // Reads all lines from the drivers file. Returns an empty list if the file does not exist.
    private List<String> readAllLines() {
        try {
            if (!Files.exists(FILE_PATH)) return new ArrayList<>();
            return Files.readAllLines(FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read driver storage file.", e);
        }
    }

    // Writes the given list of lines back to the drivers file, overwriting the previous content.
    private void writeAllLines(List<String> lines) {
        try {
            Files.write(FILE_PATH, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to driver storage file.", e);
        }
    }

    // Converts a Driver object to a comma‑separated string for file storage.
    private String serialise(Driver driver) {
        return driver.toString();
    }

    // Reconstructs a Driver object from a comma‑separated line read from the file.
    private Driver deserialise(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.split(",", FIELD_COUNT);
        if (parts.length != FIELD_COUNT) {
            System.err.println("Warning: Skipping malformed driver record: " + line);
            return null;
        }
        try {
            String driverID = parts[0].trim();
            String name = parts[1].trim();
            int experienceYears = Integer.parseInt(parts[2].trim());
            String licenseType = parts[3].trim();
            String address = parts[4].trim();
            String birthdate = parts[5].trim();
            return new Driver(driverID, name, birthdate, experienceYears, licenseType, address);
        } catch (Exception e) {
            System.err.println("Warning: Could not parse driver record: " + line);
            return null;
        }
    }
}