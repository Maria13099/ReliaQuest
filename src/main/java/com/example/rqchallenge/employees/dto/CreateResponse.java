package com.example.rqchallenge.employees.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateResponse {
    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private DataResponse data;
}
