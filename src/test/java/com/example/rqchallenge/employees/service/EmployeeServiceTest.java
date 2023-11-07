package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.RqChallengeApplication;
import com.example.rqchallenge.employees.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(classes = RqChallengeApplication.class)
public class EmployeeServiceTest {

    private EmployeeService employeeService;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        employeeService = new EmployeeService(restTemplate);
    }

    @Test
    void testGetEmployeeById_HttpOK() {
        String employeeId = "1";
        EmployeeDataResponse employeeDataResponse = new EmployeeDataResponse("1", "John Doe", "50000", "30", "");
        EmployeeResponse employeeResponse = new EmployeeResponse("", employeeDataResponse, "");
        ResponseEntity<EmployeeResponse> responseEntity = new ResponseEntity<>(employeeResponse, OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseEntity);

        ResponseEntity<EmployeeDataResponse> result = employeeService.getEmployeeById(employeeId);

        assertEquals(OK, result.getStatusCode());
        assertEquals(employeeDataResponse, result.getBody());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        String employeeId = "2";
        EmployeeResponse employeeResponse = new EmployeeResponse();
        ResponseEntity<EmployeeResponse> responseEntity = new ResponseEntity<>(employeeResponse, OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseEntity);

        ResponseEntity<EmployeeDataResponse> result = employeeService.getEmployeeById(employeeId);

        assertEquals(NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void testGetEmployeeById_InternalServerError() {
        String employeeId = "3";
        ResponseEntity<EmployeeResponse> responseEntity = new ResponseEntity<>(null, INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(Class.class)))
                .thenReturn(responseEntity);

        ResponseEntity<EmployeeDataResponse> result = employeeService.getEmployeeById(employeeId);

        assertEquals(INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void testGetAllEmployees_CorrectData() {
        // Arrange
        EmployeeListResponse expectedResponse = new EmployeeListResponse("success", Arrays.asList(
                new EmployeeDataResponse("1", "John Doe", "50000", "30", ""),
                new EmployeeDataResponse("2", "Jane Smith", "60000", "28", "")
        ), "All records fetched");
        ResponseEntity<EmployeeListResponse> responseEntity = new ResponseEntity<>(expectedResponse, OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        List<EmployeeDataResponse> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getEmployeeName());
        assertEquals("Jane Smith", result.get(1).getEmployeeName());
    }

    @Test
    void testGetAllEmployees_SuccessfulAndEmptyList() {
        EmployeeListResponse expectedResponse = new EmployeeListResponse("success", Collections.emptyList(), "All records fetched");
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(expectedResponse, OK));

        List<EmployeeDataResponse> result = employeeService.getAllEmployees();

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllEmployees_NotSuccessfulAndEmptyList() {
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(null, INTERNAL_SERVER_ERROR));

        List<EmployeeDataResponse> result = employeeService.getAllEmployees();

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetEmployeesByNameSearch_CorrectData() {
        // Arrange
        String nameToSearch = "John Doe";
        EmployeeDataResponse johnDoe = new EmployeeDataResponse("1", "John Doe", "50000", "30", "");
        EmployeeDataResponse janeSmith = new EmployeeDataResponse("2", "Jane Smith", "60000", "28", "");
        EmployeeListResponse response = new EmployeeListResponse("success", Arrays.asList(johnDoe, janeSmith), "All records fetched");
        ResponseEntity<EmployeeListResponse> responseEntity = new ResponseEntity<>(response, OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        List<EmployeeDataResponse> result = employeeService.getEmployeesByNameSearch(nameToSearch);

        assertEquals(1, result.size());
        assertEquals(johnDoe, result.get(0));
    }

    @Test
    void testGetEmployeesByNameSearch_SuccessfulAndEmptyList() {
        String nameToSearch = "John Doe";
        EmployeeDataResponse janeSmith = new EmployeeDataResponse("2", "Jane Smith", "60000", "28", "");
        EmployeeListResponse response = new EmployeeListResponse("success", Arrays.asList(janeSmith), "All records fetched");
        ResponseEntity<EmployeeListResponse> responseEntity = new ResponseEntity<>(response, OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        List<EmployeeDataResponse> result = employeeService.getEmployeesByNameSearch(nameToSearch);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetEmployeesByNameSearch_NotSuccessfulAndEmptyList() {
        String nameToSearch = "John Doe";
        EmployeeListResponse response = new EmployeeListResponse("error", Collections.emptyList(), "Failed to retrieve employees");
        ResponseEntity<EmployeeListResponse> responseEntity = new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        List<EmployeeDataResponse> result = employeeService.getEmployeesByNameSearch(nameToSearch);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetHighestSalaryOfEmployees_CorrectData() {
        List<EmployeeDataResponse> employeeList = Arrays.asList(
                new EmployeeDataResponse("1", "John Doe", "50000", "30", ""),
                new EmployeeDataResponse("2", "Jane Smith", "60000", "28", "")
        );
        ResponseEntity<EmployeeListResponse> responseEntity = new ResponseEntity<>(
                new EmployeeListResponse("success", employeeList, "All records fetched"), OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        ResponseEntity<Integer> result = employeeService.getHighestSalaryOfEmployees();

        assertEquals(OK, result.getStatusCode());
        assertEquals(60000, result.getBody());
    }

    @Test
    void testGetHighestSalaryOfEmployees_SuccessfulEmptyList() {
        ResponseEntity<EmployeeListResponse> responseEntity = new ResponseEntity<>(
                new EmployeeListResponse("success", Collections.emptyList(), "All records fetched"), OK);
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        ResponseEntity<Integer> result = employeeService.getHighestSalaryOfEmployees();

        assertEquals(OK, result.getStatusCode());
        assertEquals(0, result.getBody());
    }

    @Test
    void testGetTop10HighestEarningEmployeeNames_WithMoreThan10Employees() {
        ResponseEntity<EmployeeListResponse> responseEntity = getEmployeeListResponseResponseEntity();
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        ResponseEntity<List<String>> result = employeeService.getTop10HighestEarningEmployeeNames();

        assertEquals(10, result.getBody().size());
        assertTrue(result.getBody().get(0).contains("Alice Smith"));
        assertTrue(result.getBody().get(1).contains("Doe Johnson"));
        assertTrue(result.getBody().get(2).contains("Smith Johnson"));
        assertTrue(result.getBody().get(3).contains("Employee11"));
        assertTrue(result.getBody().get(4).contains("Alice Jane"));
        assertTrue(result.getBody().get(5).contains("John Doe"));
        assertTrue(result.getBody().get(6).contains("Jane Smith"));
        assertTrue(result.getBody().get(7).contains("Jane Johnson"));
        assertTrue(result.getBody().get(8).contains("Al Johns"));
        assertTrue(result.getBody().get(9).contains("Alice John"));
        assertFalse(result.getBody().contains("Alice Doe"));
    }

    private static ResponseEntity<EmployeeListResponse> getEmployeeListResponseResponseEntity() {
        List<EmployeeDataResponse> employees = Arrays.asList(
                new EmployeeDataResponse("1", "John Doe", "60000", "30", ""),
                new EmployeeDataResponse("2", "Jane Smith", "60000", "28", ""),
                new EmployeeDataResponse("3", "Jane Johnson", "55999", "32", ""),
                new EmployeeDataResponse("4", "Alice Jane", "60001", "32", ""),
                new EmployeeDataResponse("5", "Alice Smith", "550000", "32", ""),
                new EmployeeDataResponse("6", "Alice Doe", "5500", "32", ""),
                new EmployeeDataResponse("7", "Doe Johnson", "75000", "32", ""),
                new EmployeeDataResponse("8", "Smith Johnson", "74000", "32", ""),
                new EmployeeDataResponse("9", "Alice John", "44000", "32", ""),
                new EmployeeDataResponse("10", "Al Johns", "51000", "32", ""),
                new EmployeeDataResponse("11", "Employee11", "70000", "25", "")
        );
        return new ResponseEntity<>(
                new EmployeeListResponse("success", employees, "All records fetched"), OK
        );
    }

    @Test
    void testGetTop10HighestEarningEmployeeNames_WithLessThan10Employees() {
        List<EmployeeDataResponse> employees = Arrays.asList(
                new EmployeeDataResponse("1", "John Doe", "60000", "30", ""),
                new EmployeeDataResponse("2", "Jane Smith", "55000", "28", ""),
                new EmployeeDataResponse("3", "Alice Johnson", "70000", "32", "")
        );
        ResponseEntity<EmployeeListResponse> responseEntity = new ResponseEntity<>(
                new EmployeeListResponse("success", employees, "All records fetched"), OK
        );
        when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        ResponseEntity<List<String>> result = employeeService.getTop10HighestEarningEmployeeNames();

        assertEquals(3, result.getBody().size());
        assertTrue(result.getBody().get(0).contains("Alice Johnson"));
        assertTrue(result.getBody().get(1).contains("John Doe"));
        assertTrue(result.getBody().get(2).contains("Jane Smith"));
    }

    @Test
    public void testCreateEmployee_Successful() {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(CreateResponse.class)))
                .thenReturn(new ResponseEntity<>(new CreateResponse("Employee created", new DataResponse("John",
                        "50000", "30", "1")), OK));

        ResponseEntity<CreateResponse> response = employeeService.createEmployee(
                new EmployeeRequest("John", "50000", "30"));

        assertEquals(OK, response.getStatusCode());
        assertEquals("Employee created", response.getBody().getStatus());
        verify(restTemplate, times(1)).exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(CreateResponse.class));
    }

    @Test
    public void testDeleteEmployeeById_Success() {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.DELETE), isNull(), eq(DeleteResponse.class)))
                .thenReturn(new ResponseEntity<>(new DeleteResponse(), OK));

        ResponseEntity<DeleteResponse> result = employeeService.deleteEmployeeById("");

        assertNotNull(result);
        assertEquals(OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(restTemplate, times(1)).exchange(any(String.class), eq(HttpMethod.DELETE), isNull(), eq(DeleteResponse.class));
    }
}
