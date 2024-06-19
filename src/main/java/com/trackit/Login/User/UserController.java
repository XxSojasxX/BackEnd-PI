package com.trackit.Login.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
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
    @PutMapping("/update")
    @Operation(summary = "Actualizar un Usuario")
    public Users update(@RequestBody Users Entity)
    {
        return userService.save(Entity);
    }

    // Método delete
    @DeleteMapping("/{id}/")
    @Operation(summary = "Eliminar un Usuario")
    public void delete(@PathVariable Integer id) {
        userService.deleteById(id);
    }
}

