package org.example.dto;

public record EmployeeDto(
        Long id,
        String firstName,
        String lastName,
        String email
){}
