package com.my_java.myjava.controller;

import com.my_java.myjava.dto.request.ApiResponse;
import com.my_java.myjava.dto.request.RoleRequest;
import com.my_java.myjava.dto.response.RoleResponse;
import com.my_java.myjava.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/roles")
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
     ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .data( roleService.createRequest(roleRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .data( roleService.getAll())
                .build();
    }

    @GetMapping("/{role}")
    ApiResponse<RoleResponse> getOne(@PathVariable("role") String role) {
        return ApiResponse.<RoleResponse>builder()
                .data( roleService.getOne(role))
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable("role") String role) {
        roleService.delete(role);

        return ApiResponse.<Void>builder()
                .build();
    }

}
