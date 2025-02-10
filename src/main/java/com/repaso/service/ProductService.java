package com.repaso.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.repaso.model.Product;
import com.repaso.repository.ProductRepository;

@Service
public class ProductService {

	 @Autowired
	    private ProductRepository productRepository;

	    /**
	     * Obtiene todos los empleados de la base de datos.
	     */
	    public List<Product> getAllProducts() {
	        return productRepository.findAll();
	    }
	    
	    /**
	     * Busca un empleado por su nombre de usuario.
	     */
	    public Product findById(long id) {
	        return productRepository.findById(id);
	    }

	    /**
	     * Guarda un nuevo empleado o actualiza uno existente.
	     */
	    public void saveProduct(Product product) {
	    	productRepository.save(product);
	    }

	    /**
	     * Elimina un empleado por su username.
	     */
	    public void deleteProduct(long id) {
	    	productRepository.deleteById(id);
	    }
}
