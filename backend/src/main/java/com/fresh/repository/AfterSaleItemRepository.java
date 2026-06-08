package com.fresh.repository;

import com.fresh.entity.AfterSaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AfterSaleItemRepository extends JpaRepository<AfterSaleItem, Long> {
    List<AfterSaleItem> findByAfterSaleRequestId(Long afterSaleRequestId);
}
