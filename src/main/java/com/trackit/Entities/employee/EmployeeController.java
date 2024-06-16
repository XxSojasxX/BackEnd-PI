package com.trackit.Entities.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/trackit/rh/employees")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;

    //Create
    @PostMapping
    public Employee employeeSave (@RequestBody Employee entity)
    {
        return employeeService.employeeSave(entity);
    }

    //Select
    @GetMapping("/{id}/")
    public Employee employeeFindById(@PathVariable Long id)
    {
        return employeeService.employeeFindById(id);
    }

    //Select All
    @GetMapping
    public List<Employee> employeeFindAll(){
        return employeeService.employeeFindAll();
    }

    //Update
    @PutMapping("/update/{id}/")
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
    public void employeeDelete(@PathVariable Long id){
        employeeService.employeeDeleteById(id);
    }

}
