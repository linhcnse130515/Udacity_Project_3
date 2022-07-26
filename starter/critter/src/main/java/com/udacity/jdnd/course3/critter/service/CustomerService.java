package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;

import java.util.List;

public interface CustomerService {
    CustomerDTO save(CustomerDTO customerDTO);
    List<CustomerDTO> getCustomers();
    CustomerDTO getCustomerByPet(Long petId);
}
