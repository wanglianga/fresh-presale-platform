package com.fresh.repository;

import com.fresh.entity.AfterSaleRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AfterSaleRequestRepository extends JpaRepository<AfterSaleRequest, Long> {
    List<AfterSaleRequest> findByOrderId(Long orderId);
    List<AfterSaleRequest> findByStatus(String status);
    List<AfterSaleRequest> findByRequestType(String requestType);
}
