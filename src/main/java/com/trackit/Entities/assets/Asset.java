package com.trackit.Entities.assets;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.trackit.Entities.category.Category;
import com.trackit.Entities.employee.Employee;
import com.trackit.Login.User.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigoActivo;

    @Column(nullable = false)
    private String nombreActivo;

    @Column(nullable = false)
    private Integer vidaUtil;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaAsignacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = true)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnoreProperties({"assets"}) // Evita la serialización de assets en employee
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    @JsonBackReference("user-asset")
    private Users createdBy;


    // Método para evitar la serialización de "category" en su forma completa
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Category getCategory() {
        return category;
    }

    // Método para evitar la serialización de "employee" en su forma completa
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Employee getEmployee() {
        return employee;
    }
}