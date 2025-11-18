package edu.guvi.kaddysShop.Controller;

import edu.guvi.kaddysShop.Model.User;
import edu.guvi.kaddysShop.Service.CartService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final edu.guvi.kaddysShop.Service.UserService userService;

    private User getLoggedUser(Authentication auth) {
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        return userService.findByEmail(email);
    }

    @GetMapping
    public String viewCart(Authentication auth, Model model) {
        User user = getLoggedUser(auth);
        model.addAttribute("cart", cartService.getCart(user));
        return "cart";
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, Authentication auth) {
        User user = getLoggedUser(auth);
        cartService.addToCart(user, id);
        return "redirect:/cart";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id, Authentication auth) {
        User user = getLoggedUser(auth);
        cartService.removeFromCart(user, id);
        return "redirect:/cart";
    }

    @PostMapping("/update/{id}")
    public String updateQty(@PathVariable Long id,
                            @RequestParam int qty,
                            Authentication auth) {

        User user = getLoggedUser(auth);
        cartService.updateQuantity(user, id, qty);
        return "redirect:/cart";
    }
}
