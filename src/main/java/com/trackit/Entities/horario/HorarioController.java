package com.trackit.Entities.horario;

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
@RequestMapping("/trackit/rh/horarios")
@Tag(name = "Controlador de Horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    //Create
    @PostMapping
    @Operation(summary = "Crear un Horario")
    public Horario horarioSave (@RequestBody Horario entity)
    {
        return horarioService.horarioSave(entity);
    }

    //Select
    @GetMapping("/{id}/")
    @Operation(summary =  "Buscar un Horario por ID")
    public Horario horarioFindById(@PathVariable Long id)
    {
        return horarioService.horarioFindById(id);
    }

    //Select All
    @GetMapping
    @Operation(summary = "Buscar todos los Horarios")
    public List<Horario> horarioFindAll()
    {
        return horarioService.horarioFindAll();
    }

    //Update
    @PutMapping("/update/{id}/")
    @Operation(summary = "Actualizar un Horario")
    public ResponseEntity<Horario> horarioUpdate(@PathVariable Long id, @RequestBody Horario updatedHorario) {
        Horario actualizarHorario = horarioService.horarioFindById(id);
        if (actualizarHorario != null) {
            actualizarHorario.setNombreHorario(updatedHorario.getNombreHorario());
            actualizarHorario.setHoraEntrada(updatedHorario.getHoraEntrada());
            actualizarHorario.setHoraSalida(updatedHorario.getHoraSalida());

            return ResponseEntity.ok(horarioService.horarioSave(actualizarHorario));
        } else {
            throw new EntityNotFoundException("Horario with id " + id + " not found");
        }
    }                              
    
    //Delete
    @Operation(summary = "Eliminar un Horario")
    @DeleteMapping("/{id}/")
        public void horarioDelete(@PathVariable Long id)
        {
            horarioService.horarioDeleteById(id);
        }
    
    
}
