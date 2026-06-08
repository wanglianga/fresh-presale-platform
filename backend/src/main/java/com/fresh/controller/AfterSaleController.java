package com.fresh.controller;

import com.fresh.common.Result;
import com.fresh.entity.AfterSaleRequest;
import com.fresh.entity.CompensationRecord;
import com.fresh.entity.RefundRecord;
import com.fresh.service.AfterSaleService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/aftersale")
public class AfterSaleController {

    private final AfterSaleService afterSaleService;

    public AfterSaleController(AfterSaleService afterSaleService) {
        this.afterSaleService = afterSaleService;
    }

    @GetMapping("/requests")
    public Result<List<AfterSaleRequest>> listRequests(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        if (status != null) {
            return Result.success(afterSaleService.listRequestsByStatus(status));
        }
        if (type != null) {
            return Result.success(afterSaleService.listRequestsByType(type));
        }
        return Result.success(afterSaleService.listAllRequests());
    }

    @GetMapping("/requests/{id}")
    public Result<AfterSaleRequest> getRequestById(@PathVariable Long id) {
        return Result.success(afterSaleService.getRequestById(id));
    }

    @PostMapping("/requests")
    public Result<AfterSaleRequest> createRequest(@RequestBody AfterSaleRequest request) {
        return Result.success(afterSaleService.createRequest(request));
    }

    public static class ApproveRequest {
        private BigDecimal approvedAmount;
        private String remark;

        public BigDecimal getApprovedAmount() { return approvedAmount; }
        public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }
        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }
    }

    @PutMapping("/requests/{id}/approve")
    public Result<AfterSaleRequest> approveRequest(@PathVariable Long id, @RequestBody ApproveRequest request) {
        return Result.success(afterSaleService.approveRequest(id, request.getApprovedAmount(), request.getRemark()));
    }

    public static class RejectRequest {
        private String remark;

        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }
    }

    @PutMapping("/requests/{id}/reject")
    public Result<AfterSaleRequest> rejectRequest(@PathVariable Long id, @RequestBody RejectRequest request) {
        return Result.success(afterSaleService.rejectRequest(id, request.getRemark()));
    }

    @GetMapping("/refunds")
    public Result<List<RefundRecord>> listRefunds(@RequestParam(required = false) Long orderId) {
        if (orderId != null) {
            return Result.success(afterSaleService.listRefundsByOrder(orderId));
        }
        return Result.success(afterSaleService.listAllRefunds());
    }

    @PutMapping("/refunds/{id}/complete")
    public Result<RefundRecord> completeRefund(@PathVariable Long id) {
        return Result.success(afterSaleService.completeRefund(id));
    }

    @GetMapping("/compensations")
    public Result<List<CompensationRecord>> listCompensations() {
        return Result.success(afterSaleService.listAllCompensations());
    }

    @PostMapping("/compensations")
    public Result<CompensationRecord> createCompensation(@RequestBody CompensationRecord record) {
        return Result.success(afterSaleService.createCompensation(record));
    }

    @PutMapping("/compensations/{id}/complete")
    public Result<CompensationRecord> completeCompensation(@PathVariable Long id) {
        return Result.success(afterSaleService.completeCompensation(id));
    }
}
