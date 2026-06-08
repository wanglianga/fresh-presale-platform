package com.fresh.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "leader_receipt")
public class LeaderReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String receiptNo;

    @Column(nullable = false)
    private Long sortingRecordId;

    @Column(length = 50)
    private String sortingNo;

    @Column(nullable = false)
    private Long leaderId;

    @Column(length = 50)
    private String leaderName;

    @Column(nullable = false)
    private Long communityId;

    @Column(length = 100)
    private String communityName;

    @Column(length = 50)
    private String status;

    @Column(length = 500)
    private String remark;

    private LocalDateTime receiptTime;

    private LocalDateTime pickupNotifyTime;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
