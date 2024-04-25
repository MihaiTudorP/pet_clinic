package service;

import model.Animal;
import model.Owner;
import model.Treatment;
import repository.DefaultOwnerRepository;
import repository.OwnerRepository;
import repository.TreatmentRepository;

public class ClinicService {
    OwnerRepository ownerRepository;
    TreatmentRepository treatmentRepository;

    public ClinicService(DefaultOwnerRepository ownerRepository, TreatmentRepository treatmentRepository) {
        this.ownerRepository = ownerRepository;
        this.treatmentRepository = treatmentRepository;
    }

    public void addOwner(Owner owner) {
        ownerRepository.addOwner(owner);
    }

    public void addPatient(Owner owner, Animal patient){
        if (patient == null) {
            System.out.println("Invalid animal species");
            return;
        }

        ownerRepository.addAnimal(owner, patient);
    }

    public void addPatientTreatment(Owner owner, Animal patient, String treatment){
        Owner storedOwner = ownerRepository.getOwner(owner.getName());
        Treatment storedTreatment = treatmentRepository.getTreatment(treatment);
        ownerRepository.addAnimalTreatment(storedOwner, patient,storedTreatment);
    }

    public Owner getOwner(String ownerName) {
        return ownerRepository.getOwner(ownerName);
    }

    public Animal getPatient(Owner owner, String patientName) {
        return ownerRepository.getAnimal(owner, patientName);

    }
}
