package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.dto.DeleteResponse;
import com.example.rqchallenge.employees.dto.EmployeeDataResponse;
import com.example.rqchallenge.employees.dto.CreateResponse;
import com.example.rqchallenge.employees.dto.EmployeeRequest;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class EmployeeController implements IEmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<EmployeeDataResponse>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<EmployeeDataResponse>> getEmployeesByNameSearch(@PathVariable String searchString) {
        return new ResponseEntity<>(employeeService.getEmployeesByNameSearch(searchString), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EmployeeDataResponse> getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return employeeService.getHighestSalaryOfEmployees();
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return employeeService.getTop10HighestEarningEmployeeNames();
    }

    @Override
    public ResponseEntity<CreateResponse> createEmployee(@RequestBody @Valid EmployeeRequest employeeInput) {
        return employeeService.createEmployee(employeeInput);
    }

    @Override
    public ResponseEntity<DeleteResponse> deleteEmployeeById(@PathVariable String id) {
        return employeeService.deleteEmployeeById(id);
    }
}
