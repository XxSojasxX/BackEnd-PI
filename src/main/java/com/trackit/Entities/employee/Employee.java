package com.trackit.Entities.employee;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.trackit.Entities.area.Area;
import com.trackit.Entities.horario.Horario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cedula;

    @Column(nullable = false)
    private String nombreEmpleado;

    @Column(nullable = false)
    private String apellidoEmpleado;

    @Column(nullable = false)
    private String correoEmpleado; 

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horario_id")
    private Horario horario;
}
