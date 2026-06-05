package com.team10;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DriverRepository {

    public static final Path FILE_PATH = Paths.get("drivers.txt");
    private static final int FIELD_COUNT = 6;

    public boolean add(Driver driver) {
        if (driver == null) {
            System.out.println("Driver cannot be null");
            return false;
        }
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

    public List<Driver> retrieveAll() {
        List<Driver> drivers = new ArrayList<>();
        for (String line : readAllLines()) {
            if (line.trim().isEmpty()) continue;
            Driver driver = deserialise(line);
            if (driver != null) drivers.add(driver);
        }
        return drivers;
    }

    public boolean update(String driverID, Integer newExperience, String newLicenseType,
                          String newAddress, String newBirthdate) {
        List<String> lines = readAllLines();
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            Driver existing = deserialise(line);
            if (existing == null || !existing.getDriverID().equals(driverID)) continue;

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

            if (finalExperience < 0) {
                System.out.println("Experience cannot be negative.");
                return false;
            }

            if (newLicenseType != null && !newLicenseType.equals(existing.getLicenseType())
                    && existing.getExperienceYears() > 10) {
                System.out.println("D4: Cannot change license type when experience exceeds 10 years.");
                return false;
            }

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

    public int count() {
        int count = 0;
        for (String line : readAllLines()) {
            if (deserialise(line) != null) count++;
        }
        return count;
    }

    private List<String> readAllLines() {
        try {
            if (!Files.exists(FILE_PATH)) return new ArrayList<>();
            return Files.readAllLines(FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read driver storage file.", e);
        }
    }

    private void writeAllLines(List<String> lines) {
        try {
            Files.write(FILE_PATH, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to driver storage file.", e);
        }
    }

    private String serialise(Driver driver) {
        return driver.toString();
    }

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