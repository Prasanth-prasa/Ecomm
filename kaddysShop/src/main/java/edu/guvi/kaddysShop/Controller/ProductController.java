package edu.guvi.kaddysShop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import edu.guvi.kaddysShop.Model.Product;
import edu.guvi.kaddysShop.Service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    // PUBLIC — Product list
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product-list";
    }

    // PUBLIC — Product details
    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "product-detail";
    }

    // ADMIN — Add product page
    @GetMapping("/admin/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product-form";
    }

    // ADMIN — Save product
    @PostMapping("/admin/save")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam("imageFile") MultipartFile imageFile) {

        productService.save(product, imageFile);
        return "redirect:/products";
    }

    // ADMIN — Delete product
    @GetMapping("/admin/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }
}

