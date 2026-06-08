package com.fresh.service;

import com.fresh.entity.*;
import com.fresh.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AfterSaleService {

    private final AfterSaleRequestRepository afterSaleRequestRepository;
    private final AfterSaleItemRepository afterSaleItemRepository;
    private final RefundRecordRepository refundRecordRepository;
    private final CompensationRecordRepository compensationRecordRepository;
    private final PresaleOrderRepository orderRepository;

    public AfterSaleService(AfterSaleRequestRepository afterSaleRequestRepository, AfterSaleItemRepository afterSaleItemRepository, RefundRecordRepository refundRecordRepository, CompensationRecordRepository compensationRecordRepository, PresaleOrderRepository orderRepository) {
        this.afterSaleRequestRepository = afterSaleRequestRepository;
        this.afterSaleItemRepository = afterSaleItemRepository;
        this.refundRecordRepository = refundRecordRepository;
        this.compensationRecordRepository = compensationRecordRepository;
        this.orderRepository = orderRepository;
    }

    public List<AfterSaleRequest> listAllRequests() {
        return afterSaleRequestRepository.findAll();
    }

    public List<AfterSaleRequest> listRequestsByStatus(String status) {
        return afterSaleRequestRepository.findByStatus(status);
    }

    public List<AfterSaleRequest> listRequestsByType(String type) {
        return afterSaleRequestRepository.findByRequestType(type);
    }

    public AfterSaleRequest getRequestById(Long id) {
        return afterSaleRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("售后申请不存在: " + id));
    }

    @Transactional
    public AfterSaleRequest createRequest(AfterSaleRequest request) {
        request.setRequestNo("AS" + System.currentTimeMillis());
        request.setStatus("PENDING");

        PresaleOrder order = orderRepository.findById(request.getOrderId()).orElse(null);
        if (order != null) {
            request.setOrderNo(order.getOrderNo());
        }

        BigDecimal total = BigDecimal.ZERO;
        if (request.getItems() != null) {
            for (AfterSaleItem item : request.getItems()) {
                if (item.getAmount() != null && item.getQuantity() != null) {
                    total = total.add(item.getAmount());
                }
            }
        }
        request.setRequestAmount(total);

        AfterSaleRequest saved = afterSaleRequestRepository.save(request);
        if (request.getItems() != null) {
            for (AfterSaleItem item : request.getItems()) {
                item.setAfterSaleRequestId(saved.getId());
            }
        }
        return afterSaleRequestRepository.save(saved);
    }

    @Transactional
    public AfterSaleRequest approveRequest(Long id, BigDecimal approvedAmount, String remark) {
        AfterSaleRequest request = getRequestById(id);
        request.setStatus("APPROVED");
        request.setApprovedAmount(approvedAmount);
        request.setHandleRemark(remark);
        request.setHandleTime(LocalDateTime.now());

        RefundRecord refund = new RefundRecord();
        refund.setRefundNo("RF" + System.currentTimeMillis());
        refund.setOrderId(request.getOrderId());
        refund.setOrderNo(request.getOrderNo());
        refund.setAfterSaleRequestId(request.getId());
        refund.setRequestNo(request.getRequestNo());
        refund.setRefundType(request.getRequestType());
        refund.setReason(request.getDescription());
        refund.setRefundAmount(approvedAmount);
        refund.setStatus("PENDING");
        refund.setRemark(remark);
        refundRecordRepository.save(refund);

        return afterSaleRequestRepository.save(request);
    }

    @Transactional
    public AfterSaleRequest rejectRequest(Long id, String remark) {
        AfterSaleRequest request = getRequestById(id);
        request.setStatus("REJECTED");
        request.setHandleRemark(remark);
        request.setHandleTime(LocalDateTime.now());
        return afterSaleRequestRepository.save(request);
    }

    public List<RefundRecord> listAllRefunds() {
        return refundRecordRepository.findAll();
    }

    public List<RefundRecord> listRefundsByOrder(Long orderId) {
        return refundRecordRepository.findByOrderId(orderId);
    }

    @Transactional
    public RefundRecord completeRefund(Long id) {
        RefundRecord refund = refundRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("退款记录不存在: " + id));
        refund.setStatus("COMPLETED");
        refund.setRefundTime(LocalDateTime.now());

        PresaleOrder order = orderRepository.findById(refund.getOrderId()).orElse(null);
        if (order != null) {
            order.setRefundAmount(order.getRefundAmount().add(refund.getRefundAmount()));
            orderRepository.save(order);
        }

        return refundRecordRepository.save(refund);
    }

    public List<CompensationRecord> listAllCompensations() {
        return compensationRecordRepository.findAll();
    }

    @Transactional
    public CompensationRecord createCompensation(CompensationRecord record) {
        record.setCompensationNo("CP" + System.currentTimeMillis());
        record.setStatus("PENDING");
        return compensationRecordRepository.save(record);
    }

    @Transactional
    public CompensationRecord completeCompensation(Long id) {
        CompensationRecord record = compensationRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("补偿记录不存在: " + id));
        record.setStatus("COMPLETED");
        record.setCompensateTime(LocalDateTime.now());
        return compensationRecordRepository.save(record);
    }
}
