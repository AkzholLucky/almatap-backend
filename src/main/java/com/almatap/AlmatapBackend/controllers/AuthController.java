package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.dto.UserDTO;
import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.services.AuthService;
import com.almatap.AlmatapBackend.util.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;

    public AuthController(AuthService authService, ModelMapper modelMapper, UserValidator userValidator) {
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }

    @GetMapping("/login")
    public Map<String, String> login(){
        return Map.of("Message", "Login");
    }

    @GetMapping("/registration")
    public Map<String, String> registration(@ModelAttribute("user") UserDTO userDTO){
        return Map.of("Message", "Registration");
    }

    @PostMapping("/registration")
    public Map<String, Object> doRegistration(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult){

        Map<String, Object> map = new HashMap<>();

        userValidator.validate(userDTO, bindingResult);

        System.out.println(bindingResult);
        if (bindingResult.hasErrors()){
            map.put("Message", "This email already exist!");
            return map;
        }

        authService.userSave(convertToUser(userDTO));
        map.put("Message", "Check your email!");
        return map;
    }

    @GetMapping("/activate/{code}")
    public Map<String, Object> activate(@PathVariable String code) {
        boolean isActivated = authService.activateUser(code);
        Map<String, Object> map = new HashMap<>();

        if (isActivated) {
            map.put("Message", "User successfully activated");
        } else {
            map.put("Message", "Activation code is not found!");
        }

        return map;
    }

    @GetMapping("/lost-password")
    public ResponseEntity<HttpStatus> getEmail(@ModelAttribute("user") UserDTO userDTO){
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/lost-password")
    public Map<String, Object> afterGotEmail(String email){
        boolean isFound = authService.isFound(email);
        Map<String, Object> map = new HashMap<>();

        if(!isFound){
            map.put("Message", "User with this email not found!");
        } else {
            map.put("Message", "Check your email");
        }

        return map;
    }

    @GetMapping("/lost-password/{code}")
    public Map<String, Object> lostPassword(@PathVariable String code){
        Map<String, Object> map = new HashMap<>();
        UserDTO userDTO = convertToUserDTO(authService.findByCode(code));
        map.put("user", userDTO);
        return map;
    }

    @PostMapping("/lost-password/{code}")
    public Map<String, Object> changePassword(@ModelAttribute("user") UserDTO userDTO, @PathVariable String code){
        System.out.println(convertToUser(userDTO));
        System.out.println(userDTO);
        authService.changePassword(convertToUser(userDTO), code);
        Map<String, Object> map = new HashMap<>();

        map.put("Message", "password successfully changed");
        return map;
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}