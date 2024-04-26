package model;

import java.util.Arrays;

public class Clinic {
    private Treatment[] treatments;

    public Treatment[] getTreatments() {
        return treatments;
    }

    public void setTreatments(Treatment[] treatments) {
        this.treatments = treatments;
    }

    public void addTreatment(Treatment t) {
        if (treatments == null) {
            treatments = new Treatment[1];
        } else {
            treatments = Arrays.copyOf(treatments, treatments.length + 1);
        }
        treatments[treatments.length - 1] = t;
    }
}
