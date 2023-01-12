package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.dto.UserDTO;
import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.services.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private final ModelMapper modelMapper;

    @Autowired
    public UsersController(UsersService usersService, ModelMapper modelMapper) {
        this.usersService = usersService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<UserDTO> index(){
        return usersService.findAll().stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    public UserDTO convertToUserDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}
