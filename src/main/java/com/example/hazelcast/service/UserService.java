package com.example.hazelcast.service;

import com.example.hazelcast.entity.User;
import com.example.hazelcast.repository.UserRepository;
import com.example.hazelcast.utils.UserUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = UserUtils.USERS_BY_ID, key = "#id")
    public User getById(Long id) {
        System.out.println("fetch from DB");
        return userRepository.findById(id).orElseThrow();
    }

    public User create(User user) {
        return userRepository.save(user);
    }
}
