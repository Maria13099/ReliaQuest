package com.example.rqchallenge.employees.controller;
import com.example.rqchallenge.RqChallengeApplication;
import com.example.rqchallenge.employees.dto.EmployeeDto;
import com.example.rqchallenge.employees.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = RqChallengeApplication.class)
@AutoConfigureMockMvc
public class EmployeeDtoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        //
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        List<EmployeeDto> employeeDtos = Arrays.asList(
                new EmployeeDto("1", "John Doe", "50000", "30", ""),
                new EmployeeDto("2", "Jane Smith", "60000", "28", "")
        );

        when(employeeService.getAllEmployees()).thenReturn(employeeDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employee_name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].employee_name").value("Jane Smith"));
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto("1", "John Doe", "50000", "30", "");

        when(employeeService.getEmployeeById("1")).thenReturn(new ResponseEntity<>(employeeDto, HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders.get("/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee_name").value("John Doe"));
    }

    @Test
    public void testGetHighestSalaryOfEmployees() throws Exception {
        int highestSalary = 100000; // Adjust with your expected value

        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(new ResponseEntity<>(highestSalary, HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders.get("/highestSalary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(highestSalary));
    }
}