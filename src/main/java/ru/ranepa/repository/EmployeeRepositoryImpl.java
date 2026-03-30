package ru.ranepa.repository;

import ru.ranepa.model.Employee;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    private Map<Long, Employee> employees = new HashMap<>();
    private static Long nextId = 1L;

    @Override
    public String save(Employee employee) {
        employee.setId(nextId++);
        employees.put(employee.getId(), employee);
        return "Employee " + employee.getId() + " was saved successfully";
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(employees.get(id));
    } // Optional.ofNullable ЧТОБЫ НЕ БЫЛО ИСКЛЮЧЕНИЕМ, ПРОГРАММА РАБОТАЛА ДАЛЬШЕ

    @Override
    public Iterable<Employee> findAll() {
        return employees.values();
    } // employees.values() - ВЫВОДИТ КОЛЛЕКЦИЮ

    @Override
    public String delete(Long id) {
        if (employees.containsKey(id)) {
            employees.remove(id);
            return "Employee " + id + " was deleted";
        }
        else {
            return "Employee " + id + " not found";
        }
    } // ПРОВЕРЯЕМ, ЕСТЬ ЛИ СОТРУДНИК. УДАЛЯЕМ, ЕСЛИ ЕСТЬ. ЕСЛИ НЕТ, ВЫВОДИМ, ЧТО НЕТ ТАКОГО

    @Override
    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Заголовок для CSV
            writer.println("id,name,position,salary,hireDate");

            for (Employee employee : employees.values()) {
                writer.printf("%d,%s,%s,%s,%s%n",
                        employee.getId(),
                        employee.getName(),
                        employee.getPosition(),
                        employee.getSalary(),
                        employee.getHireDate()
                );
            }
            System.out.println("✓ Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("✗ Error saving file: " + e.getMessage());
        }
    }
}
