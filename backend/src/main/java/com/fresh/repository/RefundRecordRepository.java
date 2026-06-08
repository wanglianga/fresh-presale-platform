package com.fresh.repository;

import com.fresh.entity.RefundRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundRecordRepository extends JpaRepository<RefundRecord, Long> {
    List<RefundRecord> findByOrderId(Long orderId);
    List<RefundRecord> findByStatus(String status);
}
