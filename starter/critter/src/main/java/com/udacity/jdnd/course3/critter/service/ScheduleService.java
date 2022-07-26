package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;

import java.util.List;

public interface ScheduleService {
    ScheduleDTO save(ScheduleDTO scheduleDTO);

    List<ScheduleDTO> getSchedulesByCustomer(Long customerId);

    List<ScheduleDTO> getSchedulesByEmployee(Long employeeId);

    List<ScheduleDTO> getSchedulesByPet(Long petId);

    List<ScheduleDTO> getSchedules();
}
