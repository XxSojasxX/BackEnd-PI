package com.trackit.Entities.area;

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
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    // Insert
    public Area areaSave(Area entity) {
        Users currentUser = getCurrentUser();
        entity.setCreatedBy(currentUser);
        return areaRepository.save(entity);
    }

    // Select
    public Area areaFindById(Long id) {
        Users currentUser = getCurrentUser();

        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Área no encontrada"));

        if (canAccessArea(area, currentUser)) {
            return area;
        } else {
            System.err.println("SecurityException: No tienes permiso para ver esta área");
            throw new SecurityException("No tienes permiso para ver esta área");
        }
    }

    // Select All
    public List<Area> areaFindAll() {
        Users currentUser = getCurrentUser();

        Iterable<Area> iterable = areaRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(area -> canAccessArea(area, currentUser))
                .collect(Collectors.toList());
    }

    // Update
    public Area areaUpdate(Long id, Area updatedArea) {
        Users currentUser = getCurrentUser();

        Area existingArea = areaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Área no encontrada"));

        if (canModifyArea(existingArea, currentUser)) {
            existingArea.setNombreArea(updatedArea.getNombreArea());
            existingArea.setDescripcionArea(updatedArea.getDescripcionArea());

            return areaRepository.save(existingArea);
        } else {
            System.err.println("SecurityException: No tienes permiso para actualizar esta área");
            throw new SecurityException("No tienes permiso para actualizar esta área");
        }
    }

    // Delete
    public void areaDeleteById(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Área no encontrada"));

        Users currentUser = getCurrentUser();

        if (canModifyArea(area, currentUser)) {
            areaRepository.deleteById(id);
        } else {
            System.err.println("SecurityException: No tienes permiso para eliminar esta área");
            throw new SecurityException("No tienes permiso para eliminar esta área");
        }
    }

    // Método auxiliar para verificar si el usuario puede acceder al área
    private boolean canAccessArea(Area area, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || area.getCreatedBy().equals(currentUser);
    }

    // Método auxiliar para verificar si el usuario puede modificar el área
    private boolean canModifyArea(Area area, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || area.getCreatedBy().equals(currentUser);
    }

    // Obtener el usuario actualmente autenticado
    private Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Users) auth.getPrincipal();
    }
}