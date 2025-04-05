package com.yash.employeemanagement.service;

import com.yash.employeemanagement.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Page<Employee> getAllEmployees(Pageable pageable);
    Employee getEmployeeById(Long id);
    Employee saveEmployee(Employee employee);
    Employee updateEmployee(Long id, Employee employee);
    void deleteEmployee(Long id);
    List<Employee> getEmployeesByName(String name);
    List<Employee> getEmployeesByDepartment(String department);
    List<Employee> getEmployeesByEmail(String email);
    List<Employee> getEmployeesByCriteria(String name, String department, String email);
}
