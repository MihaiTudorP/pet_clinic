package model;

import java.util.Arrays;
import java.util.Objects;

public class Owner {
    private final String name;
    private final Address address;
    private Animal[] animals;

    public Owner(String name, Address address, Animal[] animals) {
        this.name = name;
        this.address = address;
        this.animals = animals == null? new Animal[0] : animals;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return Objects.equals(name, owner.name) && Objects.equals(address, owner.address) && Objects.deepEquals(animals, owner.animals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, Arrays.hashCode(animals));
    }

    public Animal[] getAnimals() {
        return animals;
    }

    public void addAnimal(Animal animal) {
        animals = Arrays.copyOf(animals, animals.length + 1);
        animals[animals.length - 1] = animal;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", animals=" + Arrays.toString(animals) +
                '}';
    }
}
