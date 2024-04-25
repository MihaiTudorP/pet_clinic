package runner;

import model.*;
import repository.OwnerRepository;
import repository.TreatmentRepository;
import service.ClinicService;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class ClinicRunner {

    public static final String OWNER_NAME_PROMPT = "What's the owner's name?";

    public static void main(String[] args) throws FileNotFoundException {
        ClinicService clinicService = new ClinicService(new OwnerRepository(), new TreatmentRepository());

        while (true) {
            displayMenu(clinicService);
        }
    }

    private static void displayMenu(ClinicService clinicService) {
        System.out.println("""
                Welcome to the pet clinic!
                
                Select an option from the menu below:
                1. Add owners to the clinic
                2. Add patient to the clinic
                3. Add treatment to a patient in the clinic
                4. Display all details for an owner
                5. Display the total cost for a patient in the clinic
                6. Exit
               
                """);

        Scanner scanner = new Scanner(System.in);
        int option = Integer.parseInt(scanner.nextLine());

        switch (option) {
            case 1 -> readOwners(clinicService);
            case 2 -> readPatients(clinicService);
            case 3 -> readTreaments(clinicService);
            case 4 -> displayOwnerDetails(clinicService);
            case 5 -> displayPatientBill(clinicService);
            case 6 -> System.exit(0);
            default -> System.out.println("Invalid option");
        }
    }

    private static void displayPatientBill(ClinicService clinicService) {
        String ownerName = readLookupName(OWNER_NAME_PROMPT);
        String patientName = readLookupName("Enter patient name");
        Animal patient = clinicService.getPatient(clinicService.getOwner(ownerName), patientName);
        System.out.printf("Your total cost for %s is: %f%n", patient.getName(), patient.calculateBillTotal());
    }

    private static void displayOwnerDetails(ClinicService clinicService) {
        String ownerName = readLookupName(OWNER_NAME_PROMPT);
        Owner owner = clinicService.getOwner(ownerName);
        System.out.println(owner);
    }

    private static void readTreaments(ClinicService clinicService) {
        String ownerName = readLookupName(OWNER_NAME_PROMPT);
        Owner owner = clinicService.getOwner(ownerName);
        String patientName = readLookupName("What's the patient name?");
        Animal patient = clinicService.getPatient(owner, patientName);
        String treatmentName = readLookupName("What's the treatment name?");
        clinicService.addPatientTreatment(owner, patient, treatmentName);
    }

    private static void readOwners(ClinicService clinicService){
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many owners are in the clinic? ");
        int owners = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < owners; i++) {
            clinicService.addOwner(readOwner(scanner));
        }
    }

    private static void readPatients(ClinicService clinicService){
        String ownerName = readLookupName(OWNER_NAME_PROMPT);
        Owner owner = clinicService.getOwner(ownerName);
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many animals does this owner have? ");
        int animals = Integer.parseInt(scanner.nextLine());

        for (int j = 0; j < animals; j++) {
            System.out.println("Enter animal species: ");
            String animalSpecies = scanner.nextLine();
            System.out.println("Enter animal name: ");
            String animalName = scanner.nextLine();
            Animal patient = createAnimal(animalSpecies, scanner, animalName);
            clinicService.addPatient(owner, patient);

        }
    }

    private static Owner readOwner(Scanner scanner) {
        System.out.println("Enter owner name: ");
        String name = scanner.nextLine();
        System.out.println("Enter owner street: ");
        String street = scanner.nextLine();
        System.out.println("Enter street number: ");
        String number = scanner.nextLine();
        System.out.println("Enter owner city: ");
        String city = scanner.nextLine();
        Address address = new Address(street, number, city);
        return new Owner(name, address);
    }

    private static Animal createAnimal(String animalSpecies, Scanner scanner, String animalName) {
        return switch (animalSpecies) {
            case "Dog" -> createDog(scanner, animalName);
            case "Cat" -> createCat(scanner, animalName);
            case "Parrot" -> createParrot(scanner, animalName);
            default -> null;
        };
    }

    private static Parrot createParrot(Scanner scanner, String name) {
        System.out.println("What species of parrot is it? ");
        String species = scanner.nextLine();
        System.out.println("Is it small? (y/n)");
        String smallYN = scanner.nextLine();
        boolean isSmall = "y".equalsIgnoreCase(smallYN);
        return new Parrot(name, species, isSmall);
    }

    private static Cat createCat(Scanner scanner, String name) {
        System.out.println("Enter animal breed: ");
        String breed = scanner.nextLine();
        System.out.println("Enter animal coat: ");
        String coat = scanner.nextLine();
        System.out.println("Enter animal age (years): ");
        int age = Integer.parseInt(scanner.nextLine());
        return new Cat(name, breed, coat, age);
    }

    private static Dog createDog(Scanner scanner, String name) {
        System.out.println("Enter animal weight: ");
        int animalWeight = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter animal color: ");
        String animalColor = scanner.nextLine();
        System.out.println("Enter animal age (years): ");
        int age = Integer.parseInt(scanner.nextLine());
        return new Dog(name, animalWeight, animalColor, age);
    }

    private static String readLookupName(String message) {
        Scanner screenInput = new Scanner(System.in);
        System.out.println(message);
        String lookUpName = screenInput.nextLine();
        System.out.println();
        return lookUpName;
    }
}
