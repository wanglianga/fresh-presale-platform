package com.fresh.repository;

import com.fresh.entity.PresaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PresaleOrderRepository extends JpaRepository<PresaleOrder, Long> {
    List<PresaleOrder> findByLeaderId(Long leaderId);
    List<PresaleOrder> findByCommunityId(Long communityId);
    List<PresaleOrder> findByPresaleBatchId(Long presaleBatchId);
    PresaleOrder findByOrderNo(String orderNo);
}
