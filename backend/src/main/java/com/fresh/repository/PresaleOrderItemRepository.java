package com.fresh.repository;

import com.fresh.entity.PresaleOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PresaleOrderItemRepository extends JpaRepository<PresaleOrderItem, Long> {
    List<PresaleOrderItem> findByOrderId(Long orderId);
}
