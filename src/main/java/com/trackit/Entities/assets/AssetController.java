package com.trackit.Entities.assets;

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
@RequestMapping("/trackit/bodega/assets")
public class AssetController {
    
    @Autowired
    private AssetService assetService;

    //Create
    @PostMapping
    public Asset assetSave(@RequestBody Asset entity)
    {
        return assetService.assetSave(entity);
    }

    //Select
    @GetMapping("/{id}/")
    public Asset assetFindById(@PathVariable Long id)
    {
        return assetService.assetFindById(id);
    }

    //Select All
    @GetMapping
    public List<Asset> assetFindAll()
    {
        return assetService.assetFindAll();
    }

    //Update
    @PutMapping("/update/{id}/")
    public Asset assetUpdate(@PathVariable Long id, @RequestBody Asset updatedAsset) {
        Asset existingAsset = assetService.assetFindById(id);
        if (existingAsset != null) {
            existingAsset.setCodigoActivo(updatedAsset.getCodigoActivo());
            existingAsset.setNombreActivo(updatedAsset.getNombreActivo());
            existingAsset.setVidaUtil(updatedAsset.getVidaUtil());
            existingAsset.setCategory(updatedAsset.getCategory());
            existingAsset.setEmployee(updatedAsset.getEmployee());
    
            return assetService.assetSave(existingAsset);
        } else {
            throw new EntityNotFoundException("Asset with id " + id + " not found");
        }
    }

    //Delete
    @DeleteMapping("/{id}/")
    public void assetDelete(@PathVariable Long id)
    {
        assetService.assetDeleteById(id);
    }

    //Asignar Activo a Empleado
    @PutMapping(("/{assetId}/assign/{employeeId}"))
    public ResponseEntity<Void> asignarActivoAEmpleado(@PathVariable Long assetId, @PathVariable Long employeeId){
        try{
            assetService.asignarActivoAEmpleado(assetId, employeeId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}

