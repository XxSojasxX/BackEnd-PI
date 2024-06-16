package com.trackit.Entities.employee;

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
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Insert
    public Employee employeeSave(Employee entity) {
        Users currentUser = getCurrentUser();
        entity.setCreatedBy(currentUser);
        return employeeRepository.save(entity);
    }

    // Select
    public Employee employeeFindById(Long id) {
        Users currentUser = getCurrentUser();
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (canAccessEmployee(employee, currentUser)) {
            return employee;
        } else {
            System.err.println("SecurityException: No tienes permiso para ver este empleado");
            throw new SecurityException("No tienes permiso para ver este empleado");
        }
    }

    // Select All
    public List<Employee> employeeFindAll() {
        Users currentUser = getCurrentUser();
        Iterable<Employee> iterable = employeeRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(employee -> canAccessEmployee(employee, currentUser))
                .collect(Collectors.toList());
    }

    // Delete
    public void employeeDeleteById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        Users currentUser = getCurrentUser();

        if (canModifyEmployee(employee, currentUser)) {
            employeeRepository.deleteById(id);
        } else {
            System.err.println("SecurityException: No tienes permiso para eliminar este empleado");
            throw new SecurityException("No tienes permiso para eliminar este empleado");
        }
    }

    // Método auxiliar para verificar si el usuario puede acceder al empleado
    private boolean canAccessEmployee(Employee employee, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || employee.getCreatedBy().equals(currentUser);
    }

    // Método auxiliar para verificar si el usuario puede modificar el empleado
    private boolean canModifyEmployee(Employee employee, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || employee.getCreatedBy().equals(currentUser);
    }

    // Obtener el usuario actualmente autenticado
    private Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Users) auth.getPrincipal();
    }
}