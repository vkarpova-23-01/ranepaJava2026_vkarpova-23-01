package ru.ranepa;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepositoryImpl;

import java.time.LocalDate;

public class HrmApplication {
    public static void main(String[] args) {
        Employee emp = new Employee(
                "Sidorov Alex Evgen'evich",
                "java developer",
                30_000.0,
                LocalDate.of(2026, 3, 1)

        );

        //sout -> System.out.println()
        System.out.println(emp.getSalary());

        EmployeeRepositoryImpl employeeRepository = new EmployeeRepositoryImpl();
        System.out.println("==============");
        System.out.println(employeeRepository.save(emp));
        System.out.println("==============");
        var emp1 = employeeRepository.findById(1L)
                .orElseThrow();
        System.out.println("Employee was found: " + emp1);

    }
}