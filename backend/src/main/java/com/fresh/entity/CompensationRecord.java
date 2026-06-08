package com.fresh.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compensation_record")
public class CompensationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String compensationNo;

    @Column(nullable = false)
    private Long orderId;

    @Column(length = 50)
    private String orderNo;

    private Long afterSaleRequestId;

    private Long discrepancyId;

    @Column(nullable = false, length = 50)
    private String compensationType;

    @Column(nullable = false, length = 50)
    private String reason;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal compensationAmount;

    @Column(length = 50)
    private String status;

    @Column(length = 500)
    private String remark;

    private LocalDateTime compensateTime;

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

    public String getCompensationNo() {
        return compensationNo;
    }

    public void setCompensationNo(String compensationNo) {
        this.compensationNo = compensationNo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getAfterSaleRequestId() {
        return afterSaleRequestId;
    }

    public void setAfterSaleRequestId(Long afterSaleRequestId) {
        this.afterSaleRequestId = afterSaleRequestId;
    }

    public Long getDiscrepancyId() {
        return discrepancyId;
    }

    public void setDiscrepancyId(Long discrepancyId) {
        this.discrepancyId = discrepancyId;
    }

    public String getCompensationType() {
        return compensationType;
    }

    public void setCompensationType(String compensationType) {
        this.compensationType = compensationType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getCompensationAmount() {
        return compensationAmount;
    }

    public void setCompensationAmount(BigDecimal compensationAmount) {
        this.compensationAmount = compensationAmount;
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

    public LocalDateTime getCompensateTime() {
        return compensateTime;
    }

    public void setCompensateTime(LocalDateTime compensateTime) {
        this.compensateTime = compensateTime;
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
