package com.fresh.controller;

import com.fresh.common.Result;
import com.fresh.entity.PurchaseBatch;
import com.fresh.service.PurchaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public Result<List<PurchaseBatch>> list(
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Long presaleBatchId,
            @RequestParam(required = false) String status) {
        if (supplierId != null) {
            return Result.success(purchaseService.listBySupplier(supplierId));
        }
        if (presaleBatchId != null) {
            return Result.success(purchaseService.listByPresaleBatch(presaleBatchId));
        }
        if (status != null) {
            return Result.success(purchaseService.listByStatus(status));
        }
        return Result.success(purchaseService.listAll());
    }

    @GetMapping("/{id}")
    public Result<PurchaseBatch> getById(@PathVariable Long id) {
        return Result.success(purchaseService.getById(id));
    }

    @PostMapping
    public Result<PurchaseBatch> create(@RequestBody PurchaseBatch batch) {
        return Result.success(purchaseService.create(batch));
    }

    @PutMapping("/{id}/confirm")
    public Result<PurchaseBatch> confirm(@PathVariable Long id, @RequestBody PurchaseBatch updateData) {
        return Result.success(purchaseService.confirm(id, updateData));
    }

    @PutMapping("/{id}/ship")
    public Result<PurchaseBatch> ship(@PathVariable Long id) {
        return Result.success(purchaseService.ship(id));
    }

    @PutMapping("/{id}/arrive")
    public Result<PurchaseBatch> arrive(@PathVariable Long id) {
        return Result.success(purchaseService.arrive(id));
    }
}
