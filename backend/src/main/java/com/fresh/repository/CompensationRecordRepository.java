package com.fresh.repository;

import com.fresh.entity.CompensationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompensationRecordRepository extends JpaRepository<CompensationRecord, Long> {
    List<CompensationRecord> findByOrderId(Long orderId);
    List<CompensationRecord> findByStatus(String status);
}
