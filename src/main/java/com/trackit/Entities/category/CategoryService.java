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
        entity.setCreatedBy(currentUser); // Asigna el usuario actual como creador de la categoría
        return categoryRepository.save(entity); // Guarda la categoría en la base de datos
    }

    // Select
    public Category categoryFindById(Long id) {
        Users currentUser = getCurrentUser();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found")); // Busca la categoría por ID

        if (canAccessCategory(category, currentUser)) {
            return category; // Retorna la categoría si el usuario tiene permisos de acceso
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
                .collect(Collectors.toList()); // Retorna todas las categorías accesibles para el usuario actual
    }

    // Update
    public Category categoryUpdate(Long id, Category updatedCategory) {
        Users currentUser = getCurrentUser();
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found")); // Busca la categoría existente

        if (canModifyCategory(existingCategory, currentUser)) {
            existingCategory.setNombreCategoria(updatedCategory.getNombreCategoria());
            existingCategory.setDescripcionCategoria(updatedCategory.getDescripcionCategoria());

            return categoryRepository.save(existingCategory); // Actualiza la categoría si el usuario tiene permisos
        } else {
            System.err.println("SecurityException: No tienes permiso para actualizar esta categoría");
            throw new SecurityException("No tienes permiso para actualizar esta categoría");
        }
    }

    // Delete
    public void categoryDeleteById(Long id) {
        Users currentUser = getCurrentUser();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found")); // Busca la categoría por ID

        if (canModifyCategory(category, currentUser)) {
            categoryRepository.deleteById(id); // Elimina la categoría si el usuario tiene permisos
        } else {
            System.err.println("SecurityException: No tienes permiso para eliminar esta categoría");
            throw new SecurityException("No tienes permiso para eliminar esta categoría");
        }
    }

    // Método auxiliar para verificar si el usuario puede acceder a la categoría
    private boolean canAccessCategory(Category category, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || category.getCreatedBy().getId().equals(currentUser.getId());
    }

    // Método auxiliar para verificar si el usuario puede modificar la categoría
    private boolean canModifyCategory(Category category, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || category.getCreatedBy().getId().equals(currentUser.getId());
    }

    // Obtener el usuario actualmente autenticado
    private Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("No se encontró un usuario autenticado");
        }
        return (Users) auth.getPrincipal(); // Retorna el usuario autenticado actualmente
    }
}