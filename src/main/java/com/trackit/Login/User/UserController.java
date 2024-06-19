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

@RestController
@RequestMapping("trackit/users")
public class UserController {
    @Autowired
    private UserService userService;

    //Metodo create
    @PostMapping()
    public Users save(@RequestBody Users entity)
    {
        return userService.save(entity);
    }

    //Metodo select
    @GetMapping("/{id}/")
    public Users findById(@PathVariable Integer id)
    {
        return userService.findById(id);
    }

    //Metodo select all
    @GetMapping()
    public List<Users> findAll() {
        return userService.findAll();
    }

    //Metodo update
    @PutMapping("/update")
    public Users update(@RequestBody Users Entity)
    {
        return userService.save(Entity);
    }

    // Método delete
    @DeleteMapping("/{id}/")
    public void delete(@PathVariable Integer id) {
        userService.deleteById(id);
    }
}

