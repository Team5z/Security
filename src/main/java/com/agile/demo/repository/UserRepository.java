package com.agile.demo.repository;

import com.agile.demo.biz.user.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<User> findByEmail(String email);

    Optional<org.springframework.security.core.userdetails.User> findByUsername(String email);

    // Optional<Object> findByUsername(String username);
}
