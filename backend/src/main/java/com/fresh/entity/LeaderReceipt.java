package com.fresh.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public Long getSortingRecordId() {
        return sortingRecordId;
    }

    public void setSortingRecordId(Long sortingRecordId) {
        this.sortingRecordId = sortingRecordId;
    }

    public String getSortingNo() {
        return sortingNo;
    }

    public void setSortingNo(String sortingNo) {
        this.sortingNo = sortingNo;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
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

    public LocalDateTime getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(LocalDateTime receiptTime) {
        this.receiptTime = receiptTime;
    }

    public LocalDateTime getPickupNotifyTime() {
        return pickupNotifyTime;
    }

    public void setPickupNotifyTime(LocalDateTime pickupNotifyTime) {
        this.pickupNotifyTime = pickupNotifyTime;
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
