package com.repaso.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.repaso.model.Product;
import com.repaso.repository.ProductRepository;


@Service
public class ProductService {

	 @Autowired
	    private ProductRepository productRepository;

	    /**
	     * Obtiene todos los productos de la base de datos.
	     */
	    public List<Product> getAllProducts() {
	        return productRepository.findAll();
	    }
	    
	    /**
	     * Busca un producto por su nombre de usuario.
	     */
	    public Product findById(long id) {
	        return productRepository.findById(id);
	    }

	    /**
	     * Guarda un nuevo producto o actualiza uno existente.
	     */
	    public void saveProduct(Product product) {
	    	productRepository.save(product);
	    }

	    /**
	     * Elimina un producto por su username.
	     */
	    public void deleteProduct(long id) {
	    	productRepository.deleteById(id);
	    }
	    
	    public Page<Product> getProductsPaginated(int page, int size) {
	        Pageable pageable = PageRequest.of(page, size);
	        return productRepository.findAll(pageable);
	    }
	    
	    public List<Product> searchProductsByName(String name, int page, int size) {
	        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")); // Ordenar por ID descendente
	        Page<Product> userPage = productRepository.findByNameContainingIgnoreCase(name, pageable);
	        return userPage.getContent();
	    }
	    
	    public long countProductsByName(String name) {
	        return productRepository.countByNameContainingIgnoreCase(name);
	    }
	    
	    public boolean existsByName(String name) {
	        return productRepository.findByName(name) != null;
	    }

}
