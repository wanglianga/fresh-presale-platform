package com.fresh.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sorting_item")
public class SortingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sortingRecordId;

    @Column(nullable = false)
    private Long orderItemId;

    @Column(nullable = false)
    private Long productId;

    @Column(length = 100)
    private String productName;

    @Column(nullable = false)
    private Integer expectedQuantity;

    @Column(nullable = false)
    private Integer actualQuantity = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal expectedWeight;

    @Column(precision = 10, scale = 2)
    private BigDecimal actualWeight;

    @Column(length = 50)
    private String status;

    @Column(length = 500)
    private String remark;

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

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
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

    public Integer getExpectedQuantity() {
        return expectedQuantity;
    }

    public void setExpectedQuantity(Integer expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    public Integer getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(Integer actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public BigDecimal getExpectedWeight() {
        return expectedWeight;
    }

    public void setExpectedWeight(BigDecimal expectedWeight) {
        this.expectedWeight = expectedWeight;
    }

    public BigDecimal getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(BigDecimal actualWeight) {
        this.actualWeight = actualWeight;
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
}
