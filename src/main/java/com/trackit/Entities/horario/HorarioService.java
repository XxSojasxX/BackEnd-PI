package com.trackit.Entities.horario;

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
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    // Insert
    public Horario horarioSave(Horario entity) {
        Users currentUser = getCurrentUser();
        entity.setCreatedBy(currentUser);
        return horarioRepository.save(entity);
    }

    // Select
    public Horario horarioFindById(Long id) {
        Users currentUser = getCurrentUser();

        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horario no encontrado"));

        if (canAccessHorario(horario, currentUser)) {
            return horario;
        } else {
            System.err.println("SecurityException: No tienes permiso para ver este horario");
            throw new SecurityException("No tienes permiso para ver este horario");
        }
    }

    // Select All
    public List<Horario> horarioFindAll() {
        Users currentUser = getCurrentUser();

        Iterable<Horario> iterable = horarioRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(horario -> canAccessHorario(horario, currentUser))
                .collect(Collectors.toList());
    }

    // Update
    public Horario horarioUpdate(Long id, Horario updatedHorario) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) auth.getPrincipal();

        Horario existingHorario = horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horario not found"));

        if (canModifyHorario(existingHorario, currentUser)) {
            existingHorario.setNombreHorario(updatedHorario.getNombreHorario());
            existingHorario.setHoraEntrada(updatedHorario.getHoraEntrada());
            existingHorario.setHoraSalida(updatedHorario.getHoraSalida());

            return horarioRepository.save(existingHorario);
        } else {
            System.err.println("SecurityException: You do not have permission to update this horario");
            throw new SecurityException("You do not have permission to update this horario");
        }
    }

    // Delete
    public void horarioDeleteById(Long id) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horario no encontrado"));

        Users currentUser = getCurrentUser();

        if (canModifyHorario(horario, currentUser)) {
            horarioRepository.deleteById(id);
        } else {
            System.err.println("SecurityException: No tienes permiso para eliminar este horario");
            throw new SecurityException("No tienes permiso para eliminar este horario");
        }
    }

    // Método auxiliar para verificar si el usuario puede acceder al horario
    private boolean canAccessHorario(Horario horario, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || horario.getCreatedBy().equals(currentUser);
    }

    // Método auxiliar para verificar si el usuario puede modificar el horario
    private boolean canModifyHorario(Horario horario, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || horario.getCreatedBy().equals(currentUser);
    }

    // Obtener el usuario actualmente autenticado
    private Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Users) auth.getPrincipal();
    }
}