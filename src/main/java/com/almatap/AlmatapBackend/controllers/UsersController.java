package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.models.User;
import com.almatap.AlmatapBackend.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<User> index(){
        return usersService.findAll();
    }

    @PostMapping
    public ResponseEntity<HttpStatus> userSave(@RequestBody @Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new RuntimeException();
        }

        usersService.userSave(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
