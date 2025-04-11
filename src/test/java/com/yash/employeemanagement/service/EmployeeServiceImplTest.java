package com.yash.employeemanagement.service;

import com.yash.employeemanagement.entity.Employee;
import com.yash.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceImplTest {

    @Mock
    // mocks the EmployeeRepository, mockito will simulate the behavior of this class
    private EmployeeRepository employeeRepository;

    // injects the mock EmployeeRepository into the EmployeeServiceImpl instance
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    //this method runs before each test and initializes the mock obj
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployees_ReturnsEmployeeList() {
        //mock data, simulating the data returned from the repository
        List<Employee> mockList = Arrays.asList(
                new Employee(1L, "Yash", "yash@example.com", "IT", LocalDateTime.now(), LocalDateTime.now()),
                new Employee(2L, "John", "john@example.com", "HR",LocalDateTime.now(), LocalDateTime.now())
        );
        Page<Employee> mockPage = new PageImpl<>(mockList);
        // Create a Pageable object
        Pageable pageable = PageRequest.of(0, 10);

        //configures the mock repository to return mockList when findAll() is called
        when(employeeRepository.findAll(pageable)).thenReturn(mockPage);
        //calls the method under test
        Page<Employee> result = employeeService.getAllEmployees(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("Yash", result.getContent().get(0).getName());
        //ensures employeeRepository.findAll() was called exactly once not more(making sure of redundant calls) or less(to make sure the method is called)
        verify(employeeRepository, times(1)).findAll(pageable);
    }
}
