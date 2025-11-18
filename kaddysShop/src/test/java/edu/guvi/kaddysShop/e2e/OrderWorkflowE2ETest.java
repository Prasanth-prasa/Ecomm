package edu.guvi.kaddysShop.e2e;

import edu.guvi.kaddysShop.Model.Cart;
import edu.guvi.kaddysShop.Model.Order;
import edu.guvi.kaddysShop.Model.User;

import edu.guvi.kaddysShop.Service.CartService;
import edu.guvi.kaddysShop.Service.OrderService;
import edu.guvi.kaddysShop.Service.UserService;

import edu.guvi.kaddysShop.Controller.OrderController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderWorkflowE2ETest {

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    
    private User mockUser;
    private Cart fakeCart;

    @BeforeEach
    void setup() {

       
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("classpath:/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .setViewResolvers(viewResolver)
                .build();

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@gmail.com");

        fakeCart = new Cart();
        fakeCart.setId(10L);
    }

   
    @Test
    @WithMockUser(username = "test@gmail.com")
    void testFullCheckoutFlow() throws Exception {

       
        Authentication auth = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        when(auth.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@gmail.com");
        when(userService.findByEmail("test@gmail.com")).thenReturn(mockUser);

        
        when(cartService.getCart(any())).thenReturn(fakeCart);

        mockMvc.perform(
                get("/order/checkout")
                        .principal(auth)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("checkout"))
                .andExpect(model().attributeExists("cart"));

       
        Order order = new Order();
        order.setId(300L);

        when(orderService.createOrder(any(), anyString())).thenReturn(order);

        mockMvc.perform(
                post("/order/place")
                        .principal(auth)
                        .param("address", "Chennai Main Road")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/300"));

        when(orderService.getOrder(300L)).thenReturn(order);

        mockMvc.perform(
                get("/order/success/300")
                        .principal(auth)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("order-success"))
                .andExpect(model().attributeExists("order"));
    }
}
