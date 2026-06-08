package com.fresh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "purchase_batch_item")
public class PurchaseBatchItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long purchaseBatchId;

    @Column(nullable = false)
    private Long productId;

    @Column(length = 100)
    private String productName;

    @Column(nullable = false)
    private Integer planQuantity;

    private Integer confirmQuantity;

    private Integer actualQuantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(length = 50)
    private String status;
}
