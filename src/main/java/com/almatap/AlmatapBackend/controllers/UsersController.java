package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.security.UsersDetails;
import com.almatap.AlmatapBackend.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping()
    public String profile(Model model){
        model.addAttribute("user", currentUser());
        return "user/profile";
    }

    @GetMapping("/change")
    public String changeProfile(Model model){
        model.addAttribute("user", currentUser());
        return "user/change";
    }

    @PostMapping("/change")
    public String saveChanges(@ModelAttribute("user") User user, @RequestParam("file") MultipartFile file) throws IOException {
        usersService.saveChanges(user, file);
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUser(){
        usersService.deleteUser(currentUser());
        return "redirect:/auth/login";
    }

    private User currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails usersDetails = (UsersDetails) authentication.getPrincipal();
        return usersDetails.getUser();
    }
}
