package com.fresh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "sorting_item")
public class SortingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sortingRecordId;

    @Column(nullable = false)
    private Long orderItemId;

    @Column(nullable = false)
    private Long productId;

    @Column(length = 100)
    private String productName;

    @Column(nullable = false)
    private Integer expectedQuantity;

    @Column(nullable = false)
    private Integer actualQuantity = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal expectedWeight;

    @Column(precision = 10, scale = 2)
    private BigDecimal actualWeight;

    @Column(length = 50)
    private String status;

    @Column(length = 500)
    private String remark;
}
