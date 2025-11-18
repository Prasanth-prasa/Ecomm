package edu.guvi.kaddysShop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.guvi.kaddysShop.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

