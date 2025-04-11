package com.yash.employeemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.employeemanagement.entity.Employee;
import com.yash.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // This annotation is used to load the full application context for integration tests
@AutoConfigureMockMvc // This annotation is used to configure MockMvc for testing the web layer
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setup() {
        employeeRepository.deleteAll();
        employeeRepository.saveAll(List.of(
                new Employee(null, "Yash", "yash@example.com", "IT", LocalDateTime.now(), LocalDateTime.now()),
                new Employee(null, "John", "john@example.com", "HR", LocalDateTime.now(), LocalDateTime.now())
        ));
    }

    @Test
    public void testGetAllEmployees_ReturnsData() throws Exception {
        mockMvc.perform(get("/employees") //here we are calling with(user() method inside get as its part of request confi
                .with(user("admin").password("password").roles("ADMIN"))) // Mock authentication
                .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content.length()").value(2)) // Access the content of the Page
                .andExpect(jsonPath("$.data.content[0].name").value("Yash"))
                .andExpect(jsonPath("$.data.content[1].name").value("John"));
    }
}
