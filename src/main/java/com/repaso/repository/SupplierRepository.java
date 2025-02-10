package com.repaso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.repaso.model.Supplier;



public interface SupplierRepository extends JpaRepository<Supplier, Long> {
	Supplier findById(long id);
	void deleteById(long id);
}
