package com.my_java.myjava.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.my_java.myjava.dto.request.ApiResponse;
import com.my_java.myjava.dto.request.PermissionCreationRequest;
import com.my_java.myjava.dto.response.PermissionResponse;
import com.my_java.myjava.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/permissions")
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionCreationRequest permissionCreationRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.createRequest(permissionCreationRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionService.getAll())
                .build();
    }

    @GetMapping("/{permission}")
    ApiResponse<PermissionResponse> getOne(@PathVariable("permission") String permission) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.getOne(permission))
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable("permission") String permission) {
        permissionService.delete(permission);

        return ApiResponse.<Void>builder().build();
    }
}
