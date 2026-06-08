package com.fresh.repository;

import com.fresh.entity.PickupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickupRecordRepository extends JpaRepository<PickupRecord, Long> {
    List<PickupRecord> findByOrderId(Long orderId);
}
