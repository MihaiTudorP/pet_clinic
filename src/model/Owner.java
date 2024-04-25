package model;

import java.util.*;
import java.util.stream.Collectors;

public class Owner {
    private final String name;
    private final Address address;
    private Set<Animal> animals;

    public Owner(String name, Address address) {
        this.name = name;
        this.address = address;
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
        return Objects.hash(name, address, animals);
    }

    public Set<Animal> getAnimals() {
        List<Animal> animals = new ArrayList<>();
        Collections.copy(animals, animals.stream().collect(Collectors.toList()));
        return animals.stream().collect(Collectors.toSet());
    }

    public void addAnimal(Animal animal) {
       if (animals == null){
           animals = new HashSet<>();
       }
       animals.add(animal);
    }

    @Override
    public String toString() {
        return "Owner{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", animals=" + animals +
                '}';
    }
}
