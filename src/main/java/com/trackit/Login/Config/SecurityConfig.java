package com.trackit.Login.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.trackit.Login.JWT.JwtAuthenticationFilter;
import com.trackit.Login.User.Role;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
            .csrf(csrf -> 
                csrf
                .disable())
            .authorizeHttpRequests(authRequest ->
              authRequest
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("trackit/users").hasAnyAuthority(Role.ADMIN.name())
                //.requestMatchers("/trackit/**").permitAll()
                .requestMatchers("/trackit/admin/**").hasAuthority(Role.ADMIN.name())
                .requestMatchers("/trackit/bodega/**").hasAnyAuthority(Role.BODEGA.name(), Role.ADMIN.name())
                .requestMatchers("/trackit/rh/**").hasAnyAuthority(Role.RH.name(), Role.ADMIN.name())
                .anyRequest().authenticated()
                )
            .sessionManagement(sessionManager->
                sessionManager 
                  .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
            
            
    }

}