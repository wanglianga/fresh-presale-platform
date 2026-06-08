package com.fresh.repository;

import com.fresh.entity.SortingDiscrepancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SortingDiscrepancyRepository extends JpaRepository<SortingDiscrepancy, Long> {
    List<SortingDiscrepancy> findBySortingRecordId(Long sortingRecordId);
    List<SortingDiscrepancy> findByDiscrepancyType(String discrepancyType);
    List<SortingDiscrepancy> findByStatus(String status);
}
