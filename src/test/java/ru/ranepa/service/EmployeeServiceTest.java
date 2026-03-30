package ru.ranepa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepositoryImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeServiceTest {
    private EmployeeService employeeService;
    private EmployeeRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new EmployeeRepositoryImpl();
        employeeService = new EmployeeService(repository);
    }

    @Test
    void shouldCalculateAverageSalary() {
        repository.save(new Employee("Ivanov Ivan Ivanovich", "Developer", 10000, LocalDate.now()));
        repository.save(new Employee("Mashkina Maria Maksimovna", "Manager", 20000, LocalDate.now()));
        repository.save(new Employee("Petrov Petr Petrovich", "QA", 30000, LocalDate.now()));

        BigDecimal avgSalary = employeeService.calculateAverageSalary();

        assertEquals(BigDecimal.valueOf(20000), avgSalary.setScale(0, RoundingMode.HALF_UP));
    }

    @Test
    void shouldFindTopSalaryEmployee() {
        // Подготовка
        repository.save(new Employee("Ivanov Ivan Ivanovich", "Developer", 10000, LocalDate.now()));
        repository.save(new Employee("Mashkina Maria Maksimovna", "Manager", 30000, LocalDate.now()));
        repository.save(new Employee("Petrov Petr Petrovich", "QA", 20000, LocalDate.now()));

        // Действие
        Optional<Employee> topEmpl = employeeService.findTopSalaryEmployee();

        // Проверка
        assertTrue(topEmpl.isPresent());
        assertEquals("Mashkina Maria Maksimovna", topEmpl.get().getName());
        assertEquals(BigDecimal.valueOf(30000), topEmpl.get().getSalary().setScale(0, RoundingMode.HALF_UP));
    }

    @Test
    void shouldReturnEmptyWhenNoEmployees() {
        // Действие
        Optional<Employee> topEmpl = employeeService.findTopSalaryEmployee();

        // Проверка
        assertTrue(topEmpl.isEmpty());
    }
}
