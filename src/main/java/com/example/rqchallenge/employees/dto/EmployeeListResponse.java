package com.example.rqchallenge.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeListResponse {
    private String status;
    private List<EmployeeDto> data;
    private String message;
}
