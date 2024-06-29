package org.bigBrotherBooks.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserProfileUpdateDTO {


    @NotBlank(message = "Name is required")
    public String name;
    @Email(message = "Invalid email")
    public String email;
    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Valid phone number is required")
    public String phone;
    public String address;

    public UserProfileUpdateDTO() {
    }

    public @NotBlank(message = "Name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name is required") String name) {
        this.name = name;
    }

    public @Email(message = "Invalid email") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Invalid email") String email) {
        this.email = email;
    }

    public @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Valid phone number is required") String getPhone() {
        return phone;
    }

    public void setPhone(@Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Valid phone number is required") String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserProfileUpdateDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
