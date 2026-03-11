package com.example.hazelcast.service;

import com.example.hazelcast.entity.User;
import com.example.hazelcast.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "users", key= "#id")
    public User getUserById(Long id) {
        System.out.println("fetch from DB");
        return userRepository.findById(id).orElseThrow();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }


    @CachePut(value = "users", key = "#id")
    public User updateUser(Long id, User request) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setMobile(request.getMobile());

        return userRepository.save(existing);
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
