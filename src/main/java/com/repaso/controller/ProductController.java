package com.repaso.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public String listProducts(@RequestParam(defaultValue = "0") int page, 
                @RequestParam(defaultValue = "5") int size, 
                @RequestParam(required = false, defaultValue = "") String dato, 
                @RequestParam(required = false, defaultValue = "") String orden, 
                Model model) {
        // Obtener todos los productos
        List<Product> products = productService.getAllProducts();
        
        // Filtrar productos si 'dato' no está vacío
        List<Product> filteredProducts = products.stream()
            .filter(product -> product.getName().toLowerCase().contains(dato.toLowerCase()))
            .collect(Collectors.toList());
        
        // Ordenar la lista filtrada si 'orden' no está vacío
        if (!orden.isEmpty()) {
            switch (orden) {
                case "name":
                    // Ordenar por nombre
                    filteredProducts.sort(Comparator.comparing(product -> product.getName().toLowerCase()));
                    break;
                default:
                    // Ordenar por ID (orden por defecto)
                    filteredProducts.sort(Comparator.comparing(Product::getId));
                    break;
            }
        }

        // Total de productos filtrados
        int totalProducts = filteredProducts.size();
        int totalPages = (int) Math.ceil((double) totalProducts / size);
        
        // Determinar los productos que deben mostrarse en la página actual
        int fromIndex = Math.min(page * size, totalProducts);
        int toIndex = Math.min((page + 1) * size, totalProducts);
        List<Product> productsForPage = filteredProducts.subList(fromIndex, toIndex);
        
        // Agregar la lista de productos paginados al modelo
        model.addAttribute("products", productsForPage);

        // Datos de paginación
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("resulProducts", totalProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        // Páginas anterior y siguiente
        model.addAttribute("prevPage", page > 0 ? page - 1 : null);
        model.addAttribute("nextPage", page < totalPages - 1 ? page + 1 : null);

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
    public String addProduct(Model model, @ModelAttribute Product product, @RequestParam List<Long> supplierIds, @RequestParam(required = false) Boolean enable) {
        try {
        	if(product.getName().isEmpty()) {
        		model.addAttribute("errorMsg", "Error: El nombre del producto es obligatorio.");
                model.addAttribute("enable", false);  // Esto puede ser manejado aquí.
                return "templateProduct";
        	}
        	
        	if(product.getPrice() < 0) {
        		model.addAttribute("errorMsg", "Error: El precio del producto no es correcto.");
                model.addAttribute("enable", false);  // Esto puede ser manejado aquí.
                return "templateProduct";
        	}
        	
            if (productService.existsByName(product.getName())) {
                model.addAttribute("errorMsg", "Error: Ya existe un producto con el mismo nombre.");
                model.addAttribute("enable", false);  // Esto puede ser manejado aquí.
                return "templateProduct";
            }
            if (product.getStock() < 0) {
                model.addAttribute("errorMsg", "Error: El stock no puede ser negativo.");
                model.addAttribute("enable", false);  // Esto también se maneja aquí.
                return "templateProduct";
            }
            if (supplierIds == null || supplierIds.isEmpty()) {
                model.addAttribute("errorMsg", "Error: Debe seleccionar al menos un proveedor.");
                model.addAttribute("enable", false);  // Esto también se maneja aquí.
                return "templateProduct";
            }

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
            model.addAttribute("enable", false);  // Aquí el enable se establece a false al finalizar correctamente.
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Error: " + e.getMessage());
            model.addAttribute("enable", true);  // Esto se maneja en caso de error.
        }

        // Esta línea adicional debería ir aquí para manejar el valor de enable explícitamente.
        model.addAttribute("enable", enable != null ? enable : false);
        return "templateProduct";
    }
    
    @GetMapping("/edit")
    public String editProductForm(Model model, @RequestParam Long id) {
        try {
            Product product = productService.findById(id);
            if(product == null) {
            	model.addAttribute("errorMsg", "Producto no encontrado con el ID: " + id);
                return "error";
            }
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
    public String editProduct(Model model, @ModelAttribute Product product, @RequestParam List<Long> supplierIds, @RequestParam(required = false) Boolean enable) {
        try {
            Product existingProduct = productService.findById(product.getId());
            if (existingProduct == null) {
                model.addAttribute("errorMsg", "Error: Producto no encontrado.");
                model.addAttribute("enable", false);
                return "templateProduct";
            }
            if (!existingProduct.getName().equals(product.getName()) && productService.existsByName(product.getName())) {
                model.addAttribute("errorMsg", "Error: Ya existe un producto con el mismo nombre.");
                model.addAttribute("enable", false);
                return "templateProduct";
            }
            if (product.getStock() < 0) {
                model.addAttribute("errorMsg", "Error: El stock no puede ser negativo.");
                model.addAttribute("enable", false);
                return "templateProduct";
            }
            if (supplierIds == null || supplierIds.isEmpty()) {
                model.addAttribute("errorMsg", "Error: Debe seleccionar al menos un proveedor.");
                model.addAttribute("enable", false);
                return "templateProduct";
            }

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
            model.addAttribute("errorMsg", "Error al editar el producto: " + e.getMessage());
            model.addAttribute("enable", true);
        }

        // Asegura que el valor de enable sea establecido correctamente.
        model.addAttribute("enable", enable != null ? enable : false);
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
            model.addAttribute("errorMsg", "Error al borrar el producto: " + e.getMessage());
        }
        return "templateProduct";
    }
    
    @GetMapping("/search")
    public String searchProducts(@RequestParam String searchTerm, 
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "5") int size, 
            Model model) {
    	// Buscar usuarios por nombre o apellido
        List<Product> listProducts = productService.searchProductsByName(searchTerm, page, size);
        model.addAttribute("products", listProducts);

        // Agregar datos de paginación
        long resultProducts = productService.countProductsByName(searchTerm);
        model.addAttribute("resultProducts", resultProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) resultProducts / size));
        
        List<Product> totalProducts = productService.getAllProducts();
        model.addAttribute("totalProducts", totalProducts);

        // Retornar a la misma plantilla de lista
        return "listProducts";
    }

}
