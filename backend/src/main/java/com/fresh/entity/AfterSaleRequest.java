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
@Table(name = "after_sale_request")
public class AfterSaleRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String requestNo;

    @Column(nullable = false)
    private Long orderId;

    @Column(length = 50)
    private String orderNo;

    @Column(nullable = false, length = 50)
    private String requestType;

    @Column(nullable = false, length = 50)
    private String source;

    @Column(precision = 10, scale = 2)
    private BigDecimal requestAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal approvedAmount;

    @Column(length = 50)
    private String status;

    @Column(length = 500)
    private String description;

    @Column(length = 500)
    private String imageUrls;

    @Column(length = 500)
    private String handleRemark;

    private LocalDateTime handleTime;

    @OneToMany(mappedBy = "afterSaleRequestId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AfterSaleItem> items = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
