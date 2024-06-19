package com.trackit.Login.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    //CRUD 
    //metodo insert
    public Users save(Users entity) {
        // Codificar la contraseña antes de guardarla
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userRepository.save(entity);
    }

    //metodo select
    public Users findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    //Metodo select all
    public List<Users> findAll() {
        Iterable<Users> iterable = userRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                            .collect(Collectors.toList());
    }

    //Metodo delete
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
