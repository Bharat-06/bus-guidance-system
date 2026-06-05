package com.team10;
import java.time.Year;
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

    private int age;

    private static final String[] VALID_LICENSES =
            {"Light", "Medium", "Heavy", "PublicTransport"};

    // Constructor

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

        String[] parts = this.birthdate.split("-");
        int birthYear = Integer.parseInt(parts[2]);
        int currentYear = Year.now().getValue();

        return currentYear - birthYear;
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
    public static boolean isValidExperience(int experienceYears, String birthdate) {
        if (birthdate == null) {
            return false;
        }
        
        try {
            int age = getAge(birthdate, LocalDate.now());
            int maxAllowedExperience = age - 18;
            
            // Experience cannot be higher than (Age - 18)
            return experienceYears <= maxAllowedExperience;
        } catch (Exception e) {
            return false;
        }
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


    

    // Getters
    public String getDriverID() { return driverID; }
    public String getName() { return name; }
    public int getExperienceYears() { return experienceYears; }
    public String getLicenseType() { return licenseType; }
    public String getAddress() { return address; }
    public String getBirthdate() { return birthdate; }
    public int getAge() { return age;
    }
    

    @Override
    public String toString() {
        return driverID + "," + name + "," + experienceYears + ","
                + licenseType + "," + address + "," + birthdate;
    }
}



