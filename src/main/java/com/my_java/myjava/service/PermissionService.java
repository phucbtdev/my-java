package com.my_java.myjava.service;

import com.my_java.myjava.dto.request.PermissionCreationRequest;
import com.my_java.myjava.dto.response.PermissionResponse;
import com.my_java.myjava.entity.Permission;
import com.my_java.myjava.mapper.PermissionMapper;
import com.my_java.myjava.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse createRequest(PermissionCreationRequest permissionCreationRequest) {
        Permission permission =  permissionMapper.toPermission(permissionCreationRequest);

        return permissionMapper.toPermissionResponse(
                permissionRepository.save(permission)
        );
    }

    public List<PermissionResponse> getAll(){
        var permissions = permissionRepository.findAll();

        return permissions.stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }

    public PermissionResponse getOne(String permission){
        return permissionMapper.toPermissionResponse(
                permissionRepository.findById(permission)
                        .orElseThrow(()->new RuntimeException("Permission not found"))
        );
    }
}
