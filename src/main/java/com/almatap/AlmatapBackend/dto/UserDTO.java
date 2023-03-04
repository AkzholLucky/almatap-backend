package com.almatap.AlmatapBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @NotEmpty(message = "Surname should not be empty")
    private String surname;

    @Min(value = 1945, message = "Year of birth should be more than 1945")
    @Max(value = 2008, message = "Year of birth should be less than 2008")
    private int yearOfBirth;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "email should be correctly")
    private String email;

    @NotEmpty(message = "password should not be empty")
    @Size(min = 8, max = 16, message = "password should be more than 8 and less than 16")
    private String password;
}
