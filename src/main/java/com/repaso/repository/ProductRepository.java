package com.repaso.repository;


import com.repaso.model.Product;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findById(long id);
	void deleteById(long id);
    
}
