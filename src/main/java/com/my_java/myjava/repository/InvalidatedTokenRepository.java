package com.my_java.myjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my_java.myjava.entity.InvalidatedToken;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}
