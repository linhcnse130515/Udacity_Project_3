package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.dto.PetDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;

    private final CustomerRepository customerRepository;

    public PetServiceImpl(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public PetDTO save(PetDTO petDTO) {
        Pet pet = convertPetDTOToEntity(petDTO);
        pet = petRepository.save(pet);
        Customer customer = pet.getCustomer();
        if (Objects.nonNull(customer)) {
            customer.addPet(pet);
            customerRepository.save(customer);
        }
        return this.convertEntityToPetDTO(pet);
    }

    public PetDTO getPetById(Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return this.convertEntityToPetDTO(pet);
    }

    public List<PetDTO> getPets() {
        List<Pet> pets = petRepository.findAll();
        List<PetDTO> petDTOs = new ArrayList<>();
        for (Pet pet : pets) {
            petDTOs.add(this.convertEntityToPetDTO(pet));
        }
        return petDTOs;
    }

    public List<PetDTO> getPetsByOwnerId(Long ownerId) {
        boolean isExisted = customerRepository.existsById(ownerId);
        if (!isExisted) {
            throw new NullPointerException();
        }
        List<Pet> pets = petRepository.findAllByCustomer_Id(ownerId);
        List<PetDTO> petDTOs = new ArrayList<>();
        for (Pet pet : pets) {
            petDTOs.add(convertEntityToPetDTO(pet));
        }
        return petDTOs;
    }


    private PetDTO convertEntityToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if (Objects.nonNull(pet.getCustomer())) {
            petDTO.setOwnerId(pet.getCustomer().getId());
        }
        return petDTO;
    }

    private Pet convertPetDTOToEntity(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        if (petDTO.getOwnerId() != 0) {
            Customer customer = customerRepository.findById(petDTO.getOwnerId())
                    .orElseThrow(EntityNotFoundException::new);
            pet.setCustomer(customer);
        }
        return pet;
    }
}
