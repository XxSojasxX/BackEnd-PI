package com.trackit.Login.Auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trackit.Login.JWT.JwtService;
import com.trackit.Login.User.UserRepository;
import com.trackit.Login.User.Users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
            UserDetails user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
            String token = jwtService.getToken(user);
            return AuthResponse.builder()
                .token(token)
                .build();
        } catch (BadCredentialsException e) {
            log.error("Error de autenticación: Credenciales incorrectas para el usuario {}", request.getUserName());
            throw new RuntimeException("Credenciales incorrectas.");
        } catch (Exception e) {
            log.error("Error de autenticación: {}", e.getMessage());
            throw new RuntimeException("Error de autenticación.");
        }
    }

    public AuthResponse register(RegisterRequest request) {
        try {
            if (userRepository.findByUserName(request.getUserName()).isPresent()) {
                throw new RuntimeException("El nombre de usuario ya está en uso.");
            }
            Users user = Users.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .build();
            userRepository.save(user);
            String token = jwtService.getToken(user);
            return AuthResponse.builder()
                .token(token)
                .build();
        } catch (RuntimeException e) {
            log.error("Error al registrar el usuario: {}", e.getMessage());
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al registrar el usuario: {}", e.getMessage());
            throw new RuntimeException("Error inesperado al registrar el usuario.");
        }
    }
}
