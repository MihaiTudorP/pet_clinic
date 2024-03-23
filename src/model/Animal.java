package model;

import java.util.Arrays;

public abstract class Animal {
    private String name;

    protected Treatment[] treatments;

    protected Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTreatment(Treatment treatment){
        if (treatments == null){
            treatments = new Treatment[1];
            treatments[0] = treatment;
        } else {
            for (Treatment t : treatments){
                if (t.equals(treatment)) return;
            }
            treatments = Arrays.copyOf(treatments, treatments.length + 1);
            treatments[treatments.length - 1] = treatment;
        }
    }

    public abstract double calculateBillTotal();

    private String showListOfTreatments(){
         if (treatments == null || treatments.length == 0) return "";
         else {
             StringBuilder result = new StringBuilder();
             result.append("\nTratamente efectuate:\n");
             for (Treatment t : treatments){
                 result.append(t.toString()).append("\n");
             }
             return result.toString();
         }
    }

    public void displayAllDetailsToConsole(){
        System.out.println(this + "\n" +
                showListOfTreatments() + "\n" +
                "Total: " + calculateBillTotal());
    }

    @Override
    public abstract String toString();
}
