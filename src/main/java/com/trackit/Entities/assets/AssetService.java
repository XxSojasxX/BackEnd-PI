package com.trackit.Entities.assets;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.trackit.Entities.employee.Employee;
import com.trackit.Entities.employee.EmployeeRepository;
import com.trackit.Login.User.Role;
import com.trackit.Login.User.Users;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AssetService {
    
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Insert
    public Asset assetSave(Asset entity) {
        Users currentUser = getCurrentUser();
        entity.setCreatedBy(currentUser);
        return assetRepository.save(entity);
    }

    // Select
    public Asset assetFindById(Long id) {
        Users currentUser = getCurrentUser();
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));
        
        if (canAccessAsset(asset, currentUser)) {
            return asset;
        } else {
            System.err.println("SecurityException: You do not have permission to view this asset");
            throw new SecurityException("You do not have permission to view this asset");
        }
    }

    // Select All
    public List<Asset> assetFindAll() {
        Users currentUser = getCurrentUser();
        Iterable<Asset> iterable = assetRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(asset -> canAccessAsset(asset, currentUser))
                .collect(Collectors.toList());
    }

    // Update
    public Asset assetUpdate(Long id, Asset updatedAsset) {
        Users currentUser = getCurrentUser();
        Asset existingAsset = assetRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));
    
        if (canModifyAsset(existingAsset, currentUser)) {
            existingAsset.setCodigoActivo(updatedAsset.getCodigoActivo());
            existingAsset.setNombreActivo(updatedAsset.getNombreActivo());
            existingAsset.setVidaUtil(updatedAsset.getVidaUtil());
            existingAsset.setCategory(updatedAsset.getCategory());
            existingAsset.setEmployee(updatedAsset.getEmployee());
    
            return assetRepository.save(existingAsset);
        } else {
            System.err.println("SecurityException: No tienes permiso para actualizar este activo");
            throw new SecurityException("No tienes permiso para actualizar este activo");
        }
    }

    // Delete
    public void assetDeleteById(Long id) {
        Users currentUser = getCurrentUser();
        Asset existingAsset = assetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activo no encontrado"));

        if (canModifyAsset(existingAsset, currentUser)) {
            assetRepository.deleteById(id);
        } else {
            System.err.println("SecurityException: No tienes permiso para eliminar este activo");
            throw new SecurityException("No tienes permiso para eliminar este activo");
        }
    }

    // Assign asset to employee
    public void asignarActivoAEmpleado(Long assetId, Long employeeId) {
        Users currentUser = getCurrentUser();
        
        Optional<Asset> optionalAsset = assetRepository.findById(assetId);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (optionalAsset.isPresent() && optionalEmployee.isPresent()) {
            Asset asset = optionalAsset.get();
            Employee employee = optionalEmployee.get();

            if (canModifyAsset(asset, currentUser)) {
                asset.setEmployee(employee);
                asset.setFechaAsignacion(LocalDateTime.now());
                assetRepository.save(asset);
            } else {
                throw new SecurityException("No tienes permiso para asignar este activo a un empleado");
            }
        } else {
            throw new EntityNotFoundException("Activo o Empleado no encontrado");
        }
    }

    // Método auxiliar para verificar si el usuario puede acceder al activo
    private boolean canAccessAsset(Asset asset, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || asset.getCreatedBy().getId().equals(currentUser.getId());
    }

    // Método auxiliar para verificar si el usuario puede modificar el activo
    private boolean canModifyAsset(Asset asset, Users currentUser) {
        return currentUser.getRole() == Role.ADMIN || asset.getCreatedBy().getId().equals(currentUser.getId());
    }

    // Obtener el usuario actualmente autenticado
    private Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("No se encontró un usuario autenticado");
        }
        return (Users) auth.getPrincipal();
    }
}