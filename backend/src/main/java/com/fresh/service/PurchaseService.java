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
public class PurchaseService {

    private final PurchaseBatchRepository purchaseBatchRepository;
    private final PurchaseBatchItemRepository purchaseBatchItemRepository;
    private final PresaleBatchRepository presaleBatchRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    public List<PurchaseBatch> listAll() {
        return purchaseBatchRepository.findAll();
    }

    public List<PurchaseBatch> listBySupplier(Long supplierId) {
        return purchaseBatchRepository.findBySupplierId(supplierId);
    }

    public List<PurchaseBatch> listByPresaleBatch(Long presaleBatchId) {
        return purchaseBatchRepository.findByPresaleBatchId(presaleBatchId);
    }

    public List<PurchaseBatch> listByStatus(String status) {
        return purchaseBatchRepository.findByStatus(status);
    }

    public PurchaseBatch getById(Long id) {
        return purchaseBatchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("采购批次不存在: " + id));
    }

    @Transactional
    public PurchaseBatch create(PurchaseBatch batch) {
        batch.setBatchNo("PB" + System.currentTimeMillis());
        batch.setStatus("DRAFT");

        if (batch.getPresaleBatchId() != null) {
            presaleBatchRepository.findById(batch.getPresaleBatchId()).ifPresent(pb -> {
                batch.setPresaleBatchName(pb.getBatchName());
            });
        }
        if (batch.getSupplierId() != null) {
            supplierRepository.findById(batch.getSupplierId()).ifPresent(s -> {
                batch.setSupplierName(s.getName());
            });
        }

        BigDecimal total = BigDecimal.ZERO;
        if (batch.getItems() != null) {
            for (PurchaseBatchItem item : batch.getItems()) {
                Product product = productRepository.findById(item.getProductId()).orElse(null);
                if (product != null) {
                    item.setProductName(product.getName());
                }
                if (item.getPlanQuantity() == null) {
                    item.setPlanQuantity(0);
                }
                item.setStatus("PENDING");
                if (item.getPrice() != null && item.getPlanQuantity() != null) {
                    item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getPlanQuantity())));
                    total = total.add(item.getSubtotal());
                }
            }
        }
        batch.setTotalAmount(total);

        PurchaseBatch saved = purchaseBatchRepository.save(batch);
        if (batch.getItems() != null) {
            for (PurchaseBatchItem item : batch.getItems()) {
                item.setPurchaseBatchId(saved.getId());
            }
        }
        return purchaseBatchRepository.save(saved);
    }

    @Transactional
    public PurchaseBatch confirm(Long id, PurchaseBatch updateData) {
        PurchaseBatch batch = getById(id);
        batch.setStatus("CONFIRMED");
        batch.setConfirmTime(LocalDateTime.now());
        batch.setFreshnessCondition(updateData.getFreshnessCondition());

        if (updateData.getItems() != null && batch.getItems() != null) {
            for (int i = 0; i < batch.getItems().size() && i < updateData.getItems().size(); i++) {
                batch.getItems().get(i).setConfirmQuantity(updateData.getItems().get(i).getConfirmQuantity());
                batch.getItems().get(i).setStatus("CONFIRMED");
            }
        }
        return purchaseBatchRepository.save(batch);
    }

    @Transactional
    public PurchaseBatch ship(Long id) {
        PurchaseBatch batch = getById(id);
        batch.setStatus("SHIPPED");
        batch.setShipTime(LocalDateTime.now());
        return purchaseBatchRepository.save(batch);
    }

    @Transactional
    public PurchaseBatch arrive(Long id) {
        PurchaseBatch batch = getById(id);
        batch.setStatus("ARRIVED");
        batch.setArriveTime(LocalDateTime.now());
        if (batch.getItems() != null) {
            for (PurchaseBatchItem item : batch.getItems()) {
                if (item.getActualQuantity() == null) {
                    item.setActualQuantity(item.getConfirmQuantity() != null ? item.getConfirmQuantity() : item.getPlanQuantity());
                }
                item.setStatus("ARRIVED");
            }
        }
        return purchaseBatchRepository.save(batch);
    }
}
