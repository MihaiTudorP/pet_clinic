package model;

public class Dog extends Animal{
    private double weight;
    private String color;
    private int ageInYears;

    public Dog(String name, double weight, String color, int ageInYears) {
        super(name);
        this.weight = weight;
        this.color = color;
        this.ageInYears = ageInYears;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
            total += t.price() * 1.9 + ageInYears * weight * t.price()/10;
        }
        return total;
    }

    @Override
    public String toString() {
        return "Name: " + getName() + "\n" +
                "Color: " + getColor() + "\n" +
                "Age(Years): " + getAgeInYears() + "years\n" +
                "Weight: " + weight;
    }
}
