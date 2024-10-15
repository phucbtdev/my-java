package com.my_java.myjava.mapper;

import com.my_java.myjava.dto.request.PermissionCreationRequest;
import com.my_java.myjava.dto.request.UserCreationRequest;
import com.my_java.myjava.dto.request.UserUpdateRequest;
import com.my_java.myjava.dto.response.PermissionResponse;
import com.my_java.myjava.dto.response.UserResponse;
import com.my_java.myjava.entity.Permission;
import com.my_java.myjava.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionCreationRequest permissionCreationRequest);

    PermissionResponse toPermissionResponse(Permission permission);

    void updatePermission( @MappingTarget Permission permission, PermissionCreationRequest permissionCreationRequest);
}
