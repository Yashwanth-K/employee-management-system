package com.yash.employeemanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees") // Specifies the table name in the database.
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class) // Enables JPA Auditing for this entity

public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be null") // Validates that the name is not blank
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @NotBlank(message = "Email cannot be null")
    @Email(message="invalid email")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Department Name cannot be null")
    @Size(min = 2, message = "Department Name must be at least 2 characters long")
    private String department;

    //Entity
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


}
