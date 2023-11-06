package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.dto.EmployeeListResponse;
import com.example.rqchallenge.employees.dto.EmployeeDto;
import com.example.rqchallenge.employees.dto.EmployeeResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
public class EmployeeService {

    private final String baseUrl = "https://dummy.restapiexample.com/api/v1/";
    private final RestTemplate restTemplate;

    @Autowired
    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<EmployeeDto> getAllEmployees() {
        ResponseEntity<EmployeeListResponse> response = restTemplate.exchange(baseUrl + "employees",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNullElseGet(response.getBody().getData(), List::of);
        }
        log.error("Failed to retrieve employees.");
        return List.of();
    }

    public List<EmployeeDto> getEmployeesByNameSearch(String name) {
        List<EmployeeDto> employeeDtos = getAllEmployees();
        return employeeDtos.stream()
                .filter(employee -> employee.getEmployeeName().equals(name))
                .collect(Collectors.toList());
    }

    public ResponseEntity<EmployeeDto> getEmployeeById(String id) {
        ResponseEntity<EmployeeResponse> response = restTemplate.exchange(
                baseUrl + "employee/" + id,
                HttpMethod.GET,
                null,
                EmployeeResponse.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody().getData() != null
                    ? new ResponseEntity<>(response.getBody().getData(), HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.error("Failed to retrieve employee.");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        List<EmployeeDto> employeeDtos = getAllEmployees();
        return new ResponseEntity<>(employeeDtos.stream()
                .mapToInt(employee -> Integer.parseInt(employee.getEmployeeSalary()))
                .max()
                .orElse(0), HttpStatus.OK);
    }

//    public List<Employee> getTop10HighestEarningEmployeeNames() {
//    }

    public ResponseEntity<EmployeeDto> createEmployee(Map<String, Object> employeeInput) {
        return restTemplate.postForEntity(baseUrl + "create", employeeInput, EmployeeDto.class);
    }

    public ResponseEntity<String> deleteEmployeeById(String id) {
        restTemplate.delete(baseUrl + "delete/" + id);
        return new ResponseEntity<>("Employee with ID " + id + " deleted.", HttpStatus.OK);
    }

}
