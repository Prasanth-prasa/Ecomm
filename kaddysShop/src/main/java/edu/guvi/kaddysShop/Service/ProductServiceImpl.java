package edu.guvi.kaddysShop.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import edu.guvi.kaddysShop.Model.Product;
import edu.guvi.kaddysShop.Repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;
    private final Cloudinary cloudinary;

    @Override
    public Product save(Product product, MultipartFile file) {

        try {
            if (file != null && !file.isEmpty()) {

                Map uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap("folder", "kaddysShop/products")
                );

                String imageUrl = uploadResult.get("secure_url").toString();
                product.setImage(imageUrl);
            }

        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to upload image", e);
        }

        return repo.save(product);
    }

    @Override
    public List<Product> findAll() {
        return repo.findAll();
    }

    @Override
    public Product findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
