package com.yash.employeemanagement.service;

import com.yash.employeemanagement.entity.Employee;
import com.yash.employeemanagement.exception.ResourceNotFoundException;
import com.yash.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Cacheable(value = "employees")
    @Override
    public Page<Employee> getAllEmployees(Pageable pageable){
        Page<Employee> employees =  employeeRepository.findAll(pageable);
        if (employees.isEmpty()){
            throw new ResourceNotFoundException("No employees found");
        }
        return employees;
    }
    @Cacheable(value="employee", key="#id")
    @Override
    public Employee getEmployeeById(Long id){
        return employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee id "+id+" not found"));
    }

    @Override
    public List<Employee> getEmployeesByName(String name) {
        List<Employee> employees = employeeRepository.findByNameContainingIgnoreCase(name);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found with name: " + name);
        }
        return employees;
    }

    @Override
    public List<Employee> getEmployeesByDepartment(String department) {
        List<Employee> employees = employeeRepository.findByDepartmentIgnoringCase(department);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found in department: " + department);
        }
        return employees;
    }

    @Override
    public List<Employee> getEmployeesByEmail(String email) {
        List<Employee> employees = employeeRepository.findByEmailIgnoringCase(email);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found with email: " + email);
        }
        return employees;
    }

    @Cacheable(value = "employees", key = "#name + #department + #email")
    //write function for filtering all employees by name, department and email
    @Override
    public List<Employee> getEmployeesByCriteria(String name, String department, String email) {
        List<Employee> employees = employeeRepository.findAll();
        //This retrieves all employees from the DB.

//        Converts the list into a Stream for functional-style operations.
        return employees.stream()
                .filter(employee -> (name == null || employee.getName().equalsIgnoreCase(name)) &&
                        (department == null || employee.getDepartment().equalsIgnoreCase(department)) &&
                        (email == null || employee.getEmail().equalsIgnoreCase(email)))
                .toList();
        //if all are null, it will return all employees else will check by all criteria

    }

    @CachePut(value = "employee", key = "#id")
    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        if(employeeRepository.existsById(id)){
            employee.setId(id);
            return employeeRepository.save(employee);
            //The `save(employee)` method overwrites the old record in
            // the database with new details, as the primary key (`id`) remains the same.
        }
        else {
            throw new RuntimeException("Employee not found with id"+id);
        }
    }
    @CachePut(value="employee", key="#employee.id")
    @Override
    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    @CacheEvict(value = "employee", key = "#id")
    @Override
    public void deleteEmployee(Long id){
        employeeRepository.deleteById(id);
    }
}



