package com.team10;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Driver {

    private String driverID;
    private String name;
    private int experienceYears;
    private String licenseType;
    private String address;
    private String birthdate;
    private int age;

    private static final String[] VALID_LICENSES =
            {"Light", "Medium", "Heavy", "PublicTransport"};

    public Driver(String driverID, String name, String birthdate, int experienceYears,
                  String licenseType, String address) {

        if (!isValidDriverID(driverID)) {
            throw new IllegalArgumentException("Invalid driverID");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (!isValidBirthdate(birthdate)) {
            throw new IllegalArgumentException("Invalid birthdate");
        }
        if (!isValidExperience(experienceYears, birthdate)) {
            throw new IllegalArgumentException("Invalid experience");
        }
        if (!isValidLicenseType(licenseType)) {
            throw new IllegalArgumentException("Invalid license type");
        }
        if (!isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid address");
        }

        this.driverID = driverID;
        this.name = name;
        this.birthdate = birthdate;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
        this.address = address;
        this.age = calculateAge();
    }

    public int calculateAge() {
        return getAge(this.birthdate, LocalDate.now());
    }

    public static boolean isValidDriverID(String id) {
        if (id == null || id.length() != 10) return false;

        for (int i = 0; i < 2; i++) {
            char c = id.charAt(i);
            if (c < '2' || c > '9') return false;
        }

        int special = 0;
        for (int i = 2; i <= 7; i++) {
            if (!Character.isLetterOrDigit(id.charAt(i))) special++;
        }
        if (special < 2) return false;

        for (int i = 8; i < 10; i++) {
            char c = id.charAt(i);
            if (c < 'A' || c > 'Z') return false;
        }

        return true;
    }

    public static boolean isValidExperience(int experienceYears, String birthdate) {
        if (birthdate == null) return false;
        if (experienceYears < 0) return false;

        try {
            int age = getAge(birthdate, LocalDate.now());
            int maxAllowed = age - 18;
            return experienceYears <= maxAllowed;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidAddress(String address) {
        if (address == null) return false;

        String[] parts = address.split("\\|", -1);
        if (parts.length != 5) return false;

        for (String p : parts) {
            if (p.trim().isEmpty()) return false;
        }
        return true;
    }

    public static boolean isValidBirthdate(String birthdate) {
        if (birthdate == null) return false;

        try {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd-MM-uuuu")
                            .withResolverStyle(java.time.format.ResolverStyle.STRICT);
            LocalDate.parse(birthdate, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidLicenseType(String licenseType) {
        if (licenseType == null) return false;
        for (String v : VALID_LICENSES) {
            if (v.equals(licenseType)) return true;
        }
        return false;
    }

    public static int getAge(String birthdate, LocalDate today) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dob = LocalDate.parse(birthdate, fmt);
        return Period.between(dob, today).getYears();
    }

    public String getDriverID() {
        return driverID;
    }

    public String getName() {
        return name;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return driverID + "," + name + "," + experienceYears + ","
                + licenseType + "," + address + "," + birthdate;
    }
}