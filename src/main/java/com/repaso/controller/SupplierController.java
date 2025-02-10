package com.repaso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.repaso.model.*;
import com.repaso.model.Supplier;
import com.repaso.service.SupplierService;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {
	
	@Autowired
	private SupplierService supplierService;
	
	@GetMapping
	public String listSuppliers(Model model) {
		List<Supplier> suppliers=supplierService.getAllSuppliers();
		model.addAttribute("suppliers",suppliers);
		return "listSuppliers";
	}
	
	@GetMapping("/add")
    public String addSupplierForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("action", "/suppliers/add");
        model.addAttribute("enable", true);
        model.addAttribute("button", "Añadir");
        return "templateSupplier";
    }
	
	@PostMapping("/add")
	public String addSupplier(Model model,@ModelAttribute Supplier supplier) {
		try {
			supplierService.saveSupplier(supplier);
			model.addAttribute("msg","El proveedor "+supplier.getName()+" se ha añadido con éxito.");
			model.addAttribute("enable",false);
		}catch(Exception e) {
			model.addAttribute("msg","Error: "+ e.getMessage());
			model.addAttribute("enable",true);
		}
		
		 model.addAttribute("supplier", supplier);
	        model.addAttribute("action", "/suppliers/add");
	        model.addAttribute("button", "Añadir");
	        return "templateSupplier";
		
		
	}
	
	@GetMapping("/edit")
	public String editSupplierForm(Model model,@RequestParam Long id) {
		try {
			Supplier supplier= supplierService.findById(id);
			model.addAttribute("supplier",supplier);
			model.addAttribute("action","/suppliers/edit");
			model.addAttribute("enable",true);
			model.addAttribute("button","Editar");
		}catch(Exception e) {
			return "error";
		}
		return "templateSupplier";
	}
	
	 @PostMapping("/edit")
	 public String editSupplier(Model model,@ModelAttribute Supplier supplier) {
		 try {
			 supplierService.saveSupplier(supplier);
			 model.addAttribute("msg", "El proveedor '" + supplier.getName() + "' ha sido editado con éxito.");
	            model.addAttribute("enable", false);
	        } catch (Exception e) {
	            model.addAttribute("msg", "Error al editar el proveedor: " + e.getMessage());
	            model.addAttribute("enable", true);
	        }
	        model.addAttribute("supplier", supplier);
	        model.addAttribute("action", "/supplier/edit");
	        model.addAttribute("button", "Editar");
	        return "templateSupplier";
	 }
	 

}
