package ru.ranepa.service;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class EmployeeService {
    private final EmployeeRepository employeeRepository; // final - гарантирует, что поле не изменится после создания

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }
    // добавила инициализацию, чтобы не передавался пустым, а с объектом employeeRepository
    // Сервис не создаёт репозиторий сам, а получает его

    public BigDecimal calculateAverageSalary() {
        Iterable<Employee> allEmployees = employeeRepository.findAll();
        // перейти из Iterable во что-то другое (в другую коллекцию), чтобы дальше работать

        BigDecimal sumSalary = BigDecimal.ZERO; // сумма = 0
        int count = 0; // количество с нуля
        for (Employee employee : allEmployees){
            sumSalary = sumSalary.add(employee.getSalary());
            count++;
        } // цикл неявно создаёт один итератор и использует его корректно
        if (count == 0){
            return BigDecimal.ZERO;
        }

        return sumSalary.divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP);
    }

    public Optional<Employee> findTopSalaryEmployee() {
        Iterable<Employee> allEmployees = employeeRepository.findAll();
        Employee topEmployee = null;
        BigDecimal maxSalary = BigDecimal.valueOf(-1);

        for (Employee employee : allEmployees){
            if (employee.getSalary().compareTo(maxSalary) >0){
                maxSalary = employee.getSalary();
                topEmployee = employee;
            }
        }
        return Optional.ofNullable(topEmployee);
    }

    public List<Employee> filterByPosition(String position){
        Iterable<Employee> allEmployees = employeeRepository.findAll();
        List<Employee> result = new ArrayList<>();

        for (Employee employee : allEmployees){
            if (employee.getPosition().equalsIgnoreCase(position)){
                result.add(employee);
            }
        }

        return result;
    }


    public List<Employee> sortByName() {
        return StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Employee::getName))
                .toList();
    }

    public List<Employee> sortByHireDate() {
        return StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Employee::getHireDate))
                .toList();
    }
}
