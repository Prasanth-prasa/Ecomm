package edu.guvi.kaddysShop.controller;

import edu.guvi.kaddysShop.Controller.OrderController;
import edu.guvi.kaddysShop.Model.Order;
import edu.guvi.kaddysShop.Model.User;
import edu.guvi.kaddysShop.Model.Cart;
import edu.guvi.kaddysShop.Service.OrderService;
import edu.guvi.kaddysShop.Service.UserService;
import edu.guvi.kaddysShop.Service.CartService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {

        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .setViewResolvers(resolver)
                .build();
    }

    private User mockUser() {
        User u = new User();
        u.setId(1L);
        u.setEmail("test@gmail.com");
        return u;
    }

    private void mockAuth() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@gmail.com");
        when(userService.findByEmail("test@gmail.com")).thenReturn(mockUser());
    }

  
    @Test
    void testCheckoutPage() throws Exception {
        mockAuth();

        Cart fakeCart = new Cart();
        when(cartService.getCart(any())).thenReturn(fakeCart);

        mockMvc.perform(get("/order/checkout").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout"))
                .andExpect(model().attributeExists("cart"));

        verify(cartService).getCart(any());
    }

    @Test
    void testPlaceOrder() throws Exception {
        mockAuth();

        Order order = new Order();
        order.setId(10L);

        when(orderService.createOrder(any(), anyString())).thenReturn(order);

        mockMvc.perform(post("/order/place")
                        .param("address", "Chennai Main Road")
                        .principal(authentication))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/10"));

        verify(orderService).createOrder(any(), eq("Chennai Main Road"));
    }

    
    @Test
    void testOrderSuccessPage() throws Exception {

        Order order = new Order();
        order.setId(5L);

        when(orderService.getOrder(5L)).thenReturn(order);

        mockMvc.perform(get("/order/success/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-success"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("order", order));

        verify(orderService).getOrder(5L);
    }
}
