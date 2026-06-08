package com.fresh.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "purchase_batch")
public class PurchaseBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String batchNo;

    @Column(nullable = false)
    private Long presaleBatchId;

    @Column(length = 100)
    private String presaleBatchName;

    @Column(nullable = false)
    private Long supplierId;

    @Column(length = 100)
    private String supplierName;

    @Column(length = 50)
    private String status;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 500)
    private String freshnessCondition;

    @Column(length = 500)
    private String remark;

    private LocalDateTime confirmTime;

    private LocalDateTime shipTime;

    private LocalDateTime arriveTime;

    @OneToMany(mappedBy = "purchaseBatchId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseBatchItem> items = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
