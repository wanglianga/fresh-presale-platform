package com.fresh.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSortingNo() {
        return sortingNo;
    }

    public void setSortingNo(String sortingNo) {
        this.sortingNo = sortingNo;
    }

    public Long getPurchaseBatchId() {
        return purchaseBatchId;
    }

    public void setPurchaseBatchId(Long purchaseBatchId) {
        this.purchaseBatchId = purchaseBatchId;
    }

    public String getPurchaseBatchNo() {
        return purchaseBatchNo;
    }

    public void setPurchaseBatchNo(String purchaseBatchNo) {
        this.purchaseBatchNo = purchaseBatchNo;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getSortingTime() {
        return sortingTime;
    }

    public void setSortingTime(LocalDateTime sortingTime) {
        this.sortingTime = sortingTime;
    }

    public List<SortingItem> getItems() {
        return items;
    }

    public void setItems(List<SortingItem> items) {
        this.items = items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
