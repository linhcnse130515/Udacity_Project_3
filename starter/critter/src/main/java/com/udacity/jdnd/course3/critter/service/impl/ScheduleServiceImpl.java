package com.udacity.jdnd.course3.critter.service.impl;

import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    private final PetRepository petRepository;

    private final EmployeeRepository employeeRepository;

    private final CustomerRepository customerRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
                               PetRepository petRepository,
                               EmployeeRepository employeeRepository,
                               CustomerRepository customerRepository) {
        this.scheduleRepository = scheduleRepository;
        this.petRepository = petRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public ScheduleDTO save(ScheduleDTO scheduleDTO) {
        Schedule schedule = this.convertScheduleDTOToEntity(scheduleDTO);
        schedule = scheduleRepository.save(schedule);
        return this.convertEntityToScheduleDTO(schedule);
    }

    @Override
    public List<ScheduleDTO> getSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        for (Schedule schedule : schedules) {
            scheduleDTOs.add(convertEntityToScheduleDTO(schedule));
        }
        return scheduleDTOs;
    }

    @Override
    public List<ScheduleDTO> getSchedulesByPet(Long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if (optionalPet.isPresent()) {
            List<Schedule> schedules = scheduleRepository.findAllByPetsContaining(optionalPet.get());
            List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
            for (Schedule schedule : schedules) {
                scheduleDTOs.add(this.convertEntityToScheduleDTO(schedule));
            }
            return scheduleDTOs;
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public List<ScheduleDTO> getSchedulesByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(EntityNotFoundException::new);
        List<Schedule> schedules = scheduleRepository.findAllByEmployeesContaining(employee);
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        for (Schedule schedule : schedules) {
            scheduleDTOs.add(this.convertEntityToScheduleDTO(schedule));
        }
        return scheduleDTOs;
    }

    @Override
    public List<ScheduleDTO> getSchedulesByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(EntityNotFoundException::new);
        List<Pet> pets = customer.getPets();
        List<Schedule> schedules = scheduleRepository.findAllByPetsIn(pets);
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        for (Schedule schedule : schedules) {
            scheduleDTOs.add(this.convertEntityToScheduleDTO(schedule));
        }
        return scheduleDTOs;
    }

    private ScheduleDTO convertEntityToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        if (Objects.nonNull(schedule.getPets())) {
            List<Long> pets = schedule.getPets()
                    .stream()
                    .map(Pet::getId)
                    .collect(Collectors.toList());
            scheduleDTO.setPetIds(pets);
        }
        if (Objects.nonNull(schedule.getEmployees())) {
            List<Long> employees = schedule.getEmployees()
                    .stream()
                    .map(Employee::getId)
                    .collect(Collectors.toList());
            scheduleDTO.setEmployeeIds(employees);
        }
        return scheduleDTO;
    }

    private Schedule convertScheduleDTOToEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        if (Objects.nonNull(scheduleDTO.getPetIds())) {
            Set<Pet> pets = new HashSet<>();
            scheduleDTO.getPetIds()
                    .forEach(
                            petId -> pets.add(
                                    petRepository.findById(petId)
                                            .orElseThrow(EntityNotFoundException::new)
                            )
                    );
            schedule.setPets(pets);
        }
        if (Objects.nonNull(scheduleDTO.getEmployeeIds())) {
            Set<Employee> employees = new HashSet<>();
            scheduleDTO.getEmployeeIds()
                    .forEach(
                            employeeId -> employees.add(
                                    employeeRepository.findById(employeeId)
                                            .orElseThrow(EntityNotFoundException::new)
                            )
                    );
            schedule.setEmployees(employees);
        }
        return schedule;
    }
}
