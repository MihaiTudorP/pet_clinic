package repository;

import model.Animal;
import model.Owner;
import model.Treatment;

import java.util.Arrays;

public class OwnerRepository {
    private Owner[] owners;

    public void addOwner(Owner owner) {
        if (owners == null){
            owners = new Owner[1];
        } else {
            owners = Arrays.copyOf(owners, owners.length + 1);
        }

        owners[owners.length - 1] = owner;
    }

    public Owner getOwner(String name) {
        for (Owner owner : owners) {
            if (owner.getName().equals(name)) {
                return owner;
            }
        }
        return null;
    }

    public void addAnimal(Owner owner, Animal animal) {
        for(Owner own : owners) {
            if (own.equals(owner)) {
                own.addAnimal(animal);
            }
        }
    }

    public Animal getAnimal(Owner owner, Animal patient) {
        for (Animal animal : owner.getAnimals()) {
            if (animal.equals(patient)) {
                return animal;
            }
        }
        return null;
    }

    public void addAnimalTreatment(Owner owner, Animal patient, Treatment treatment) {
        Owner own = getOwner(owner.getName());

        if (own != null) {
            Animal animal = getAnimal(owner, patient);
            if (animal != null) {animal.addTreatment(treatment);}
        }
    }

    public Animal getAnimal(Owner owner, String patientName) {
        for (Animal animal : owner.getAnimals()) {
            if (animal.getName().equals(patientName)) {
                return animal;
            }
        }
        return null;
    }
}
