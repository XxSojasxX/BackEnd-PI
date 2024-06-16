package com.trackit.Login.Auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trackit.Login.JWT.JwtService;
import com.trackit.Login.User.UserRepository;
import com.trackit.Login.User.Users;

import lombok.RequiredArgsConstructor;




@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        UserDetails user = userRepository.findByUserName(request.getUserName()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        Users user = Users.builder()
            .userName(request.getUserName())
            .password(passwordEncoder.encode(request.getPassword())) // Encriptar la contraseña
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .role(request.getRole()) 
            .build();  

        userRepository.save(user);

        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .build();
    }
}

