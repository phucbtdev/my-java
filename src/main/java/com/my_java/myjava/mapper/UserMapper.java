package com.my_java.myjava.mapper;

import com.my_java.myjava.dto.request.UserCreationRequest;
import com.my_java.myjava.dto.request.UserUpdateRequest;
import com.my_java.myjava.dto.response.UserResponse;
import com.my_java.myjava.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

//    @Mapping(source = "",target="") map từ source -> target
//    @Mapping(source = "",ignore = true) loại bỏ field
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles",ignore = true)
    void updateUser( @MappingTarget User user, UserUpdateRequest request);
}
