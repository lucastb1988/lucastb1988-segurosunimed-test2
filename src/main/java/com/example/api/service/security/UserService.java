package com.example.api.service.security;

import com.example.api.domain.security.ProfileEnum;
import com.example.api.domain.security.User;
import com.example.api.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user) {
        User userDb = findByEmailExists(user.getEmail());

        if (userDb != null) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "User already exists!");
        }

        user.getProfiles().forEach(p -> ProfileEnum.fromDescription(p));
        userRepository.save(user);
    }

    private User findByEmailExists(String email) {
        return userRepository.findByEmail(email);
    }

}
