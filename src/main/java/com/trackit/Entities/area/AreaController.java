package com.trackit.Entities.area;

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
@RequestMapping("/trackit/rh/areas")
@Tag(name = "Controlador de Area")
public class AreaController {
    
    @Autowired
    private AreaService areaService;

    //Create
    @PostMapping
    @Operation(summary = "Guarda un Area")
    public Area areaSave (@RequestBody Area entity)
    {
        return areaService.areaSave(entity);
    }

    //Select
    @GetMapping("/{id}/")
    @Operation(summary = "Busca un Area por id")
    public Area areaFindById(@PathVariable Long id)
    {
        return areaService.areaFindById(id);
    }

    //Select All
    @GetMapping
    @Operation(summary = "Busca todas las Areas")
    public List<Area> areaFindAll()
    {
        return areaService.areaFindAll();
    }

    //Update
    @PutMapping("/update/{id}/")
    @Operation(summary = "Actualizar un Area")
    public ResponseEntity<Area> areaUpdate(@PathVariable Long id, @RequestBody Area updatedArea) {
        Area existingArea = areaService.areaFindById(id);
        if (existingArea != null) {
            existingArea.setNombreArea(updatedArea.getNombreArea());
            existingArea.setDescripcionArea(updatedArea.getDescripcionArea());
            // Actualizar otros campos según sea necesario

            return ResponseEntity.ok(areaService.areaSave(existingArea));
        } else {
            throw new EntityNotFoundException("Area with id " + id + " not found");
        }
    }

    //Delete
    @DeleteMapping("/{id}/")
    @Operation(summary = "Elminar un Area")
    public void areaDelete(@PathVariable Long id)
    {
        areaService.areaDeleteById(id);
    }

}
