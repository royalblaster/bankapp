package com.banking.backend.customer.controller;

import com.banking.backend.customer.dto.CustomerCreateDTO;
import com.banking.backend.customer.dto.CustomerPatchDTO;
import com.banking.backend.customer.dto.CustomerResponseDTO;
import com.banking.backend.customer.dto.CustomerUpdateDTO;
import com.banking.backend.customer.exception.CustomerAlreadyExistsException;
import com.banking.backend.customer.exception.NoSuchCustomerExistsException;
import com.banking.backend.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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

    // -------------------- CREATE --------------------

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

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDTO))).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.firstName").value("John")).
                andExpect(jsonPath("$.email").value("john@example.com"));

    }

    @Test
    void create_WhenEmailAlreadyExists_ReturnsConflict() throws Exception {
        CustomerCreateDTO createDTO = new CustomerCreateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );

        when(customerService.createCustomer(createDTO)).thenThrow(new CustomerAlreadyExistsException("exist"));
        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDTO))).
                andExpect(status().isConflict());
    }


    @Test
    void create_WhenBlank_thenBadRequest() throws Exception {
        CustomerCreateDTO createDTO = new CustomerCreateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );
        createDTO.setFirstName("");

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: First name is required."));
    }

    @Test
    void create_WhenInvalidEmail_thenBadRequest() throws Exception {
        CustomerCreateDTO createDTO = new CustomerCreateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );
        createDTO.setEmail("not-an-email");

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: Invalid email format."));
    }

    @Test
    void create_WhenInvalidPhone_thenBadRequest() throws Exception {
        CustomerCreateDTO createDTO = new CustomerCreateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );
        createDTO.setPhone("+123-456-7890");

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: Phone number should be in the format of a valid phone number."));
    }

    @Test
    void create_WhenNullDOB_thenBadRequest() throws Exception {
        CustomerCreateDTO createDTO = new CustomerCreateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );
        createDTO.setDob(null);

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: Date of birth is required."));
    }

    @Test
    void create_WhenPresentDOB_thenBadRequest() throws Exception {
        CustomerCreateDTO createDTO = new CustomerCreateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );
        createDTO.setDob(LocalDate.now());

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: Date of birth must be a past date."));
    }

    @Test
    void create_WhenFutureDOB_thenBadRequest() throws Exception {
        CustomerCreateDTO createDTO = new CustomerCreateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );
        createDTO.setDob(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: Date of birth must be a past date."));
    }

    // -------------------- GET --------------------

    @Test
    void getById_ReturnsCustomer() throws Exception {

        CustomerResponseDTO response = new CustomerResponseDTO(
                customerId,
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );

        when(customerService.getCustomerById(customerId)).thenReturn(response);
        mockMvc.perform(get("/api/v1/customers/{id}", customerId)).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.id").value(response.getId().toString())).
                andExpect(jsonPath("$.firstName").value(response.getFirstName())).
                andExpect(jsonPath("$.email").value(response.getEmail()));
    }

    @Test
    void getById_NotFound() throws Exception {
        when(customerService.getCustomerById(customerId)).thenThrow(new NoSuchCustomerExistsException("Not Found"));
        mockMvc.perform(get("/api/v1/customers/{id}", customerId)).andExpect(status().isNotFound());
    }

    @Test
    void getAll_ReturnCustomers() throws Exception {

        UUID customerId1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID customerId2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

        List<CustomerResponseDTO> response = List.of(
                new CustomerResponseDTO(
                        customerId1,
                        "John",
                        "Doe",
                        "john@example.com",
                        "123 Main St",
                        "+1234567890",
                        LocalDate.of(1990, 1, 1)
                ),

                new CustomerResponseDTO(
                        customerId2,
                        "Jane",
                        "Doe",
                        "jane@gmail.com",
                        "456 Side Ave",
                        "+987654321",
                        LocalDate.of(1999, 1, 1)
                )
        );

        when(customerService.getAllCustomers()).thenReturn(response);

        mockMvc.perform(get("/api/v1/customers")).andExpect(status().isOk()).
                andExpect(jsonPath("$.length()").value(2)).
                andExpect(jsonPath("$[0].id").value(customerId1.toString())).
                andExpect(jsonPath("$[0].firstName").value("John")).
                andExpect(jsonPath("$[0].lastName").value("Doe")).
                andExpect(jsonPath("$[0].email").value("john@example.com")).
                andExpect(jsonPath("$[1].id").value(customerId2.toString())).
                andExpect(jsonPath("$[1].firstName").value("Jane")).
                andExpect(jsonPath("$[1].lastName").value("Doe")).
                andExpect(jsonPath("$[1].email").value("jane@gmail.com"));
    }

    @Test
    void getAll_ReturnEmpty() throws Exception {

        when(customerService.getAllCustomers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/customers")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
    }

    // -------------------- UPDATE (PUT) --------------------

    @Test
    void update_ReturnUpdatedCustomer() throws Exception {

        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO(
                "John",
                "Doe",
                "john.doe@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );

        CustomerResponseDTO response = new CustomerResponseDTO(
                customerId,
                "John",
                "Doe",
                "john.doe@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );

        when(customerService.updateCustomer(customerId, updateDTO)).thenReturn(response);

        mockMvc.perform(put("/api/v1/customers/{id}", customerId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDTO))).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(response.getId().toString())).
                andExpect(jsonPath("$.firstName").value(response.getFirstName())).
                andExpect(jsonPath("$.email").value(response.getEmail()));
    }

    @Test
    void update_NotFound() throws Exception {

        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO(
                "John",
                "Doe",
                "john.doe@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );

        when(customerService.updateCustomer(customerId, updateDTO)).thenThrow(new NoSuchCustomerExistsException("no such customer"));

        mockMvc.perform(put("/api/v1/customers/{id}", customerId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDTO))).andExpect(status().isNotFound());

    }

    @Test
    void update_WhenEmailAlreadyExists_ReturnsConflict() throws Exception {
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );

        when(customerService.updateCustomer(customerId, updateDTO)).thenThrow(new CustomerAlreadyExistsException("customer already exists"));

        mockMvc.perform(put("/api/v1/customers/{id}", customerId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDTO))).andExpect(status().isConflict());

    }

    @Test
    void update_WhenBlank_thenBadRequest() throws Exception {
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO(
                "John",
                "Doe",
                "john.doe@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );
        updateDTO.setLastName("");

        mockMvc.perform(put("/api/v1/customers/{id}", customerId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: Last name is required."));
    }

    @Test
    void update_WhenInvalidEmail_thenBadRequest() throws Exception {
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO(
                "John",
                "Doe",
                "john.doe@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );
        updateDTO.setEmail("not-an-email");

        mockMvc.perform(put("/api/v1/customers/{id}", customerId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: Invalid email format."));
    }

    @Test
    void update_WhenInvalidPhone_thenBadRequest() throws Exception {
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO(
                "John",
                "Doe",
                "john.doe@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );
        updateDTO.setPhone("+1234567890123456789");

        mockMvc.perform(put("/api/v1/customers/{id}", customerId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: Phone number should be in the format of a valid phone number."));
    }

    @Test
    void update_WhenNullDOB_thenBadRequest() throws Exception {
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );
        updateDTO.setDob(null);

        mockMvc.perform(put("/api/v1/customers/{id}", customerId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: Date of birth is required."));
    }

    @Test
    void update_WhenPresentDOB_thenBadRequest() throws Exception {
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );
        updateDTO.setDob(LocalDate.now());

        mockMvc.perform(put("/api/v1/customers/{id}", customerId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: Date of birth must be a past date."));
    }

    @Test
    void update_WhenFutureDOB_thenBadRequest() throws Exception {
        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );
        updateDTO.setDob(LocalDate.now().plusDays(1));

        mockMvc.perform(put("/api/v1/customers/{id}", customerId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: Date of birth must be a past date."));
    }


    // -------------------- PARTIAL UPDATE (PATCH) --------------------

    @Test
    void patch_ReturnUpdatedCustomer() throws Exception {
        CustomerPatchDTO patchDTO = new CustomerPatchDTO();
        patchDTO.setFirstName("Johnny");
        patchDTO.setEmail("johnny.doe@example.com");

        CustomerResponseDTO response = new CustomerResponseDTO(
                customerId,
                "Johnny",
                "Doe",
                "johnny.doe@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );

        when(customerService.partialUpdateCustomer(customerId, patchDTO)).thenReturn(response);

        mockMvc.perform(patch("/api/v1/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId().toString()))
                .andExpect(jsonPath("$.firstName").value(response.getFirstName()))
                .andExpect(jsonPath("$.email").value(response.getEmail()));
    }

    @Test
    void patch_NotFound() throws Exception {
        CustomerPatchDTO patchDTO = new CustomerPatchDTO();
        patchDTO.setLastName("Johnny");

        when(customerService.partialUpdateCustomer(customerId, patchDTO))
                .thenThrow(new NoSuchCustomerExistsException("No such customer"));

        mockMvc.perform(patch("/api/v1/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void patch_WhenEmailAlreadyExists_ReturnsConflict() throws Exception {
        CustomerPatchDTO patchDTO = new CustomerPatchDTO();
        patchDTO.setEmail("johnny.bgood@example.com");

        when(customerService.partialUpdateCustomer(customerId, patchDTO))
                .thenThrow(new CustomerAlreadyExistsException("customer already exists"));

        mockMvc.perform(patch("/api/v1/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void patch_WhenInvalidEmail_thenBadRequest() throws Exception {
        CustomerPatchDTO patchDTO = new CustomerPatchDTO();
        patchDTO.setEmail("not-an-email");

        mockMvc.perform(patch("/api/v1/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Validation failed: Invalid email format."));
    }

    @Test
    void patch_WhenInvalidPhone_thenBadRequest() throws Exception {
        CustomerPatchDTO patchDTO = new CustomerPatchDTO();
        patchDTO.setPhone("+1234567890123456789");

        mockMvc.perform(patch("/api/v1/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Validation failed: Phone number should be in the format of a valid phone number."));
    }

    @Test
    void patch_WhenFutureDOB_thenBadRequest() throws Exception {
        CustomerPatchDTO patchDTO = new CustomerPatchDTO();
        patchDTO.setDob(LocalDate.now().plusDays(1));

        mockMvc.perform(patch("/api/v1/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Validation failed: Date of birth must be a past date."));
    }

    @Test
    void patch_WhenPresentDOB_thenBadRequest() throws Exception {
        CustomerPatchDTO patchDTO = new CustomerPatchDTO();
        patchDTO.setDob(LocalDate.now());

        mockMvc.perform(patch("/api/v1/customers/{id}", customerId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(patchDTO))).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Validation failed: Date of birth must be a past date."));
    }


    // -------------------- DELETE --------------------

    @Test
    void deleteById_WhenExists_ReturnsNoContent() throws Exception {
        // service delete normally does not return anything, so no need to stub
        doNothing().when(customerService).deleteCustomerById(customerId);

        mockMvc.perform(delete("/api/v1/customers/{id}", customerId)).andExpect(status().isNoContent());
    }

    @Test
    void deleteById_WhenNotFound_ReturnsNotFound() throws Exception {
        doThrow(new NoSuchCustomerExistsException("Customer not found")).when(customerService).deleteCustomerById(customerId);


        mockMvc.perform(delete("/api/v1/customers/{id}", customerId)).
                andExpect(status().isNotFound()).
                andExpect(jsonPath("$.message").exists()).
                andExpect(jsonPath("$.message").value("Customer not found"));
    }

}