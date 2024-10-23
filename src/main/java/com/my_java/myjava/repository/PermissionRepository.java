package com.my_java.myjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my_java.myjava.entity.Permission;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
