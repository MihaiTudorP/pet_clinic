package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Animal implements Serializable {
    private String name;
    private String species;
    private int age;

    private String gender;
    private String race;

    public Animal(String name, String species, int age, String gender, String race) {
        this.name = name;
        this.species = species;
        this.age = age;
        this.gender = gender;
        this.race = race;
    }

    protected Treatment[] treatments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Treatment[] getTreatments() {
        return treatments;
    }

    public void setTreatments(Treatment[] treatments) {
        this.treatments = treatments;
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

    public double calculateBillTotal(){
        double total = 0;
        for (Treatment treatment : treatments){
            total += treatment.price();
        }
        return total;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return age == animal.age && Objects.equals(name, animal.name) && Objects.equals(gender, animal.gender) && Objects.equals(species, animal.species) && Objects.equals(race, animal.race) && Objects.deepEquals(treatments, animal.treatments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, gender, species, race, Arrays.hashCode(treatments));
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", species='" + species + '\'' +
                ", race='" + race + '\'' +
                ", treatments=" + Arrays.toString(treatments) +
                '}';
    }
}
