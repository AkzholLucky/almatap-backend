package com.almatap.AlmatapBackend.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthenticationDTO {
    @NotEmpty(message = "username should not be empty")
    @Email(message = "email should be correctly")
    private String username;
    private String password;
}
