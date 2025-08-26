package com.banking.backend.customer.service;

import com.banking.backend.customer.dto.CustomerCreateDTO;
import com.banking.backend.customer.dto.CustomerPatchDTO;
import com.banking.backend.customer.dto.CustomerResponseDTO;
import com.banking.backend.customer.dto.CustomerUpdateDTO;
import com.banking.backend.customer.exception.CustomerAlreadyExistsException;
import com.banking.backend.customer.exception.NoSuchCustomerExistsException;
import com.banking.backend.customer.model.Customer;
import com.banking.backend.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Utility mappers
    private Customer mapToEntity(CustomerCreateDTO customerDTO) {
        return new Customer(
                customerDTO.getFirstName(),
                customerDTO.getLastName(),
                customerDTO.getEmail(),
                customerDTO.getAddress(),
                customerDTO.getPhone(),
                customerDTO.getDob()
        );
    }

    private CustomerResponseDTO mapToResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhone(),
                customer.getDob()
        );
    }

    @Transactional
    public CustomerResponseDTO createCustomer(CustomerCreateDTO customerDTO) {
        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer with email " + customerDTO.getEmail() + " already exists.");
        }

        Customer customer = customerRepository.save(mapToEntity(customerDTO));
        return mapToResponseDTO(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(UUID id) {
        CustomerResponseDTO customerDTO = customerRepository.findById(id).map(this::mapToResponseDTO).orElseThrow(() -> new NoSuchCustomerExistsException("No customer found with id " + id));
        return customerDTO;
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::mapToResponseDTO).toList();
    }

    @Transactional
    public CustomerResponseDTO updateCustomer(UUID id, CustomerUpdateDTO customerDTO) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NoSuchCustomerExistsException("No customer found with id " + id));

        if (!customerDTO.getEmail().equals(customer.getEmail())) {
            if (customerRepository.existsByEmail(customerDTO.getEmail())) throw new CustomerAlreadyExistsException("Email already used by another customer");
            customer.setEmail(customerDTO.getEmail());
        }

        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setAddress(customerDTO.getAddress());
        customer.setPhone(customerDTO.getPhone());
        customer.setDob(customerDTO.getDob());

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponseDTO(updatedCustomer);
    }

    @Transactional
    public CustomerResponseDTO partialUpdateCustomer(UUID id, CustomerPatchDTO customerDTO) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NoSuchCustomerExistsException("No customer found with id " + id));

        if (customerDTO.getEmail() != null && !customerDTO.getEmail().equals(customer.getEmail())) {
            if (customerRepository.existsByEmail(customerDTO.getEmail())) {
                throw new CustomerAlreadyExistsException("Email already used by another customer");
            }
            customer.setEmail(customerDTO.getEmail());
        }
        if (customerDTO.getFirstName() != null) {
            customer.setFirstName(customerDTO.getFirstName());
        }
        if (customerDTO.getLastName() != null) {
            customer.setLastName(customerDTO.getLastName());
        }
        if (customerDTO.getAddress() != null) {
            customer.setAddress(customerDTO.getAddress());
        }
        if (customerDTO.getPhone() != null) {
            customer.setPhone(customerDTO.getPhone());
        }
        if (customerDTO.getDob() != null) {
            customer.setDob(customerDTO.getDob());
        }

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponseDTO(updatedCustomer);
    }

    @Transactional
    public void deleteCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NoSuchCustomerExistsException("No customer found with id " + id));
        customerRepository.delete(customer);
    }
}

