package com.agile.demo.biz.user;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String email);

}
