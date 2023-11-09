package com.example.e_backend.repository;

import com.example.e_backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByEmail(String email);

}
