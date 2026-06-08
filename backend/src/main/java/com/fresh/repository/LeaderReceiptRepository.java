package com.fresh.repository;

import com.fresh.entity.LeaderReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderReceiptRepository extends JpaRepository<LeaderReceipt, Long> {
    List<LeaderReceipt> findByLeaderId(Long leaderId);
    List<LeaderReceipt> findByStatus(String status);
}
