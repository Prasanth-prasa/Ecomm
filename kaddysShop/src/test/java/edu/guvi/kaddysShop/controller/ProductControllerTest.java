package edu.guvi.kaddysShop.controller;

import edu.guvi.kaddysShop.Model.Product;
import edu.guvi.kaddysShop.Service.ProductService;
import edu.guvi.kaddysShop.Controller.ProductController;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    private void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

   
    @Test
    void testGetAllProductsPage() throws Exception {
        setup();

        Product p1 = new Product(); p1.setName("A");
        Product p2 = new Product(); p2.setName("B");

        when(productService.findAll()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-list"))
                .andExpect(model().attributeExists("products"));

        verify(productService, times(1)).findAll();
    }

   
    @Test
    void testGetProductDetail() throws Exception {
        setup();

        Product p = new Product();
        p.setId(1L);
        p.setName("Phone");

        when(productService.findById(1L)).thenReturn(p);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-detail"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", p));

        verify(productService, times(1)).findById(1L);
    }
}
