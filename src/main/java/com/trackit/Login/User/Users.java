package com.trackit.Login.User;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.trackit.Entities.assets.Asset;
import com.trackit.Entities.category.Category;
import com.trackit.Entities.area.Area;
import com.trackit.Entities.employee.Employee;
import com.trackit.Entities.horario.Horario;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"userName"})})
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @JsonManagedReference("user-asset")
    @JsonIgnore
    private List<Asset> createdAssets;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @JsonManagedReference("user-category")
    @JsonIgnore
    private List<Category> createdCategories;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @JsonManagedReference("user-area")
    @JsonIgnore
    private List<Area> createdAreas;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @JsonManagedReference("user-employee")
    @JsonIgnore
    private List<Employee> createdEmployees;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    @JsonManagedReference("user-horario")
    @JsonIgnore
    private List<Horario> createdHorarios;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}