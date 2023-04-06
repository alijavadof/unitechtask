package com.uniTech.uniTechTask.service;

import com.uniTech.uniTechTask.exception.UserAlreadyExistsException;
import com.uniTech.uniTechTask.model.Users;
import com.uniTech.uniTechTask.repository.UserRepository;
import com.uniTech.uniTechTask.dto.request.RegisterRequest;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldSuccess_whenPin_notExistsInDb() {
        var registeredUserPin = "qwerty";
        var pinForRegister = "abc";
        var password = "password";
        var userForRegister = RegisterRequest.builder()
                .pin(pinForRegister)
                .password(password)
                .build();
        var registeredUser = Users.builder()
                .id(1)
                .pin(registeredUserPin)
                .build();
        when(userService.getUserByPin(eq(registeredUserPin))).thenReturn(Optional.of(registeredUser));

        userService.register(userForRegister);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void register_shouldFail_whenPin_existsInDb() {
        var pin = "qwerty";
        var password = "password";
        var userForRegister = RegisterRequest.builder()
                .pin(pin)
                .password(password)
                .build();
        var registeredUser = Users.builder()
                .id(1)
                .pin(pin)
                .build();
        when(userService.getUserByPin(eq(pin))).thenReturn(Optional.of(registeredUser));

        assertThrows(UserAlreadyExistsException.class,
                () -> userService.register(userForRegister),
                "User already exists"
        );
    }

    @Test
    void getUserByPin() {
        var pin = "qwertyy";
        var user = Users.builder().pin(pin).build();
        when(userRepository.findByPin(eq(pin))).thenReturn(Optional.ofNullable(user));

        var response = userService.getUserByPin(pin);

        assertEquals(response.get().getPin(), pin);
    }

    @Test
    public void authenticate_shouldSuccess_whenPinAndPassword_isCorrect() {
        var pin = "qwerty";
        var password = "12345";
        var bCryptPasswordEncoder = new BCryptPasswordEncoder();

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenAnswer(args -> bCryptPasswordEncoder.matches(args.getArgument(0), args.getArgument(1)));

        var user = Users.builder()
                .pin(pin)
                .password(bCryptPasswordEncoder.encode(password))
                .build();

        when(userRepository.findByPin(pin))
                .thenReturn(Optional.of(user));

        var isAuthenticated = userService.authenticate(pin, password);
        assertTrue(isAuthenticated);
    }

    @Test
    public void authenticate_shouldSuccess_whenPin_isNotCorrect() {
        var pin = "qwerty";
        var password = "12345";
        var fakePin = "fakePin";
        var bCryptPasswordEncoder = new BCryptPasswordEncoder();

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenAnswer(args -> bCryptPasswordEncoder.matches(args.getArgument(0), args.getArgument(1)));

        var user = Users.builder()
                .pin(pin)
                .password(bCryptPasswordEncoder.encode(password))
                .build();

        when(userRepository.findByPin(pin))
                .thenReturn(Optional.of(user));

        var isAuthenticated = userService.authenticate(fakePin, password);
        assertFalse(isAuthenticated);
    }


    @Test
    public void authenticate_shouldSuccess_whenPassword_isNotCorrect() {
        var pin = "qwerty";
        var password = "12345";
        var fakePassword = "fakePassword";
        var bCryptPasswordEncoder = new BCryptPasswordEncoder();

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenAnswer(args -> bCryptPasswordEncoder.matches(args.getArgument(0), args.getArgument(1)));

        var user = Users.builder()
                .pin(pin)
                .password(bCryptPasswordEncoder.encode(password))
                .build();

        when(userRepository.findByPin(pin))
                .thenReturn(Optional.of(user));

        var isAuthenticated = userService.authenticate(pin, fakePassword);
        assertFalse(isAuthenticated);
    }
}