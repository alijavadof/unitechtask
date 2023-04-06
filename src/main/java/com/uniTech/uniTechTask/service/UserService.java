package com.uniTech.uniTechTask.service;

import com.uniTech.uniTechTask.model.Users;
import com.uniTech.uniTechTask.dto.request.RegisterRequest;

import java.util.Optional;

public interface UserService {
    void register(RegisterRequest requestDto);

    Optional<Users> getUserByPin(String pin);

    boolean authenticate(String pin, String password);
}
