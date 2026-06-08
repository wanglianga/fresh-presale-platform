package com.fresh.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pickup_record")
public class PickupRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(length = 50)
    private String orderNo;

    @Column(nullable = false)
    private Long receiptId;

    @Column(length = 50)
    private String receiptNo;

    @Column(nullable = false, length = 50)
    private String pickupCode;

    @Column(length = 50)
    private String status;

    private LocalDateTime pickupTime;

    @Column(length = 200)
    private String pickupRemark;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
