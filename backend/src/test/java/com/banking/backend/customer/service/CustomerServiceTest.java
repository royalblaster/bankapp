package com.banking.backend.customer.service;

import com.banking.backend.customer.dto.CustomerCreateDTO;
import com.banking.backend.customer.dto.CustomerPatchDTO;
import com.banking.backend.customer.dto.CustomerResponseDTO;
import com.banking.backend.customer.dto.CustomerUpdateDTO;
import com.banking.backend.customer.exception.CustomerAlreadyExistsException;
import com.banking.backend.customer.exception.NoSuchCustomerExistsException;
import com.banking.backend.customer.model.Customer;
import com.banking.backend.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;

    private CustomerCreateDTO createDTO;
    private CustomerUpdateDTO updateDTO;
    private Customer customer;

    @BeforeEach
    void setUp() {
        createDTO = new CustomerCreateDTO(
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );

        updateDTO = new CustomerUpdateDTO(
                "Johnny",
                "DoeUpdated",
                "johnny@example.com",
                "456 Elm St",
                "+1987654321",
                LocalDate.of(1991, 2, 2)
        );

        customer = new Customer(
                UUID.randomUUID(),
                "John",
                "Doe",
                "john@example.com",
                "123 Main St",
                "+1234567890",
                LocalDate.of(1990, 1, 1)
        );

    }

    @Test
    void createCustomer_success() {
        when(customerRepository.existsByEmail(createDTO.getEmail())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDTO result = customerService.createCustomer(createDTO);

        assertNotNull(result);
        assertEquals(customer.getEmail(),result.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createCustomer_emailExists_throwsException() {
        when(customerRepository.existsByEmail(createDTO.getEmail())).thenReturn(true);
        assertThrows(CustomerAlreadyExistsException.class, ()->customerService.createCustomer(createDTO));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getCustomerById_success() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        CustomerResponseDTO result = customerService.getCustomerById(customer.getId());

        assertNotNull(result);

        assertEquals(customer.getId(),result.getId());
        verify(customerRepository).findById(customer.getId());
    }

    @Test
    void getCustomerById_notFound_throwsException() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchCustomerExistsException.class, () -> customerService.getCustomerById(id));
    }

    @Test
    void getAllCustomers_success() {
        List<Customer> customers = List.of(customer);
        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerResponseDTO> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals(customer.getId(), result.get(0).getId());
        assertEquals(customer.getEmail(), result.get(0).getEmail());
    }

    @Test
    void updateCustomer_success() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(updateDTO.getEmail())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDTO result = customerService.updateCustomer(customer.getId(), updateDTO);

        assertNotNull(result);
        assertEquals(updateDTO.getFirstName(), result.getFirstName());
        assertEquals(updateDTO.getLastName(), result.getLastName());
        assertEquals(updateDTO.getEmail(), result.getEmail());
        assertEquals(updateDTO.getAddress(), result.getAddress());
        assertEquals(updateDTO.getPhone(), result.getPhone());
        assertEquals(updateDTO.getDob(), result.getDob());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void updateCustomer_emailAlreadyExists_throwsException() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(updateDTO.getEmail())).thenReturn(true);

        assertThrows(CustomerAlreadyExistsException.class, ()->customerService.updateCustomer(customer.getId(), updateDTO));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomer_notFound_throwsException() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchCustomerExistsException.class, () -> customerService.updateCustomer(id, updateDTO));
    }

    @Test
    void patchCustomer_success() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail("patched@example.com")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Only updating email
        CustomerPatchDTO patchDTO = new CustomerPatchDTO();
        patchDTO.setEmail("patched@example.com");

        CustomerResponseDTO result = customerService.partialUpdateCustomer(customer.getId(), patchDTO);

        assertNotNull(result);
        assertEquals("patched@example.com", result.getEmail());
        assertEquals(customer.getFirstName(), result.getFirstName());
        assertEquals(customer.getLastName(), result.getLastName());
        assertEquals(customer.getAddress(), result.getAddress());
        assertEquals(customer.getPhone(), result.getPhone());
        assertEquals(customer.getDob(), result.getDob());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void patchCustomer_notFound_throwsException() {
        UUID randomId = UUID.randomUUID();
        when(customerRepository.findById(randomId)).thenReturn(Optional.empty());

        CustomerPatchDTO patchDTO = new CustomerPatchDTO();
        patchDTO.setEmail("patched@example.com");

        assertThrows(NoSuchCustomerExistsException.class, () -> customerService.partialUpdateCustomer(randomId, patchDTO));

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void patchCustomer_emailAlreadyExists_throwsException() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail("patched@example.com")).thenReturn(true);

        CustomerPatchDTO patchDTO = new CustomerPatchDTO();
        patchDTO.setEmail("patched@example.com");

        assertThrows(CustomerAlreadyExistsException.class, () -> customerService.partialUpdateCustomer(customer.getId(), patchDTO));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deleteCustomerById_success() {
        UUID id = customer.getId();
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        customerService.deleteCustomerById(id);

        verify(customerRepository).delete(customer);
    }

    @Test
    void deleteCustomerById_notFound_throwsException() {
        // arrange
        UUID id = UUID.randomUUID();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchCustomerExistsException.class, () -> customerService.deleteCustomerById(id));
        verify(customerRepository, never()).delete(any(Customer.class));
    }


}
