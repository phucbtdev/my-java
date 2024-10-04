package com.my_java.myjava.service;

import com.my_java.myjava.dto.request.UserCreationRequest;
import com.my_java.myjava.dto.request.UserUpdateRequest;
import com.my_java.myjava.dto.response.UserResponse;
import com.my_java.myjava.entity.User;
import com.my_java.myjava.mapper.UserMapper;
import com.my_java.myjava.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    //Create
    public UserResponse createRequest(UserCreationRequest request){
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username exist!");

        User user = userMapper.toUser(request); // Map request -> model

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10); //Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user)); //Map model -> response
    }

    //Update user
    public UserResponse updateRequest(String userId ,UserUpdateRequest request){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        userMapper.updateUser(user,request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    //Get list users
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    //Get by ID
    public UserResponse getUserById(String id){
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found")));
    }

}
