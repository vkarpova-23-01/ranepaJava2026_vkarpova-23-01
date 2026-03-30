package ru.ranepa.repository;

import ru.ranepa.model.Employee;

import java.util.Optional;

//Alt + Enter -> Implement interface
public interface EmployeeRepository {
    String save(Employee employee);
    Optional<Employee> findById(Long id);
    Iterable<Employee> findAll();
    String delete(Long id);
    void saveToFile(String filename);
}
