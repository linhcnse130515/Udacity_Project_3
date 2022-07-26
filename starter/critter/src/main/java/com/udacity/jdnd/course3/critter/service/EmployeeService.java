package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface EmployeeService {
    EmployeeDTO save(EmployeeDTO employeeDTO);
    EmployeeDTO getEmployeeById(Long id);
    void setEmployeeAvailability(Long id, Set<DayOfWeek> daysAvailable);
    List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeRequestDTO);
}
