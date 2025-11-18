package edu.guvi.kaddysShop.Service;

import edu.guvi.kaddysShop.Model.*;
import edu.guvi.kaddysShop.Repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;

    @Override
    public Order createOrder(User user, String address) {

        Cart cart = cartRepo.findByUser(user);

        List<OrderItem> orderItems = new ArrayList<>();

        // 1. Create Order
        Order order = Order.builder()
                .user(user)
                .address(address)
                .totalAmount(cart.getTotalAmount())
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        orderRepo.save(order);

        // 2. Convert Cart Items into Order Items
        for (CartItem ci : cart.getItems()) {

            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .product(ci.getProduct())
                    .quantity(ci.getQuantity())
                    .price(ci.getTotalPrice())
                    .build();

            orderItems.add(oi);
        }

        order.setItems(orderItems);
        orderRepo.save(order); // update order with items

        // 3. Clear Cart After Order
        cart.getItems().clear();
        cart.setTotalAmount(0);
        cartRepo.save(cart);

        // delete old cart items
        itemRepo.deleteAll();

        return order;
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    @Override
    public Order save(Order order) {
        return orderRepo.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
    return orderRepo.findByUserOrderByCreatedAtDesc(user);
}


}
