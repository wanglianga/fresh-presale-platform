package com.fresh.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSortingRecordId() {
        return sortingRecordId;
    }

    public void setSortingRecordId(Long sortingRecordId) {
        this.sortingRecordId = sortingRecordId;
    }

    public Long getSortingItemId() {
        return sortingItemId;
    }

    public void setSortingItemId(Long sortingItemId) {
        this.sortingItemId = sortingItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDiscrepancyType() {
        return discrepancyType;
    }

    public void setDiscrepancyType(String discrepancyType) {
        this.discrepancyType = discrepancyType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getWeightDiff() {
        return weightDiff;
    }

    public void setWeightDiff(BigDecimal weightDiff) {
        this.weightDiff = weightDiff;
    }

    public Long getReplacedProductId() {
        return replacedProductId;
    }

    public void setReplacedProductId(Long replacedProductId) {
        this.replacedProductId = replacedProductId;
    }

    public String getReplacedProductName() {
        return replacedProductName;
    }

    public void setReplacedProductName(String replacedProductName) {
        this.replacedProductName = replacedProductName;
    }

    public Integer getReplacedQuantity() {
        return replacedQuantity;
    }

    public void setReplacedQuantity(Integer replacedQuantity) {
        this.replacedQuantity = replacedQuantity;
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
