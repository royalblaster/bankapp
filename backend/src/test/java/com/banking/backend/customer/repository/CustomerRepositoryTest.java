package com.banking.backend.customer.repository;

import com.banking.backend.customer.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer baseCustomer;

    @BeforeEach
    void setUp() {
        baseCustomer = new Customer("John", "Doe", "john.doe@example.com", "123 Main St", "+12345678901", LocalDate.of(1990, 1, 1));
    }

    @AfterEach
    void cleanUp() {
        customerRepository.deleteAll();
    }

    @Test
    void testPrePersist() {
        Customer saved = customerRepository.saveAndFlush(baseCustomer);
        Instant createdAt = saved.getCreatedAt();
        Instant updatedAt = saved.getUpdatedAt();

        assertNotNull(saved.getId());
        assertNotNull(createdAt);
        assertNotNull(updatedAt);
        assertEquals(createdAt,updatedAt);
    }

    @Test
    void testPreUpdate() {
        Customer saved = customerRepository.saveAndFlush(baseCustomer);
        Instant createdAt = saved.getCreatedAt();
        Instant updatedAtBefore = saved.getUpdatedAt();

        saved.setAddress("new address");
        Customer updated = customerRepository.saveAndFlush(saved);

        assertEquals(createdAt, updated.getCreatedAt());
        assertTrue(updated.getUpdatedAt().isAfter(updatedAtBefore));
    }

    @Test
    void testFindCustomerById() {
        Customer saved = customerRepository.saveAndFlush(baseCustomer);
        UUID id = saved.getId();
        assertNotNull(id);
        Optional<Customer> found = customerRepository.findById(id);
        assertTrue(found.isPresent());
    }

    @Test
    void testExistEmail() {
        Customer saved = customerRepository.saveAndFlush(baseCustomer);
        assertTrue(customerRepository.existsByEmail("john.doe@example.com"));
        assertFalse(customerRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    void deleteCustomerById() {
        Customer saved = customerRepository.save(baseCustomer);
        UUID id = saved.getId();
        customerRepository.deleteById(id);
        assertFalse(customerRepository.existsById(id));
    }



}
