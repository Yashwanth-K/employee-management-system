package com.yash.employeemanagement.repository;

import com.yash.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long>, PagingAndSortingRepository<Employee,Long> {
    //for custom query methods, you can define them here
    List<Employee> findByNameContainingIgnoreCase(String name); // partial search
    List<Employee> findByDepartmentIgnoringCase(String department); // exact search
    List<Employee> findByEmailIgnoringCase(String email); // exact search

}
