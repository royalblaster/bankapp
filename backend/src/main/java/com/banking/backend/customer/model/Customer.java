package com.banking.backend.customer.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "Customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private LocalDate dob;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String email, String address, String phone, LocalDate dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.dob = dob;
    }

    public Customer(UUID id, String firstName, String lastName, String email, String address, String phone, LocalDate dob) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.dob = dob;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}

//package com.banking.backend.customer.model;
//
//import java.time.Instant;
//import java.time.LocalDate;
//import java.util.UUID;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import jakarta.persistence.Column;
//import jakarta.persistence.PrePersist;
//import jakarta.persistence.PreUpdate;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Past;
//
//@Entity
//@Table(name = "Customers")
//public class Customer {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    @Column(nullable = false)
//    @NotBlank(message = "First name is required.")
//    private String firstName;
//
//    @Column(nullable = false)
//    @NotBlank(message = "Last name is required.")
//    private String lastName;
//
//    @Column(unique = true, nullable = false)
//    @NotBlank(message = "Email is required.")
//    @Email(message = "Invalid email format.")
//    private String email;
//
//    @Column(nullable = false)
//    @NotBlank(message = "Address is required.")
//    private String address;
//
//    @Column(nullable = false)
//    @NotBlank(message = "Phone number is required.")
//    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be in the format of a valid phone number.")
//    private String phone;
//
//    @Column(nullable = false)
//    @NotNull(message = "Date of birth is required.")
//    @Past(message = "Date of birth must be a past date.")
//    private LocalDate dob;
//
//    @Column(nullable = false, updatable = false)
//    private Instant createdAt;
//
//    @Column(nullable = false)
//    private Instant updatedAt;
//
//    public Customer() {
//    }
//
//    public Customer(String firstName, String lastName, String email, String address, String phone, LocalDate dob) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.address = address;
//        this.phone = phone;
//        this.dob = dob;
//    }
//
//    public Customer(UUID id, String firstName, String lastName, String email, String address, String phone, LocalDate dob) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.address = address;
//        this.phone = phone;
//        this.dob = dob;
//    }
//
//    @PrePersist
//    public void prePersist() {
//        Instant now = Instant.now();  // Get the current timestamp (UTC)
//        this.createdAt = now;
//        this.updatedAt = now;
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        this.updatedAt = Instant.now(); // Update the updatedAt field with the current timestamp
//    }
//
//    public UUID getId() {
//        return id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public LocalDate getDob() {
//        return dob;
//    }
//
//    public void setDob(LocalDate dob) {
//        this.dob = dob;
//    }
//
//    public Instant getCreatedAt() {
//        return createdAt;
//    }
//
//    public Instant getUpdatedAt() {
//        return updatedAt;
//    }
//
//}