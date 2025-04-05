package com.studysmart.service;

import com.studysmart.dto.UserRequest;
import com.studysmart.dto.UserResponse;
import com.studysmart.entity.User;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    List<UserResponse> getAllUsers();
    UserResponse getUserByEmail(String email);
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserRequest userRequest);
    void deleteUser(Long id);
}