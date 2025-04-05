package com.yash.employeemanagement.controller;

import com.yash.employeemanagement.dto.ApiResponse;
import com.yash.employeemanagement.entity.Employee;
import com.yash.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Employee>>> getAllEmployees(
            //add request param
            //create sort obj and then pagable object,get paginated employee,return response
            @RequestParam(defaultValue ="0") int page,
            @RequestParam(defaultValue = "10")int size,
            @RequestParam(defaultValue = "id")String sortBy,
            @RequestParam(defaultValue = "asc")String sortDir)
    {
        Sort sort = sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Employee> employeePage = employeeService.getAllEmployees(pageable);

        return ResponseEntity.ok(new ApiResponse<>(true,"employee retreived",employeePage));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Employee>> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee retrieved successfully", employee));
    }
    // Example: /employees/filter?name=John (only one query param allowed)
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<Employee>>> getEmployeesByFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String email) {
        List<Employee> employees;
        if (name != null) {
            employees = employeeService.getEmployeesByName(name);
        } else if (department != null) {
            employees = employeeService.getEmployeesByDepartment(department);
        } else if (email != null) {
            employees = employeeService.getEmployeesByEmail(email);
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Invalid filter parameters", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Employees retrieved successfully", employees));
    }

    @GetMapping("/filter/all")
    public ResponseEntity<ApiResponse<List<Employee>>> getEmployeesByCriteria(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String email) {
        List<Employee> employees = employeeService.getEmployeesByCriteria(name, department, email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employees retrieved successfully", employees));
    }
    @PostMapping
    public ResponseEntity<ApiResponse<Employee>> saveEmployee(@Valid @RequestBody Employee employee) {
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return new ResponseEntity<>(new ApiResponse<>(true, "Employee saved successfully", savedEmployee), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Employee>> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee updated successfully", updatedEmployee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee with ID " + id + " has been deleted", null));
    }
}
