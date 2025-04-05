package com.yash.employeemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> { //<T> is a generic type parameter that allows the class to handle different types of data.
    private boolean success;
    private T data;
    private LocalDateTime timestamp;
    private String message;


    public ApiResponse(boolean success,String message,T data){
        this.success=success;
        this.data=data;
        this.message=message;
        this.timestamp=LocalDateTime.now();
    }
}

