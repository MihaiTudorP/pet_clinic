package model;

public class Cat extends Animal {
    private String breed;
    private String coat;
    private int ageInYears;

    public Cat(String name, String breed, String coat, int ageInYears) {
        super(name);
        this.breed = breed;
        this.coat = coat;
        this.ageInYears = ageInYears;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getCoat() {
        return coat;
    }

    public void setCoat(String coat) {
        this.coat = coat;
    }

    public int getAgeInYears() {
        return ageInYears;
    }

    public void setAgeInYears(int ageInYears) {
        this.ageInYears = ageInYears;
    }

    @Override
    public double calculateBillTotal() {
        double total = 0;
        for (Treatment t: treatments) {
            total += t.price() * 1.9;
        }
        return total;
    }

    @Override
    public String toString() {
       return "Name: " + getName() + "\n" +
               "Breed: " + getBreed() + "\n" +
               "Coat: " + getCoat() + "\n" +
               "Age: " + getAgeInYears() + " years\n";
    }
}
