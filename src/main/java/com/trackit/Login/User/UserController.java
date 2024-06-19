package com.trackit.Login.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@RestController
@CrossOrigin({"*"})
@RequestMapping("trackit/users")
@Tag(name = "Controlador para Usuarios")
public class UserController {
    @Autowired
    private UserService userService;

    //Metodo create
    @PostMapping()
    @Operation(summary = "Crear un Usuario")
    public Users save(@RequestBody Users entity)
    {
        return userService.save(entity);
    }

    //Metodo select
    @GetMapping("/{id}/")
    @Operation(summary = "Buscar Usuario por ID")
    public Users findById(@PathVariable Integer id)
    {
        return userService.findById(id);
    }

    //Metodo select all
    @GetMapping()
    @Operation(summary = "Buscar todos los Usuarios")
    public List<Users> findAll() {
        return userService.findAll();
    }

    //Metodo update
    @PutMapping("/update/{id}/")
    @Operation(summary = "Actualizar un Usuario por ID")
    public ResponseEntity<Users> updateUserById(@PathVariable Integer id, @RequestBody Users updatedUser) {
    Users existingUser = userService.findById(id);
    if (existingUser != null) {
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setUserName(updatedUser.getUsername());
        // Actualizar otros campos según sea necesario

        Users savedUser = userService.save(existingUser);
        return ResponseEntity.ok(savedUser);
    } else {
        throw new EntityNotFoundException("Usuario con id " + id + " no encontrado");
    }
    }

    // Método delete
    @DeleteMapping("/{id}/")
    @Operation(summary = "Eliminar un Usuario")
    public void delete(@PathVariable Integer id) {
        userService.deleteById(id);
    }
}

