package com.my_java.myjava.controller;

import com.my_java.myjava.dto.request.ApiResponse;
import com.my_java.myjava.dto.request.UserCreationRequest;
import com.my_java.myjava.dto.request.UserUpdateRequest;
import com.my_java.myjava.dto.response.UserResponse;
import com.my_java.myjava.entity.User;
import com.my_java.myjava.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor //Tự động inject cho các dependency (kết hợp với makeFinal= true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    //Create model
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.createRequest(request));

        return apiResponse;
    }

    //Update model
    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId ,@RequestBody @Valid UserUpdateRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.updateRequest(userId, request));

        return apiResponse;
    }

    //Get all list model
    @GetMapping
    ApiResponse<List<User>> getAllUser(){
        ApiResponse<List<User>> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getUsers());

        return apiResponse;
    }

    //Get one model
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getOneUser(@PathVariable String userId){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getUserById(userId));

        return apiResponse;
    }

}
