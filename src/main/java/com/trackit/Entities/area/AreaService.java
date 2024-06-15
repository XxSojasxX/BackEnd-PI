package com.trackit.Entities.area;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaService {
    
    @Autowired
    private AreaRepository areaRepository;

    //Inster
    public Area areaSave (Area entity)
    {
        return areaRepository.save(entity);
    }

    //Select
    public Area areaFindById(Long id)
    {
        return areaRepository.findById(id).orElse(null);
    }

    //Select All
    public List<Area> areaFindAll()
    {
        Iterable<Area> iterable = areaRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                            .collect(Collectors.toList());
    }

    //Delete
    public void areaDeleteById(Long id)
    {
        areaDeleteById(id);
    }
}
