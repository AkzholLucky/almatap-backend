package com.almatap.AlmatapBackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "Surname should not be empty")
    private String surname;

    @Column(name = "year_of_birth")
    @Min(value = 1945, message = "Year of birth should be more than 1945")
    @Max(value = 2008, message = "Year of birth should be less than 2008")
    private int YearOfBirth;

    @Column(name = "email")
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "password should not be empty")
    private String password;

    @Column(name = "username")
    @NotEmpty(message = "username should not be empty")
    private String username;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "is_enabled")
    private boolean isEnabled;
}
