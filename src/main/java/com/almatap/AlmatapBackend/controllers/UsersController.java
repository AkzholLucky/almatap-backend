package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.dto.UserDTO;
import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.security.UsersDetails;
import com.almatap.AlmatapBackend.services.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @GetMapping()
    public UserDTO profile(){

        return convertToUserDTO(currentUser());
    }

    @GetMapping("/change")
    public UserDTO changeProfile(){
        return convertToUserDTO(currentUser());
    }

    @PostMapping("/change")
    public ResponseEntity<HttpStatus> saveChanges(@ModelAttribute("user") User user,
                                                  @RequestParam("image") MultipartFile file) throws IOException {
        usersService.saveChanges(user, file);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> deleteUser(){
        usersService.deleteUser(currentUser());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private User currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersDetails usersDetails = (UsersDetails) authentication.getPrincipal();
        return usersDetails.getUser();
    }

    private UserDTO convertToUserDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}