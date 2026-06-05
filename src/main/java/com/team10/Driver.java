package com.team10;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;



 //Driver class for Intelligent Bus Driver Guidance System.


public class Driver {


    private String driverID;      
    private String name;           
    private int experienceYears;
    private String licenseType;
    private String address;
    private String birthdate;

    private static final String[] VALID_LICENSES =
            {"Light", "Medium", "Heavy", "PublicTransport"};

    // Constructor

    public Driver(String driverID, String name, int experienceYears,
                  String licenseType, String address, String birthdate) {

        if (!isValidDriverID(driverID)) {
            System.out.println("Invalid driverID");
            driverID = "12345678";
        }

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Invalid name");
            name = "noname";
        }

        if (experienceYears < 0) {
            System.out.println("Invalid experience");
            experienceYears = 0;
        }

        if (!isValidLicenseType(licenseType)) {
            System.out.println("Invalid license type");
        
        }

        if (!isValidAddress(address)) {
            System.out.println("Invalid address");
        }

        if (!isValidBirthdate(birthdate)) {
            System.out.println("Invalid birthdate");
        }

        this.driverID = driverID;
        this.name = name;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
        this.address = address;
        this.birthdate = birthdate;
    }

    // Update 

    public boolean update(String newDriverID, String newName, Integer newExperienceYears,
                       String newLicenseType, String newAddress, String newBirthdate) {

        // D5: immutable fields
        if (newDriverID != null && !newDriverID.equals(this.driverID)) {
            System.out.println("D5: driverID cannot be changed");
            return false;
        }

        if (newName != null && !newName.equals(this.name)) {
            System.out.println("D5: name cannot be changed");
            return false;
        }

        // Update experience first safely
        int updatedExperience = this.experienceYears;
        if (newExperienceYears != null) {
            if (newExperienceYears < 0) {
                System.out.println("Invalid experience");
                return false;
            }
            updatedExperience = newExperienceYears;
        }

        // D4 check BEFORE applying license update
        if (newLicenseType != null) {
            if (updatedExperience > 10 &&
                    !newLicenseType.equals(this.licenseType)) {
                System.out.println(
                        "D4: cannot change license after 10 years experience");
            }

            if (!isValidLicenseType(newLicenseType)) {
                System.out.println("Invalid license type");
            }
        }

        // Apply updates AFTER validation
        if (newExperienceYears != null) {
            this.experienceYears = newExperienceYears;
        }

        if (newLicenseType != null) {
            this.licenseType = newLicenseType;
        }

        if (newAddress != null) {
            if (!isValidAddress(newAddress)) {
                System.out.println("Invalid address");
            }
            this.address = newAddress;
        }

        if (newBirthdate != null) {
            if (!isValidBirthdate(newBirthdate)) {
                System.out.println("Invalid birthdate");
            }
            this.birthdate = newBirthdate;
        }
        return true;
    }

    // Driver ID Validation
    public static boolean isValidDriverID(String id) {
        if (id == null || id.length() != 10) return false;

        // first two digits 2–9
        for (int i = 0; i < 2; i++) {
            char c = id.charAt(i);
            if (c < '2' || c > '9') return false;
        }

        // at least 2 special chars in positions 3–8
        int special = 0;
        for (int i = 2; i <= 7; i++) {
            char c = id.charAt(i);
            if (!Character.isLetterOrDigit(c)) special++;
        }
        if (special < 2) return false;

        // last two uppercase letters
        for (int i = 8; i < 10; i++) {
            char c = id.charAt(i);
            if (c < 'A' || c > 'Z') return false;
        }

        return true;
    }

    // Address Validation
    public static boolean isValidAddress(String address) {
        if (address == null) return false;

        String[] parts = address.split("\\|", -1);
        if (parts.length != 5) return false;

        for (String p : parts) {
            if (p.trim().isEmpty()) return false;
        }
        return true;
    }

    // Birthdate Validation
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

    // License Validation
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

    public int getAge() {
        return getAge(this.birthdate, LocalDate.now());
    }
    

    // Getters
    public String getDriverID() { return driverID; }
    public String getName() { return name; }
    public int getExperienceYears() { return experienceYears; }
    public String getLicenseType() { return licenseType; }
    public String getAddress() { return address; }
    public String getBirthdate() { return birthdate; }

    @Override
    public String toString() {
        return driverID + "," + name + "," + experienceYears + ","
                + licenseType + "," + address + "," + birthdate;
    }
}