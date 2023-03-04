package com.almatap.AlmatapBackend.util;

import com.almatap.AlmatapBackend.dto.UserDTO;
import com.almatap.AlmatapBackend.services.AuthService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final AuthService authService;

    public UserValidator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;

        if(authService.findByEmail(userDTO.getEmail()) != null){
            errors.rejectValue("email", "", "This email already exist!");
        }
    }
}
