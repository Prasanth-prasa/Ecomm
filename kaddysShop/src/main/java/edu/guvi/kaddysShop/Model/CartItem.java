package edu.guvi.kaddysShop.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cartitem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private int quantity;

    private double totalPrice;

    @ManyToOne
    private Cart cart;
}
