package com.team10;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Bus Guidance System");
        System.out.println("What would you like to do?");
        System.out.println("Enter \"A\" for Bus driver Functions or \"B\" for Bus Functions");
        String letterSelection;
        while (true) {
        letterSelection = sc.nextLine();

        if (letterSelection.equals("A")) {
            while (true){
                System.out.println("Please select a driver function:");
                System.out.println("A) Add new driver");
                System.out.println("B) Retrieve Driver Details");
                System.out.println("C) Update existing driver details");
                System.out.println("D) Return the number of stored drivers");
                letterSelection = sc.nextLine();

                if (letterSelection.equals("A")){
                    System.out.println("Selecting option A");
                    break;
                }
                else if (letterSelection.equals("B")){
                    System.out.println("Selecting option B");
                    break;
                }
                else if (letterSelection.equals("C")){
                    System.out.println("Selecting option C");
                    break;
                }
                else if (letterSelection.equals("D")){
                    System.out.println("Selecting option D");
                    break;
                }
                else {
                    System.out.println("Please enter a valid option!");
                }
            }
            break;
        }

        else if (letterSelection.equals("B")) {
            while(true){
                System.out.println("Please select a bus function:");
                System.out.println("A) Add new buses");
                System.out.println("B) Retrieve bus Details");
                System.out.println("C) Update existing bus details");
                System.out.println("D) Return the number of stored bus");
                
                letterSelection = sc.nextLine();

                if (letterSelection.equals("A")){
                    System.out.println("Selecting option A");
                    break;
                }
                else if (letterSelection.equals("B")){
                    System.out.println("Selecting option B");
                    break;
                }
                else if (letterSelection.equals("C")){
                    System.out.println("Selecting option C");
                    break;
                }
                else if (letterSelection.equals("D")){
                    System.out.println("Selecting option D");
                    break;
                }
                else {
                    System.out.println("Please enter a valid option!");
                }
            }
            break;
        }

        else {
            System.out.println("Please enter a valid option!");

        }

    }
    }
}