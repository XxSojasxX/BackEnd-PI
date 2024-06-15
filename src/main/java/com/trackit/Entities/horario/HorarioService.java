package com.trackit.Entities.horario;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HorarioService {
    
    @Autowired
    private HorarioRepository horarioRepository;

    //Insert
    public Horario horarioSave(Horario entity)
    {
        return horarioRepository.save(entity);
    }

    //Select
    public Horario horarioFindById(Long id)
    {
        return horarioRepository.findById(id).orElse(null);
    }

    //select all
    public List<Horario> horarioFindAll()
    {
        Iterable<Horario> iterable = horarioRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                            .collect(Collectors.toList());
    }

    //delete
    public void horarioDeleteById(Long id)
    {
        horarioRepository.deleteById(id);
    }
}
