package edu.guvi.kaddysShop.repository;

import edu.guvi.kaddysShop.Model.Product;
import edu.guvi.kaddysShop.Repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // <-- IMPORTANT
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repo;

    @Test
    void testSaveProduct() {
        Product p = new Product();
        p.setName("Test Product");
        p.setPrice(100);
        p.setDescription("Test");
        p.setImage("test.jpg");

        Product saved = repo.save(p);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Product");
    }

    @Test
    void testFindById() {
        Product p = new Product();
        p.setName("phone");
        p.setPrice(500);

        Product saved = repo.save(p);

        assertThat(repo.findById(saved.getId())).isPresent();
    }
}
