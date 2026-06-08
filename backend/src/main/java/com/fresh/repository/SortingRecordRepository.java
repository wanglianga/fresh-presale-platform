package com.fresh.repository;

import com.fresh.entity.SortingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SortingRecordRepository extends JpaRepository<SortingRecord, Long> {
    List<SortingRecord> findByPurchaseBatchId(Long purchaseBatchId);
    List<SortingRecord> findByCommunityId(Long communityId);
    List<SortingRecord> findByStatus(String status);
}
