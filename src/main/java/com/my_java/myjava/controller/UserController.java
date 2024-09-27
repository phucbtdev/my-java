package com.my_java.myjava.controller;

import com.my_java.myjava.dto.request.UserCreationRequest;
import com.my_java.myjava.entity.User;
import com.my_java.myjava.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    private String userId;

    @PostMapping
    User createUser(@RequestBody @Valid UserCreationRequest request){
        return userService.createRequest(request);
    }

    @GetMapping
    List<User> getAllUser(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    User getOneUser(@PathVariable String userId){
        return userService.getUserById(userId);
    }

}
