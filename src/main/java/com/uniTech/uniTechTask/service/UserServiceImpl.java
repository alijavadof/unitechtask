package com.uniTech.uniTechTask.service;

import com.uniTech.uniTechTask.exception.UserAlreadyExistsException;
import com.uniTech.uniTechTask.model.Users;
import com.uniTech.uniTechTask.repository.UserRepository;
import com.uniTech.uniTechTask.dto.request.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void register(RegisterRequest registerRequest) {
        Optional<Users> user = getUserByPin(registerRequest.getPin());
        if (user.isPresent())
            throw new UserAlreadyExistsException("User already exists");
        Users users = Users.builder()
                .pin(registerRequest.getPin())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        userRepository.save(users);
    }

    @Override
    public Optional<Users> getUserByPin(String pin) {
        return userRepository.findByPin(pin);
    }

    @Override
    public boolean authenticate(String pin, String password) {
        Optional<Users> user = userRepository.findByPin(pin);
        if (user.isEmpty()) {
            return false;
        }
        return passwordEncoder.matches(password, user.get().getPassword());
    }
}
