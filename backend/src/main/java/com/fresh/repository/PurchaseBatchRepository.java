package com.fresh.repository;

import com.fresh.entity.PurchaseBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseBatchRepository extends JpaRepository<PurchaseBatch, Long> {
    List<PurchaseBatch> findBySupplierId(Long supplierId);
    List<PurchaseBatch> findByPresaleBatchId(Long presaleBatchId);
    List<PurchaseBatch> findByStatus(String status);
}
