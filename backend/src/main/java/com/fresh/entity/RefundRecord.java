package com.fresh.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "refund_record")
public class RefundRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String refundNo;

    @Column(nullable = false)
    private Long orderId;

    @Column(length = 50)
    private String orderNo;

    private Long afterSaleRequestId;

    @Column(length = 50)
    private String requestNo;

    private Long discrepancyId;

    @Column(nullable = false, length = 50)
    private String refundType;

    @Column(nullable = false, length = 50)
    private String reason;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal refundAmount;

    @Column(length = 50)
    private String status;

    @Column(length = 500)
    private String remark;

    private LocalDateTime refundTime;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
