package com.banking.backend.customer.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class CustomerUpdateDTO {

    @NotBlank(message = "First name is required.")
    private String firstName;
    @NotBlank(message = "Last name is required.")
    private String lastName;
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;
    @NotBlank(message = "Address is required.")
    private String address;
    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be in the format of a valid phone number.")
    private String phone;
    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth must be a past date.")
    private LocalDate dob;

    public CustomerUpdateDTO() {
    }

    public CustomerUpdateDTO(String firstName, String lastName, String email, String address, String phone, LocalDate dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.dob = dob;
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
}
