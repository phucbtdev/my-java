package com.my_java.myjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my_java.myjava.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {}
