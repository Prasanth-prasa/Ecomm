package edu.guvi.kaddysShop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.guvi.kaddysShop.Model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
