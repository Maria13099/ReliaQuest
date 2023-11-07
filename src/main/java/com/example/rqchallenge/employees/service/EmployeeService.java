package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.dto.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
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

    public List<EmployeeDataResponse> getAllEmployees() {
        ResponseEntity<EmployeeListResponse> response = restTemplate.exchange(
                baseUrl + "employees",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNullElseGet(response.getBody().getData(), List::of);
        }
        log.error("Failed to retrieve employees.");
        return List.of();
    }

    public List<EmployeeDataResponse> getEmployeesByNameSearch(String name) {
        List<EmployeeDataResponse> employeeDataResponses = getAllEmployees();
        return employeeDataResponses.stream()
                .filter(employee -> employee.getEmployeeName().equals(name))
                .collect(Collectors.toList());
    }

    public ResponseEntity<EmployeeDataResponse> getEmployeeById(String id) {
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
        List<EmployeeDataResponse> employeeDataResponses = getAllEmployees();
        return new ResponseEntity<>(employeeDataResponses.stream()
                .mapToInt(employee -> Integer.parseInt(employee.getEmployeeSalary()))
                .max()
                .orElse(0), HttpStatus.OK);
    }

    public ResponseEntity<List<String>> getTop10HighestEarningEmployeeNames() {
        List<EmployeeDataResponse> employeeDataResponses = getAllEmployees();
        List<String> top10Salaries = employeeDataResponses.stream()
                .sorted(Comparator.comparingInt(employee -> -Integer.parseInt(employee.getEmployeeSalary())))
                .limit(10)
                .map(EmployeeDataResponse::getEmployeeName)
                .collect(Collectors.toList());
        return new ResponseEntity<>(top10Salaries, HttpStatus.OK);
    }


    public ResponseEntity<CreateResponse> createEmployee(EmployeeRequest employeeInput) {
        ResponseEntity<CreateResponse> response = restTemplate.exchange(
                baseUrl + "create",
                HttpMethod.POST,
                new HttpEntity<>(employeeInput),
                CreateResponse.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody() != null
                    ? new ResponseEntity<>(response.getBody(), HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.error("Unable to create an employee.");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<DeleteResponse> deleteEmployeeById(String id) {
        ResponseEntity<DeleteResponse> response = restTemplate.exchange(
                baseUrl + "delete/" + id,
                HttpMethod.DELETE,
                null,
                DeleteResponse.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody() != null
                    ? new ResponseEntity<>(response.getBody(), HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.error("Unable to delete an employee.");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
