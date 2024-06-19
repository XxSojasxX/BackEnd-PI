package com.trackit.Entities.employee;

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
@RequestMapping("/trackit/rh/employees")
@Tag(name = "Controlador de Empleados")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;

    //Create
    @PostMapping
    @Operation(summary = "Crea un Empleado")
    public Employee employeeSave (@RequestBody Employee entity)
    {
        return employeeService.employeeSave(entity);
    }

    //Select
    @GetMapping("/{id}/")
    @Operation(summary = "Busca un Empleado por id")
    public Employee employeeFindById(@PathVariable Long id)
    {
        return employeeService.employeeFindById(id);
    }

    //Select All
    @GetMapping
    @Operation(summary = "Busca todos los empleados")
    public List<Employee> employeeFindAll(){
        return employeeService.employeeFindAll();
    }

    //Update
    @PutMapping("/update/{id}/")
    @Operation(summary = "Actualiza un Empleado")
    public ResponseEntity<Employee> employeeUpdate(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        Employee existingEmployee = employeeService.employeeFindById(id);
        if (existingEmployee != null) {
            existingEmployee.setNombreEmpleado(updatedEmployee.getNombreEmpleado());
            existingEmployee.setApellidoEmpleado(updatedEmployee.getApellidoEmpleado());
            // Actualizar otros campos según sea necesario

            return ResponseEntity.ok(employeeService.employeeSave(existingEmployee));
        } else {
            throw new EntityNotFoundException("Employee with id " + id + " not found");
        }
    }

    //DeleteMapping
    @DeleteMapping("/{id}/")
    @Operation(summary = "Busca un Area por id")
    public void employeeDelete(@PathVariable Long id){
        employeeService.employeeDeleteById(id);
    }

}
