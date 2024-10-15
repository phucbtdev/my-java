package com.my_java.myjava.service;

import com.my_java.myjava.dto.request.UserCreationRequest;
import com.my_java.myjava.dto.request.UserUpdateRequest;
import com.my_java.myjava.dto.response.UserResponse;
import com.my_java.myjava.entity.User;
import com.my_java.myjava.enums.Role;
import com.my_java.myjava.mapper.UserMapper;
import com.my_java.myjava.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    //Create
    public UserResponse createRequest(UserCreationRequest request){
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username exist!");

        User user = userMapper.toUser(request); // Map request -> model

        //Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

//        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user)); //Map model -> response
    }

    //Update user
    public UserResponse updateRequest(String userId ,UserUpdateRequest request){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        userMapper.updateUser(user,request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    //Get list users
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers(){
        var user =  userRepository.findAll();
        return user.stream().map(userMapper::toUserResponse).toList();
    }

    //Get by ID
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String id){
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found")));
    }

}
