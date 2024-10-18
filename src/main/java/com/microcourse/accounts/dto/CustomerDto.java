package com.microcourse.accounts.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto {

    @NotEmpty(message="Name cannot be a null or empty")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotEmpty(message="Email cannot be a null or empty")
    @Email(message = "Email must be a valid email address")
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10}$)", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    private AccountsDto AccountsDto;
}
