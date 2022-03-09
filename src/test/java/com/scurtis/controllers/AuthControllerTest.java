package com.scurtis.controllers;

import com.scurtis.models.LoginCredentials;
import com.scurtis.models.UserInformation;
import com.scurtis.repository.RoleRepository;
import com.scurtis.repository.UserRepository;
import com.scurtis.security.JWTUtil;
import com.scurtis.security.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    AuthController target;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JWTUtil jwtUtil;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder encoder;
    @Mock
    RefreshTokenService refreshTokenService;

    private final String username = "user";
    private final String password = "123456";

    @BeforeEach
    void setUp() {
        target = new AuthController(authenticationManager, jwtUtil, userRepository, roleRepository, encoder, refreshTokenService);
    }

    @Test
    void registerUser() {
        target.registerUser();
    }

    @Test
    void authenticateUser() {
        LoginCredentials loginCredentials = new LoginCredentials(username, password);
        UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(loginCredentials.getUsername(), loginCredentials.getPassword());

        ResponseEntity<?> result = target.authenticateUser(loginCredentials);

        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        //verify(authenticationManager).authenticate(userAuth);
        verify(jwtUtil).generateToken(new UserInformation());
    }
}