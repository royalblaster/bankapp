package com.banking.backend.customer.controller;

import com.banking.backend.customer.dto.CustomerCreateDTO;
import com.banking.backend.customer.dto.CustomerResponseDTO;
import com.banking.backend.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.UUID;


@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CustomerService customerService;

    private final UUID customerId = UUID.randomUUID();

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_ReturnsCustomer() throws Exception {

        CustomerCreateDTO createDTO = new CustomerCreateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );

        CustomerResponseDTO response = new CustomerResponseDTO(
                customerId,
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );

        when(customerService.createCustomer(createDTO)).thenReturn(response);

        mockMvc.perform(post("/api/v1/customers").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(createDTO))).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.firstName").value("John")).
                andExpect(jsonPath("$.email").value("john@example.com"));

    }





}