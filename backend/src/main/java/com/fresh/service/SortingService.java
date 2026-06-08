package com.fresh.service;

import com.fresh.entity.*;
import com.fresh.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SortingService {

    private final SortingRecordRepository sortingRecordRepository;
    private final SortingItemRepository sortingItemRepository;
    private final SortingDiscrepancyRepository sortingDiscrepancyRepository;
    private final PurchaseBatchRepository purchaseBatchRepository;
    private final CommunityRepository communityRepository;

    public List<SortingRecord> listAll() {
        return sortingRecordRepository.findAll();
    }

    public List<SortingRecord> listByPurchaseBatch(Long purchaseBatchId) {
        return sortingRecordRepository.findByPurchaseBatchId(purchaseBatchId);
    }

    public List<SortingRecord> listByCommunity(Long communityId) {
        return sortingRecordRepository.findByCommunityId(communityId);
    }

    public SortingRecord getById(Long id) {
        return sortingRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("分拣记录不存在: " + id));
    }

    @Transactional
    public SortingRecord create(SortingRecord record) {
        record.setSortingNo("SR" + System.currentTimeMillis());
        record.setStatus("PENDING");

        if (record.getPurchaseBatchId() != null) {
            purchaseBatchRepository.findById(record.getPurchaseBatchId()).ifPresent(pb -> {
                record.setPurchaseBatchNo(pb.getBatchNo());
            });
        }
        if (record.getCommunityId() != null) {
            communityRepository.findById(record.getCommunityId()).ifPresent(c -> {
                record.setCommunityName(c.getName());
            });
        }

        SortingRecord saved = sortingRecordRepository.save(record);
        if (record.getItems() != null) {
            for (SortingItem item : record.getItems()) {
                item.setSortingRecordId(saved.getId());
                if (item.getStatus() == null) {
                    item.setStatus("PENDING");
                }
            }
        }
        return sortingRecordRepository.save(saved);
    }

    @Transactional
    public SortingRecord completeSorting(Long id, List<SortingItem> items, List<SortingDiscrepancy> discrepancies) {
        SortingRecord record = getById(id);
        record.setStatus("COMPLETED");
        record.setSortingTime(LocalDateTime.now());

        if (items != null && record.getItems() != null) {
            for (int i = 0; i < record.getItems().size() && i < items.size(); i++) {
                SortingItem src = items.get(i);
                SortingItem tgt = record.getItems().get(i);
                tgt.setActualQuantity(src.getActualQuantity());
                tgt.setActualWeight(src.getActualWeight());
                tgt.setStatus(src.getActualQuantity() >= tgt.getExpectedQuantity() ? "NORMAL" : "DISCREPANCY");
                tgt.setRemark(src.getRemark());
            }
        }

        if (discrepancies != null) {
            for (SortingDiscrepancy d : discrepancies) {
                d.setSortingRecordId(record.getId());
                d.setStatus("PENDING");
                sortingDiscrepancyRepository.save(d);
            }
        }

        return sortingRecordRepository.save(record);
    }

    public List<SortingDiscrepancy> listDiscrepanciesBySortingRecord(Long sortingRecordId) {
        return sortingDiscrepancyRepository.findBySortingRecordId(sortingRecordId);
    }

    public List<SortingDiscrepancy> listAllDiscrepancies() {
        return sortingDiscrepancyRepository.findAll();
    }

    public List<SortingDiscrepancy> listDiscrepanciesByType(String type) {
        return sortingDiscrepancyRepository.findByDiscrepancyType(type);
    }

    @Transactional
    public SortingDiscrepancy handleDiscrepancy(Long id, SortingDiscrepancy update) {
        SortingDiscrepancy d = sortingDiscrepancyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("差异记录不存在: " + id));
        d.setStatus(update.getStatus());
        d.setCompensationAmount(update.getCompensationAmount());
        d.setRemark(update.getRemark());
        return sortingDiscrepancyRepository.save(d);
    }
}
