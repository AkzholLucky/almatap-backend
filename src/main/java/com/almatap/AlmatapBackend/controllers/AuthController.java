package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user){
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String doRegistration(@ModelAttribute("user") User user){
        authService.userSave(user);
        return "redirect:/auth/login";
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
    public String getEmail(@ModelAttribute("user") User user){
        return "auth/getEmail";
    }

    @PostMapping("/lost-password")
    public String afterGotEmail(@ModelAttribute("user") User user, Model model){
        boolean isFound = authService.isFound(user.getEmail());
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
    public String changePassword(@ModelAttribute("user") User user, Model model, @PathVariable String code){
        authService.changePassword(user, code);
        model.addAttribute("message", "password successfully changed");
        return "auth/login";
    }
}