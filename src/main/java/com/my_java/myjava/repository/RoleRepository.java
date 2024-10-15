package com.my_java.myjava.repository;

import com.my_java.myjava.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository  extends JpaRepository<Role,String> {
}
