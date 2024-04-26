package repository;

import model.Animal;
import model.Owner;
import model.Treatment;

import java.util.*;

public class DefaultOwnerRepository implements OwnerRepository {
    private Set<Owner> owners;

    public void addOwner(Owner owner) {
        if (owners == null){
            owners = new HashSet<>();
        }

        owners.add(owner);
    }

    public Owner getOwner(String name) {
        return owners.stream().filter(owner -> owner.getName().equals(name)).findFirst().orElse(null);
    }

    public void addAnimal(Owner owner, Animal animal) {
        owners.stream().filter(o -> o.getName().equals(owner.getName())).findFirst().ifPresent(o -> o.addAnimal(animal));
    }

    public Animal getAnimal(Owner owner, Animal patient) {
        for (Animal animal : owner.getAnimals()) {
            if (animal.equals(patient)) {
                return animal;
            }
        }

        Owner retrieved = owners.
                stream()
                .filter(o -> o.getName().equals(owner.getName())).findFirst()
                .orElseThrow();

        Animal retrievedPatient = retrieved.getAnimals().stream().filter(p -> p.equals(patient)).findFirst().orElse(null);
        return retrievedPatient;
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

    public List<Animal> getAnimals() {
        List<Animal> animals = new ArrayList<>();
        for (Owner owner : owners) {
            animals.addAll(owner.getAnimals());
        }
        return animals;
    }
}
