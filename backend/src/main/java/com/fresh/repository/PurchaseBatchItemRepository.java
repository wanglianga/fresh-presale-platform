package com.fresh.repository;

import com.fresh.entity.PurchaseBatchItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseBatchItemRepository extends JpaRepository<PurchaseBatchItem, Long> {
    List<PurchaseBatchItem> findByPurchaseBatchId(Long purchaseBatchId);
}
