package edu.guvi.kaddysShop.Service;

import java.util.List;

import edu.guvi.kaddysShop.Model.Order;
import edu.guvi.kaddysShop.Model.User;

public interface OrderService {

    Order createOrder(User user, String address);

    Order getOrder(Long id);

    
    Order save(Order order);

     List<Order> getOrdersByUser(User user);

    
}
