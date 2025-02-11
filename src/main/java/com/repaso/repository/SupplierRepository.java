package com.repaso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.repaso.model.Supplier;


@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
	Supplier findById(long id);
	void deleteById(long id);
}
