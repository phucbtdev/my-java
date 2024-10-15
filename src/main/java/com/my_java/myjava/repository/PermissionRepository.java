package com.my_java.myjava.repository;

import com.my_java.myjava.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PermissionRepository  extends JpaRepository<Permission,String> {
}
