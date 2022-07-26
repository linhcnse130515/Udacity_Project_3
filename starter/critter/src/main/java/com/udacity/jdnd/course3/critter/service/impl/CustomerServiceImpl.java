package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    private final PetRepository petRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = this.convertCustomerDTOToEntity(customerDTO);
        customer = customerRepository.save(customer);
        return this.convertEntityToCustomerDTO(customer);
    }

    @Override
    public List<CustomerDTO> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        for (Customer customer : customers) {
            customerDTOs.add(this.convertEntityToCustomerDTO(customer));
        }
        return customerDTOs;
    }

    @Override
    public CustomerDTO getCustomerByPet(Long petId) {
        // find pet by id
        Pet pet = petRepository.findById(petId).orElseThrow(EntityNotFoundException::new);
        // find customer by id
        Customer customer = customerRepository.findById(pet.getCustomer().getId())
                .orElseThrow(EntityNotFoundException::new);
        return this.convertEntityToCustomerDTO(customer);
    }

    private CustomerDTO convertEntityToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        if (Objects.nonNull(customer.getPets())) {
            customerDTO.setPetIds(
                    customer.getPets()
                            .stream()
                            .map(Pet::getId)
                            .collect(Collectors.toList())
            );
        }
        return customerDTO;
    }

    private Customer convertCustomerDTOToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        if (Objects.nonNull(customerDTO.getPetIds())) {
            List<Long> petIdLst = customerDTO.getPetIds();
            List<Pet> pets = petRepository.findAllByIdIn(petIdLst);
            customer.setPets(pets);
        }
        return customer;
    }
}
