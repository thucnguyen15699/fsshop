package com.example.e_backend.service;

import com.example.e_backend.Model.User;

public interface UserService {
    public User findUserById(Long userId) throws UserException;

    public User findUserProfileByJwt(String jwt) throws UserException;
}
