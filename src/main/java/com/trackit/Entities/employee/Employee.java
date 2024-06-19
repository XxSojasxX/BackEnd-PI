package com.trackit.Entities.employee;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trackit.Entities.area.Area;
import com.trackit.Entities.horario.Horario;
import com.trackit.Entities.assets.Asset;
import com.trackit.Login.User.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    @JoinColumn(name = "area_id", nullable = true)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnoreProperties({"employees"})
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horario_id", nullable = true)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnoreProperties({"employees"})
    private Horario horario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    @JsonBackReference("user-employee")
    private Users createdBy;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore 
    private List<Asset> assets;

    // Métodos getter para área y horario con las anotaciones de JSON para evitar problemas de inicialización perezosa
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Area getArea() {
        return area;
    }

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Horario getHorario() {
        return horario;
    }
}