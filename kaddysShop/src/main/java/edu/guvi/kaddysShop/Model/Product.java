package edu.guvi.kaddysShop.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1500)
    private String description;

    private double price;

    private String image; // Cloudinary image URL
}

