package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.dto.UserDTO;
import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.services.AuthService;
import com.almatap.AlmatapBackend.util.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
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
    public String login(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") UserDTO userDTO){
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String doRegistration(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult, Model model){

        userValidator.validate(userDTO, bindingResult);

        System.out.println("Binding: " + bindingResult);
        if (bindingResult.hasErrors()){
            return "auth/registration";
        }

        authService.userSave(convertToUser(userDTO));
        model.addAttribute("message", "check your email!");
        return "auth/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = authService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "auth/login";
    }

    @GetMapping("/lost-password")
    public String getEmail(@ModelAttribute("user") UserDTO userDTO){
        return "auth/getEmail";
    }

    @PostMapping("/lost-password")
    public String afterGotEmail(@ModelAttribute("user") UserDTO userDTO, Model model){
        boolean isFound = authService.isFound(convertToUser(userDTO).getEmail());
        if(!isFound){
            model.addAttribute("message", "User with this email not found!");
        } else {
            model.addAttribute("message", "Check your email");
        }

        return "auth/getEmail";
    }

    @GetMapping("/lost-password/{code}")
    public String lostPassword(Model model, @PathVariable String code){
        model.addAttribute("user", authService.findByCode(code));
        return "auth/lostPassword";
    }

    @PostMapping("/lost-password/{code}")
    public String changePassword(@ModelAttribute("user") UserDTO userDTO, Model model, @PathVariable String code){
        authService.changePassword(convertToUser(userDTO), code);
        model.addAttribute("message", "password successfully changed");
        return "auth/login";
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}