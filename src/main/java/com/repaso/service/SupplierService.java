package com.repaso.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.repaso.model.Supplier;
import com.repaso.repository.SupplierRepository;


@Service
public class SupplierService {
	@Autowired
    private SupplierRepository supplierRepository;

    /**
     * Obtiene todos los proveedores de la base de datos.
     */
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    
    /**
     * Busca un proveedor por su nombre de id.
     */
    public Supplier findById(long id) {
        return supplierRepository.findById(id);
    }
    
    /**
     * Guarda un nuevo proveedor o actualiza uno existente.
     */
    public void saveSupplier(Supplier supplier) {
    	supplierRepository.save(supplier);
    }
}
