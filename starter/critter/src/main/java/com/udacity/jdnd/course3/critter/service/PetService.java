package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.dto.PetDTO;

import java.util.List;

public interface PetService {
    PetDTO save(PetDTO petDTO);
    PetDTO getPetById(Long id);
    List<PetDTO> getPets();
    List<PetDTO> getPetsByOwnerId(Long ownerId);
}
