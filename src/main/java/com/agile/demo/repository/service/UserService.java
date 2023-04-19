package com.agile.demo.repository.service;

import com.agile.demo.biz.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    // private final RoleRepository roleRepository;

    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByUsername(email).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }
}