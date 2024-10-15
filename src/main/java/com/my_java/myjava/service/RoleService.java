package com.my_java.myjava.service;

import com.my_java.myjava.dto.request.RoleRequest;
import com.my_java.myjava.dto.response.RoleResponse;
import com.my_java.myjava.entity.Role;
import com.my_java.myjava.mapper.RoleMapper;
import com.my_java.myjava.repository.PermissionRepository;
import com.my_java.myjava.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse createRequest(RoleRequest roleRequest) {
        var role =  roleMapper.toRole(roleRequest);

        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(
                roleRepository.save(role)
        );
    }

    public List<RoleResponse> getAll(){
        var roles = roleRepository.findAll();

        return roles.stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }

    public RoleResponse getOne(String role){
        return roleMapper.toRoleResponse(
                roleRepository.findById(role)
                        .orElseThrow(()->new RuntimeException("Role not found"))
        );
    }
}
