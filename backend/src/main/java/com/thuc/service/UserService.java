package com.thuc.service;

import com.thuc.Model.User;
import jdk.jshell.spi.ExecutionControl;

public interface UserService {
    public User findUserById(Long userId) throws UserException;

    public User findUserProfileByJwt(String jwt) throws UserException;
}
