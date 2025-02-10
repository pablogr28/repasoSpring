package com.repaso.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.repaso.model.Product;
import com.repaso.model.Supplier;
import com.repaso.model.Categoria;
import com.repaso.service.ProductService;
import com.repaso.service.SupplierService;
import com.repaso.service.CategoriaService;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private SupplierService supplierService;

    @ModelAttribute("categories")
    public List<Categoria> getCategorias() {
        return categoriaService.getAllCategories();
    }

    @ModelAttribute("suppliers")
    public List<Supplier> getSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "listProducts";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("action", "/products/add");
        model.addAttribute("enable", true);
        model.addAttribute("button", "Añadir");
        return "templateProduct";
    }

    @PostMapping("/add")
    public String addProduct(Model model, @ModelAttribute Product product, @RequestParam List<Long> supplierIds) {
        try {
            List<Supplier> selectedSuppliers = new ArrayList<>();
            for (Long supplierId : supplierIds) {
                Supplier supplier = supplierService.findById(supplierId);
                if (supplier != null) {
                    selectedSuppliers.add(supplier);
                }
            }
            product.setSuppliers(selectedSuppliers);

            productService.saveProduct(product);
            model.addAttribute("msg", "El producto '" + product.getName() + "' se ha añadido con éxito.");
            model.addAttribute("enable", false);
        } catch (Exception e) {
            model.addAttribute("msg", "Error: " + e.getMessage());
            model.addAttribute("enable", true);
        }
        model.addAttribute("product", product);
        model.addAttribute("action", "/products/add");
        model.addAttribute("button", "Añadir");
        return "templateProduct";
    }

    @GetMapping("/edit")
    public String editProductForm(Model model, @RequestParam Long id) {
        try {
            Product product = productService.findById(id);
            model.addAttribute("product", product);
            model.addAttribute("action", "/products/edit");
            model.addAttribute("enable", true);
            model.addAttribute("button", "Editar");
        } catch (Exception e) {
            return "error";
        }
        return "templateProduct";
    }

    @PostMapping("/edit")
    public String editProduct(Model model, @ModelAttribute Product product, @RequestParam List<Long> supplierIds) {
        try {
            List<Supplier> updatedSuppliers = new ArrayList<>();
            for (Long supplierId : supplierIds) {
                Supplier supplier = supplierService.findById(supplierId);
                if (supplier != null) {
                    updatedSuppliers.add(supplier);
                }
            }
            product.setSuppliers(updatedSuppliers);

            productService.saveProduct(product);
            model.addAttribute("msg", "El producto '" + product.getName() + "' ha sido editado con éxito.");
            model.addAttribute("enable", false);
        } catch (Exception e) {
            model.addAttribute("msg", "Error al editar el producto: " + e.getMessage());
            model.addAttribute("enable", true);
        }
        model.addAttribute("product", product);
        model.addAttribute("action", "/products/edit");
        model.addAttribute("button", "Editar");
        return "templateProduct";
    }

    @GetMapping("/del")
    public String deleteProductForm(Model model, @RequestParam Long id) {
        try {
            Product product = productService.findById(id);
            model.addAttribute("product", product);
            model.addAttribute("action", "/products/del");
            model.addAttribute("enable", false);
            model.addAttribute("button", "Borrar");
        } catch (Exception e) {
            return "error";
        }
        return "templateProduct";
    }

    @PostMapping("/del")
    public String deleteProduct(Model model, @RequestParam Long id) {
        try {
            productService.deleteProduct(id);
            model.addAttribute("msg", "El producto ha sido borrado con éxito.");
            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("msg", "Error al borrar el producto: " + e.getMessage());
        }
        return "templateProduct";
    }
}
