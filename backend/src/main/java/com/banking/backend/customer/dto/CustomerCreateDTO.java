package com.banking.backend.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Objects;

public class CustomerCreateDTO {

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

    public CustomerCreateDTO() {
    }

    public CustomerCreateDTO(String firstName, String lastName, String email, String address, String phone, LocalDate dob) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerCreateDTO)) return false;
        CustomerCreateDTO that = (CustomerCreateDTO) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(dob, that.dob);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, address, phone, dob);
    }
}
