package edu.guvi.kaddysShop.Repository;

import edu.guvi.kaddysShop.Model.Order;
import edu.guvi.kaddysShop.Model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // List<Order> findByUser(User user);
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
