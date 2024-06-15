package com.trackit.Entities.horario;

import java.util.List;

import com.trackit.Entities.employee.Employee;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Horario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreHorario;

    @Column(nullable = false)
    private String horaEntrada;

    @Column(nullable = false)
    private String horaSalida;

    @OneToMany(mappedBy = "horario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> empleados;

}