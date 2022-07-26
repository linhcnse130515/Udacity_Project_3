package com.udacity.jdnd.course3.critter.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "customer")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String phoneNumber;

    private String notes;

    @OneToMany(mappedBy = "customer",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Pet> pets;

    public void addPet(Pet pet) {
        if (pets == null) {
            pets = new ArrayList<>();
        }
        pets.add(pet);
    }

}
