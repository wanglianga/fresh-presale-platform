package com.fresh.repository;

import com.fresh.entity.PresaleBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PresaleBatchRepository extends JpaRepository<PresaleBatch, Long> {
    List<PresaleBatch> findByStatus(String status);
}
