package edu.guvi.kaddysShop.Service;

import edu.guvi.kaddysShop.Model.Cart;
import edu.guvi.kaddysShop.Model.User;

public interface CartService {

    Cart getCart(User user);

    void addToCart(User user, Long productId);

    void removeFromCart(User user, Long itemId);

    void updateQuantity(User user, Long itemId, int qty);
}
