package com.studysmart.feign;

import com.studysmart.dto.User;
import com.studysmart.exception.UserServiceException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service", fallbackFactory = UserServiceFallbackFactory.class)
public interface UserServiceClient {

    @GetMapping("/users/{userId}")
    User getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/users")
    List<User> getAllUsers();

    @GetMapping("/users/batch/{userIds}")
    List<User> getUsersByIds(@PathVariable("userIds") List<Long> userIds);
}

@Component
class UserServiceFallbackFactory implements FallbackFactory<UserServiceClient> {

    @Override
    public UserServiceClient create(Throwable cause) {
        return new UserServiceClient() {
            @Override
            public User getUserById(Long userId) {
                throw new UserServiceException("User service is unavailable: " + cause.getMessage(), cause);
            }

            @Override
            public List<User> getAllUsers() {
                throw new UserServiceException("User service is unavailable: " + cause.getMessage(), cause);
            }

            @Override
            public List<User> getUsersByIds(List<Long> userIds) {
                throw new UserServiceException("User service is unavailable: " + cause.getMessage(), cause);
            }
        };
    }
}