package repository;

import model.Treatment;

public interface TreatmentRepository {
    boolean isTreatmentAlreadyInList(Treatment t);

    Treatment getTreatment(String treatment);
}
