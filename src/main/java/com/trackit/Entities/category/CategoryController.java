package com.trackit.Entities.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@RestController
@CrossOrigin({"*"})
@RequestMapping("/trackit/bodega/categories")
@Tag(name = "Controlador de Categorias")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;

    //Create
    @PostMapping
    @Operation(summary = "Crear una Categoria")
    public Category categorySave(@RequestBody Category entity)
    {
        return categoryService.categorySave(entity);
    }

    //Select
    @GetMapping("/{id}/")
    @Operation(summary = "Busca una Categoria por id")
    public Category categoryFindById(@PathVariable Long id)
    {
        return categoryService.categoryFindById(id);
    }

    //SelectAll
    @GetMapping
    @Operation(summary = "Busca todas las Categorias")
    public List<Category> categoryFindAll()
    {
        return categoryService.categoryFindAll();
    }

    //Update
    @PutMapping("/update/{id}/")
    @Operation(summary = "Actualizar una Categoria")
    public ResponseEntity<Category> categoryUpdate(@PathVariable Long id, @RequestBody Category updatedCategory) {
        Category existingCategory = categoryService.categoryFindById(id);
        if (existingCategory != null) {
            existingCategory.setNombreCategoria(updatedCategory.getNombreCategoria());
            existingCategory.setDescripcionCategoria(updatedCategory.getDescripcionCategoria());
            // Actualizar otros campos según sea necesario

            return ResponseEntity.ok(categoryService.categorySave(existingCategory));
        } else {
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }
    }
    
    //Delete
    @DeleteMapping("/{id}/")
    @Operation(summary = "Eliminar una Categoria")
    public void categoryDelete(@PathVariable Long id)
    {
        categoryService.categoryDeleteById(id);
    }
}
