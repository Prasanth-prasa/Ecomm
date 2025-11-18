package edu.guvi.kaddysShop.Service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.guvi.kaddysShop.Model.Product;

public interface ProductService {

    Product save(Product p, MultipartFile file);

    List<Product> findAll();

    Product findById(Long id);

    void delete(Long id);
}
