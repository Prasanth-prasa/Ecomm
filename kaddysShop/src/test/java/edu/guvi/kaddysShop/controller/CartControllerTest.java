package edu.guvi.kaddysShop.controller;

import edu.guvi.kaddysShop.Model.User;
import edu.guvi.kaddysShop.Model.Cart;
import edu.guvi.kaddysShop.Service.CartService;
import edu.guvi.kaddysShop.Service.UserService;
import edu.guvi.kaddysShop.Controller.CartController;

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
class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {

        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(cartController)
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
    void testViewCart() throws Exception {
        mockAuth();

        Cart fakeCart = new Cart(); // FIX
        when(cartService.getCart(any())).thenReturn(fakeCart);

        mockMvc.perform(get("/cart").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("cart"));

        verify(cartService).getCart(any());
    }

   
    @Test
    void testAddToCart() throws Exception {
        mockAuth();

        mockMvc.perform(get("/cart/add/1").principal(authentication))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).addToCart(any(), eq(1L));
    }

   
    @Test
    void testDeleteItem() throws Exception {
        mockAuth();

        mockMvc.perform(get("/cart/delete/1").principal(authentication))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).removeFromCart(any(), eq(1L));
    }

  
    @Test
    void testUpdateQty() throws Exception {
        mockAuth();

        mockMvc.perform(
                post("/cart/update/1")
                        .param("qty", "3")
                        .principal(authentication)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).updateQuantity(any(), eq(1L), eq(3));
    }
}
