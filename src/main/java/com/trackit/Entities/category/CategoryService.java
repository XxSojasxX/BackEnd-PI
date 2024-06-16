package com.trackit.Entities.category;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.trackit.Login.User.Role;
import com.trackit.Login.User.Users;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Insert
    public Category categorySave(Category entity) {
        Users currentUser = getCurrentUser();
        entity.setCreatedBy(currentUser);
        return categoryRepository.save(entity);
    }

    // Select
    public Category categoryFindById(Long id) {
        Users currentUser = getCurrentUser();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (canAccessCategory(category, currentUser)) {
            return category;
        } else {
            System.err.println("SecurityException: No tienes permiso para ver esta categoría");
            throw new SecurityException("No tienes permiso para ver esta categoría");
        }
    }

    // Select All
    public List<Category> categoryFindAll() {
        Users currentUser = getCurrentUser();
        Iterable<Category> iterable = categoryRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(category -> canAccessCategory(category, currentUser))
                .collect(Collectors.toList());
    }

    // Delete
    public void categoryDeleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Users currentUser = getCurrentUser();

        if (canModifyCategory(category, currentUser)) {
            categoryRepository.deleteById(id);
        } else {
            System.err.println("SecurityException: No tienes permiso para eliminar esta categoría");
            throw new SecurityException("No tienes permiso para eliminar esta categoría");
        }
    }

    // Método auxiliar para verificar si el usuario puede acceder a la categoría
    private boolean canAccessCategory(Category category, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || category.getCreatedBy().equals(currentUser);
    }

    // Método auxiliar para verificar si el usuario puede modificar la categoría
    private boolean canModifyCategory(Category category, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || category.getCreatedBy().equals(currentUser);
    }

    // Obtener el usuario actualmente autenticado
    private Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Users) auth.getPrincipal();
    }
}
