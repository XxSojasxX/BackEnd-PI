package com.trackit.Entities.category;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    //Insert
    public Category categorySave (Category entity)
    {
        return categoryRepository.save(entity);
    }

    //Select
    public Category categoryFindById(Long id)
    {
        return categoryRepository.findById(id).orElse(null);
    }

    //Select All
    public List<Category> categoryFindAll()
    {
        Iterable<Category> iterable = categoryRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                            .collect(Collectors.toList());
    }

    //Delete
    public void categoryDeleteById(Long id)
    {
        categoryRepository.deleteById(id);
    }
    
}
