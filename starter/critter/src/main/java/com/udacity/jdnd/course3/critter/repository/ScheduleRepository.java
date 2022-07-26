package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    //Optional<Schedule> findByActivitiesContaining_activitiesAndPetsContaining_petsAndEmployeesContaining_employeesAndDateEquals_date(Set<EmployeeSkill> activities, Set<Pet> pets, Set<Employee> employees, LocalDate date);
    List<Schedule> findAllByEmployeesContaining(Employee employee);
    List<Schedule> findAllByPetsContaining(Pet pet);
    List<Schedule> findAllByPetsIn(List<Pet> pets);
}
