package com.my_java.myjava.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.my_java.myjava.dto.request.PermissionCreationRequest;
import com.my_java.myjava.dto.response.PermissionResponse;
import com.my_java.myjava.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionCreationRequest permissionCreationRequest);

    PermissionResponse toPermissionResponse(Permission permission);

    void updatePermission(@MappingTarget Permission permission, PermissionCreationRequest permissionCreationRequest);
}
