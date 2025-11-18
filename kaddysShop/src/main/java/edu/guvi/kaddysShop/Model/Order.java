package edu.guvi.kaddysShop.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // USER WHO PLACED THE ORDER
    @ManyToOne
    private User user;

    // ORDER ITEMS
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    private double totalAmount;

    private String address;

    private String status; // CREATED, PAID, SHIPPED, DELIVERED

    private LocalDateTime createdAt;
}
