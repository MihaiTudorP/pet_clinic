package model;

public class Parrot extends Animal{

    private String species;
    private boolean small;

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public boolean isSmall() {
        return small;
    }

    public void setSmall(boolean small) {
        this.small = small;
    }

    public Parrot(String name, String species, boolean small) {
        super(name);
        this.species = species;
        this.small = small;
    }

    @Override
    public double calculateBillTotal() {
        double total = 0;
        for (Treatment t: treatments) {
            total += t.price() * 1.9 + (small? t.price()/10 : t.price() * 100);
        }
        return total;
    }

    @Override
    public String toString() {
        return "Nume: " + getName() + "\n" +
                "Specie: " + species + "\n" +
                "Mic: " + (isSmall()? "da": "nu");
    }
}
