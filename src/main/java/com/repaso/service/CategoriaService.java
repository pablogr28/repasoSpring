package com.repaso.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.repaso.model.Categoria;
import com.repaso.repository.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Obtiene todas las categorias de la base de datos.
     */
    public List<Categoria> getAllCategories() {
        return categoriaRepository.findAll();
    }
}
