package com.yash.employeemanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity // indicates that this class is an entity and is mapped to a database table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users") // specifies the name of the table in the database

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-generates the primary key value with an identity strategy
    private Long id;

    @Column(nullable=false,unique=true) // specifies that this column cannot be null and must be unique
    private String Username;

    @Column(nullable = false)
    private String Password;

}
