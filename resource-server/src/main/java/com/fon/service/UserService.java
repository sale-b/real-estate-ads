package com.fon.service;

import com.fon.DAO.UserRepository;
import com.fon.dto.UserDto;
import com.fon.entity.User;
import com.fon.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserDto findByEmailOrSave(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper.INSTANCE::toUserDto)
                .orElseGet(() -> {
                    User newUser = User.builder().email(email).build();
                    return UserMapper.INSTANCE.toUserDto(saveUser(newUser));
                });
    }

    public UserDto findDtoById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return UserMapper.INSTANCE.toUserDto(
                user.orElseThrow(() -> new EntityNotFoundException(String.format("Entity with ID: %d not found.", id))));
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
