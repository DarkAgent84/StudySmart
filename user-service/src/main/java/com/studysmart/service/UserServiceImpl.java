package com.studysmart.service;

import com.studysmart.dto.UserRequest;
import com.studysmart.dto.UserResponse;
import com.studysmart.entity.User;
import com.studysmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserResponse createUser(UserRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword()) // Using plain text password
                .role(request.getRole())
                .build();
        repository.save(user);
        return mapToResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(this::mapToResponse)
                .orElse(null);
    }

    @Override
    public UserResponse getUserById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = repository.findById(id).orElse(null);
        if (user != null) {
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setRole(request.getRole());
            repository.save(user);
            return mapToResponse(user);
        }
        return null;
    }

    // Utility method to convert User -> UserResponse
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}