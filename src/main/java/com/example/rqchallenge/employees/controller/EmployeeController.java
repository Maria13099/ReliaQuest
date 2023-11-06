package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.dto.EmployeeDto;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController implements IEmployeeController {
    private final EmployeeService employeeService;

    @Inject
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;

    }

    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }


    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByNameSearch(@PathVariable String searchString) {
        return new ResponseEntity<>(employeeService.getEmployeesByNameSearch(searchString), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id);
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return employeeService.getHighestSalaryOfEmployees();
    }

//    @Override
//    @GetMapping("/topTenHighestEarningEmployeeNames")
//    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
//        return employeeService.getTop10HighestEarningEmployeeNames();
//    }

    @Override
    @PostMapping()
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody Map<String, Object> employeeInput) {
        return employeeService.createEmployee(employeeInput);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        return employeeService.deleteEmployeeById(id);
    }
}
