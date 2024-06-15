package com.trackit.Entities.employee;

import java.util.List;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    //Insert
    public Employee employeeSave(Employee entity)
    {
        return employeeRepository.save(entity);
    }

    //select
    public Employee employeeFindById(Long id)
    {
        return employeeRepository.findById(id).orElse(null);
    }

    //select all
    public List<Employee> employeeFindAll()
    {
        Iterable<Employee> iterable = employeeRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                            .collect(Collectors.toList());
    }

    //delete 
    public void employeeDeleteById(Long id)
    {
        employeeRepository.deleteById(id);
    }
}
