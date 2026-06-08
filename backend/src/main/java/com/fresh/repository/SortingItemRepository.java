package com.fresh.repository;

import com.fresh.entity.SortingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SortingItemRepository extends JpaRepository<SortingItem, Long> {
    List<SortingItem> findBySortingRecordId(Long sortingRecordId);
}
