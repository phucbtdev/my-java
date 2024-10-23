package com.my_java.myjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my_java.myjava.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {}
