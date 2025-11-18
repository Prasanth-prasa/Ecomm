package edu.guvi.kaddysShop.Service;

import edu.guvi.kaddysShop.Model.*;
import edu.guvi.kaddysShop.Repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final ProductRepository productRepo;

    @Override
    public Cart getCart(User user) {
        Cart cart = cartRepo.findByUser(user);

        if (cart == null) {
            cart = Cart.builder()
                    .user(user)
                    .items(new ArrayList<>())
                    .totalAmount(0)
                    .build();

            cartRepo.save(cart);
        }

        return cart;
    }

    @Override
    public void addToCart(User user, Long productId) {

        Cart cart = getCart(user);
        Product product = productRepo.findById(productId).orElse(null);

        CartItem item = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(1)
                .totalPrice(product.getPrice())
                .build();

        itemRepo.save(item);

        cart.getItems().add(item);
        updateCartTotal(cart);
    }

    @Override
    public void removeFromCart(User user, Long itemId) {

        Cart cart = getCart(user);
        CartItem item = itemRepo.findById(itemId).orElse(null);

        cart.getItems().remove(item);
        itemRepo.delete(item);

        updateCartTotal(cart);
    }

    @Override
    public void updateQuantity(User user, Long itemId, int qty) {

        CartItem item = itemRepo.findById(itemId).orElse(null);

        item.setQuantity(qty);
        item.setTotalPrice(item.getProduct().getPrice() * qty);

        itemRepo.save(item);

        updateCartTotal(getCart(user));
    }

    private void updateCartTotal(Cart cart) {
        double total = cart.getItems()
                .stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        cart.setTotalAmount(total);
        cartRepo.save(cart);
    }
}
