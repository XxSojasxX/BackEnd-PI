package com.trackit.Login.Auth;

import com.trackit.Login.User.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String userName;
    String password;
    String firstName;
    String lastName;
    Role role; 
    
}