package repository;

import model.Clinic;
import model.Treatment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DefaultTreatmentRepository implements TreatmentRepository {
    private final Clinic clinic;

    public DefaultTreatmentRepository() throws FileNotFoundException {
        clinic = new Clinic();
        readTreatmentsFromFile();
    }

    private void readTreatmentsFromFile() throws FileNotFoundException {
        Scanner input = new Scanner(new File("tratament.txt"));
        while (input.hasNextLine()){
            String line = input.nextLine();
            String[] fields = line.split(",");
            Treatment t = new Treatment(fields[0], Double.parseDouble(fields[1]));
            if (!isTreatmentAlreadyInList(t)){
                clinic.addTreatment(t);
            }
        }
        input.close();
    }

    @Override
    public boolean isTreatmentAlreadyInList(Treatment t) {
        boolean found = false;

        if (clinic == null || clinic.getTreatments()==null){
            return found;
        }

        for (Treatment recordedTreatment: clinic.getTreatments()){
            if (recordedTreatment.equals(t)){
                found = true;
                break;
            }
        }
        return found;
    }

    @Override
    public Treatment getTreatment(String treatment) {
        Treatment[] treatments = clinic.getTreatments();
        for (Treatment recordedTreatment: treatments){
            if (recordedTreatment.description().equalsIgnoreCase(treatment)){
                return recordedTreatment;
            }
        }
        return null;
    }
}
