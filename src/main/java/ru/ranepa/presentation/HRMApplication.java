package ru.ranepa.presentation;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;
import ru.ranepa.repository.EmployeeRepositoryImpl;
import ru.ranepa.service.EmployeeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class HRMApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EmployeeService employeeService;
    private static final EmployeeRepositoryImpl repository = new EmployeeRepositoryImpl();
    static {
        employeeService = new EmployeeService(repository);
    }

    public static void main(String[] args){
        while (true){
            printMenu();

            int variant = readIntInput("Select a variant: ");
            switch (variant) {
                case 1:
                    showAllEmployees();
                    break;
                case 2:
                    addEmployee();
                    break;
                case 3:
                    deleteEmployee();
                    break;
                case 4:
                    findEmployeeById();
                    break;
                case 5:
                    showStatistics();
                    break;
                case 6:
                    System.out.println("Saving data...");
                    repository.saveToFile("employees.csv");  // автосохранение
                    System.out.println("bye-bye!");
                    return;
                case 7:
                    sortBynameMenu();
                    break;
                case 8:
                    sortByHireDateMenu();
                    break;
                default:
                    System.out.println("Invalid variant. Select a variant from 1 to 8 again.");
            }
        }
    }

    private static int readIntInput(String variant) {
        while(true){
            System.out.println(variant);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e){
                System.out.println("Please select a variant");
            }
        }
    }

    private static void showAllEmployees() {
        Iterable<Employee> employees = repository.findAll();

        boolean found = false;
        for (Employee employee : employees){
            System.out.println(employee);
            found = true;
        }

        if (!found){
            System.out.println("No employees found");
        }
    }

    private static void addEmployee() {
        System.out.println("\nAdd new employee");

        System.out.print("FIO: ");
        String name = scanner.nextLine();
        if (name == null || name.trim().isEmpty()) {
            System.out.println("FIO cannot be empty");
            return;
        }
        name = name.trim();

        System.out.print("Position: ");
        String position = scanner.nextLine();
        if (position == null || position.trim().isEmpty()) {
            System.out.println("Position cannot be empty");
            return;
        }
        position = position.trim();

        System.out.print("Salary: ");
        double salary;
        try {
            salary = Double.parseDouble(scanner.nextLine());
            if (salary <= 0) {
                System.out.println("Salary should be positive");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid salary. Enter a number");
            return;
        }

        System.out.print("Hire date (YYYY-MM-DD): ");
        LocalDate hireDate;
        try {
            hireDate = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid hire date. Use YYYY-MM-DD");
            return;
        }

        Employee newEmployee = new Employee(name, position, salary, hireDate);
        String result = repository.save(newEmployee);
        System.out.println(result);
    }

    private static void deleteEmployee() {
        Long id = readLongInput("Employee ID to delete: ");
        String result = repository.delete(id);
        System.out.println(result);
    }

    private static long readLongInput(String ID) {
        while(true){
            System.out.println(ID);
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e){
                System.out.println("Enter a valid number");
            }
        }
    }

    private static void findEmployeeById() {
        Long id = readLongInput("Employee ID to find: ");
        Optional<Employee> empl = repository.findById(id);

        if (empl.isPresent()) {
            System.out.println(empl.get());
        } else {
            System.out.println("Employee " + id + " not found");
        }
    }

    private static void showStatistics() {
        BigDecimal avgSalary = employeeService.calculateAverageSalary();
        System.out.println("Average salary: " + avgSalary);

        Optional<Employee> topEmployee = employeeService.findTopSalaryEmployee();
        if (topEmployee.isPresent()) {
            Employee topEmpl = topEmployee.get();
            System.out.println("Top earner: " + topEmpl.getName() + " - " + topEmpl.getSalary());
        } else {
            System.out.println("No employees found");
        }
    }

    private static void sortByHireDateMenu() {
        List<Employee> sorted = employeeService.sortByHireDate();
        printEmployeesList(sorted);
    }

    private static void sortBynameMenu() {
        List<Employee> sorted = employeeService.sortByName();
        printEmployeesList(sorted);
    }

    // Вспомогательный метод для вывода
    private static void printEmployeesList(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employees found");
            return;
        }
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private static void printMenu() {
        System.out.println("\nSystem Menu:");
        System.out.println("1. Show all employees");
        System.out.println("2. Add employee");
        System.out.println("3. Delete employee");
        System.out.println("4. Find employee by ID");
        System.out.println("5. Show statistics");
        System.out.println("6. Exit");
        System.out.println("7. Sort by name");
        System.out.println("8. Sort by hire date");
    }
}
