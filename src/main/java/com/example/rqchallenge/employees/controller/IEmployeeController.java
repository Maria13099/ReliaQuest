package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.dto.DeleteResponse;
import com.example.rqchallenge.employees.dto.EmployeeDataResponse;
import com.example.rqchallenge.employees.dto.CreateResponse;
import com.example.rqchallenge.employees.dto.EmployeeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public interface IEmployeeController {

    @GetMapping()
    ResponseEntity<List<EmployeeDataResponse>> getAllEmployees() throws IOException;

    @GetMapping("/search/{searchString}")
    ResponseEntity<List<EmployeeDataResponse>> getEmployeesByNameSearch(@PathVariable String searchString);

    @GetMapping("/{id}")
    ResponseEntity<EmployeeDataResponse> getEmployeeById(@PathVariable String id);

    @GetMapping("/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    @PostMapping()
    ResponseEntity<CreateResponse> createEmployee(@RequestBody EmployeeRequest employeeInput);

    @DeleteMapping("/{id}")
    ResponseEntity<DeleteResponse> deleteEmployeeById(@PathVariable String id);

}