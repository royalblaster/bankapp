package com.banking.backend.customer.controller;

import com.banking.backend.customer.dto.CustomerCreateDTO;
import com.banking.backend.customer.dto.CustomerPatchDTO;
import com.banking.backend.customer.dto.CustomerResponseDTO;
import com.banking.backend.customer.dto.CustomerUpdateDTO;
import com.banking.backend.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerCreateDTO dto) {
        return ResponseEntity.ok(customerService.createCustomer(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody CustomerUpdateDTO dto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> partialUpdate(@PathVariable UUID id, @Valid @RequestBody CustomerPatchDTO dto) {
        return ResponseEntity.ok(customerService.partialUpdateCustomer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

}

