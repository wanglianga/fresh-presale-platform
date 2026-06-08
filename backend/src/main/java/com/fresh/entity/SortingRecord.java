package com.fresh.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "sorting_record")
public class SortingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String sortingNo;

    @Column(nullable = false)
    private Long purchaseBatchId;

    @Column(length = 50)
    private String purchaseBatchNo;

    @Column(nullable = false)
    private Long communityId;

    @Column(length = 100)
    private String communityName;

    @Column(length = 50)
    private String status;

    @Column(length = 500)
    private String remark;

    private LocalDateTime sortingTime;

    @OneToMany(mappedBy = "sortingRecordId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SortingItem> items = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
