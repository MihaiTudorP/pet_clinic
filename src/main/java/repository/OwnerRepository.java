package repository;

import model.Animal;
import model.Owner;
import model.Treatment;

public interface OwnerRepository {
    void addOwner(Owner owner);

    void addAnimal(Owner owner, Animal patient);

    Owner getOwner(String name);

    void addAnimalTreatment(Owner storedOwner, Animal patient, Treatment storedTreatment);

    Animal getAnimal(Owner owner, String patientName);
}
