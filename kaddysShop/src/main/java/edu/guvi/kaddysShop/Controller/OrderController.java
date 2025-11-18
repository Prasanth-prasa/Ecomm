package edu.guvi.kaddysShop.Controller;

import edu.guvi.kaddysShop.Model.Order;
import edu.guvi.kaddysShop.Model.User;
import edu.guvi.kaddysShop.Service.OrderService;
import edu.guvi.kaddysShop.Service.UserService;
import edu.guvi.kaddysShop.Service.CartService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    private User getUser(Authentication auth) {
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        return userService.findByEmail(email);
    }

    // CHECKOUT PAGE (Address Form)
    @GetMapping("/checkout")
    public String checkout(Authentication auth, Model model) {
        User user = getUser(auth);
        model.addAttribute("cart", cartService.getCart(user));
        return "checkout";
    }

   @PostMapping("/place")
public String placeOrder(@RequestParam String address, Authentication auth) {

    User user = getUser(auth);

    Order order = orderService.createOrder(user, address);

    return "redirect:/payment/" + order.getId();
}


    // ORDER SUCCESS PAGE
    @GetMapping("/success/{id}")
    public String success(@PathVariable Long id, Model model) {

        Order order = orderService.getOrder(id);
        model.addAttribute("order", order);

        return "order-success";
    }

    @GetMapping("/my-orders")
public String myOrders(Authentication auth, Model model) {
    User user = getUser(auth);
    model.addAttribute("orders", orderService.getOrdersByUser(user));
    return "order-list";
}
}
