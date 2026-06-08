package com.fresh.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sorting_discrepancy")
public class SortingDiscrepancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sortingRecordId;

    @Column(nullable = false)
    private Long sortingItemId;

    @Column(nullable = false)
    private Long productId;

    @Column(length = 100)
    private String productName;

    @Column(nullable = false, length = 50)
    private String discrepancyType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal weightDiff;

    private Long replacedProductId;

    @Column(length = 100)
    private String replacedProductName;

    private Integer replacedQuantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal compensationAmount;

    @Column(length = 50)
    private String status;

    @Column(length = 500)
    private String remark;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
