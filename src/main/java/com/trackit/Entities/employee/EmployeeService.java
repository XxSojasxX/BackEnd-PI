package com.trackit.Entities.employee;

import java.util.List;
import java.util.Optional;
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
            System.err.println("SecurityException: You do not have permission to view this employee");
            throw new SecurityException("You do not have permission to view this employee");
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

    // Update
    public Employee employeeUpdate(Long id, Employee updatedEmployee) {
        Users currentUser = getCurrentUser();
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (canModifyEmployee(existingEmployee, currentUser)) {
            existingEmployee.setCedula(updatedEmployee.getCedula());
            existingEmployee.setNombreEmpleado(updatedEmployee.getNombreEmpleado());
            existingEmployee.setApellidoEmpleado(updatedEmployee.getApellidoEmpleado());
            existingEmployee.setCorreoEmpleado(updatedEmployee.getCorreoEmpleado());
            existingEmployee.setArea(updatedEmployee.getArea());
            existingEmployee.setHorario(updatedEmployee.getHorario());

            return employeeRepository.save(existingEmployee);
        } else {
            System.err.println("SecurityException: You do not have permission to update this employee");
            throw new SecurityException("You do not have permission to update this employee");
        }
    }

    // Delete
    public void employeeDeleteById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        Users currentUser = getCurrentUser();

        if (canModifyEmployee(employee, currentUser)) {
            employeeRepository.deleteById(id);
        } else {
            System.err.println("SecurityException: You do not have permission to delete this employee");
            throw new SecurityException("You do not have permission to delete this employee");
        }
    }

    // Método auxiliar para verificar si el usuario puede acceder al empleado
    private boolean canAccessEmployee(Employee employee, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || employee.getCreatedBy().getId().equals(currentUser.getId());
    }

    // Método auxiliar para verificar si el usuario puede modificar el empleado
    private boolean canModifyEmployee(Employee employee, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || employee.getCreatedBy().getId().equals(currentUser.getId());
    }

    // Obtener el usuario actualmente autenticado
    private Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("No authenticated user found");
        }
        return (Users) auth.getPrincipal();
    }
}