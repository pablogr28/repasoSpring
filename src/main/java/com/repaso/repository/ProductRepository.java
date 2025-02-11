package com.repaso.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.repaso.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findById(long id);
	void deleteById(long id);
	
	Page<Product> findAll(Pageable pageable);
	
	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
	
	 long countByNameContainingIgnoreCase(String name);
    
}
