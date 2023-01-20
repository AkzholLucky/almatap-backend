package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.dto.UserDTO;
import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.security.UsersDetails;
import com.almatap.AlmatapBackend.services.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private final ModelMapper modelMapper;

    @Autowired
    public UsersController(UsersService usersService, ModelMapper modelMapper) {
        this.usersService = usersService;
        this.modelMapper = modelMapper;
    }

//    @GetMapping
//    public List<UserDTO> index(){
//        return usersService.findAll().stream().map(this::convertToUserDTO).collect(Collectors.toList());
//    }
    @GetMapping("/{id}")
    public String profile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails usersDetails = (UsersDetails) authentication.getPrincipal();
        model.addAttribute("user", usersDetails.getUser());
        return "user/profile";
    }

    public UserDTO convertToUserDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}
