package com.repaso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.repaso.model.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	Categoria findById(long id);
	void deleteById(long id);
}
