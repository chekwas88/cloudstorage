package com.udacity.jwdnd.course1.cloudstorage.services;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.exception.EntityNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exception.ErrorException;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.repository.UserMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
    private UserMapper userMapper;
    private HashService hashService;

    public boolean isUsernameAvailable(String username) {
        return userMapper.findUserByUsername(username) == null;
    }

    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper
                .insert(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(),
                        user.getLastName()));
    }

    public User findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    public User findUserById(Integer userId) {
        return userMapper.findUserById(userId);
    }

    static User getCurrentUser(User user, String username) {
        if (user != null) {
            return user;
        }
        throw new ErrorException("User with " + username + " is not found");
    }

}
