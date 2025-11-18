package edu.guvi.kaddysShop.Repository;

import edu.guvi.kaddysShop.Model.Cart;
import edu.guvi.kaddysShop.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
