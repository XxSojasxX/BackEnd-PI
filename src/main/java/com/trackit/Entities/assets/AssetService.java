package com.trackit.Entities.assets;

import java.util.List;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import java.util.Optional;
import java.time.LocalDateTime;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackit.Entities.employee.Employee;
import com.trackit.Entities.employee.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class AssetService {
    
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    //Insert
    public Asset assetSave (Asset entity)
    {
        return assetRepository.save(entity);
    }

    //Select
    public Asset assetFindById(Long id)
    {
        return assetRepository.findById(id).orElse(null);
    }

    //Select All
    public List<Asset> assetFindAll()
    {
        Iterable<Asset> iterable = assetRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                            .collect(Collectors.toList());
    }

    //Delete
    public void assetDeleteById(Long id)
    {
        assetRepository.deleteById(id);
    }

    //Asignar activo a empleado
    public void asignarActivoAEmpleado(Long assetId, Long employeeId)
    {
        Optional<Asset> optionalAsset = assetRepository.findById(assetId);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);


        if (optionalAsset.isPresent() && optionalEmployee.isPresent())
        {
            Asset asset = optionalAsset.get();
            Employee employee = optionalEmployee.get();
            asset.setEmployee(employee);
            asset.setFechaAsignacion(LocalDateTime.now());
            assetRepository.save(asset);
        } else 
        {
            throw new EntityNotFoundException("Activo o Empleado no encontrado");
        }
    }
}
