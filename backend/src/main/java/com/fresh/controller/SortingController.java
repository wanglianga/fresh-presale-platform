package com.fresh.controller;

import com.fresh.common.Result;
import com.fresh.entity.SortingDiscrepancy;
import com.fresh.entity.SortingItem;
import com.fresh.entity.SortingRecord;
import com.fresh.service.SortingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sorting")
public class SortingController {

    private final SortingService sortingService;

    public SortingController(SortingService sortingService) {
        this.sortingService = sortingService;
    }

    @GetMapping("/records")
    public Result<List<SortingRecord>> listRecords(
            @RequestParam(required = false) Long purchaseBatchId,
            @RequestParam(required = false) Long communityId) {
        if (purchaseBatchId != null) {
            return Result.success(sortingService.listByPurchaseBatch(purchaseBatchId));
        }
        if (communityId != null) {
            return Result.success(sortingService.listByCommunity(communityId));
        }
        return Result.success(sortingService.listAll());
    }

    @GetMapping("/records/{id}")
    public Result<SortingRecord> getRecordById(@PathVariable Long id) {
        return Result.success(sortingService.getById(id));
    }

    @PostMapping("/records")
    public Result<SortingRecord> createRecord(@RequestBody SortingRecord record) {
        return Result.success(sortingService.create(record));
    }

    public static class CompleteSortingRequest {
        private List<SortingItem> items;
        private List<SortingDiscrepancy> discrepancies;

        public List<SortingItem> getItems() { return items; }
        public void setItems(List<SortingItem> items) { this.items = items; }
        public List<SortingDiscrepancy> getDiscrepancies() { return discrepancies; }
        public void setDiscrepancies(List<SortingDiscrepancy> discrepancies) { this.discrepancies = discrepancies; }
    }

    @PostMapping("/records/{id}/complete")
    public Result<SortingRecord> completeSorting(@PathVariable Long id, @RequestBody CompleteSortingRequest request) {
        return Result.success(sortingService.completeSorting(id, request.getItems(), request.getDiscrepancies()));
    }

    @GetMapping("/discrepancies")
    public Result<List<SortingDiscrepancy>> listDiscrepancies(
            @RequestParam(required = false) Long sortingRecordId,
            @RequestParam(required = false) String type) {
        if (sortingRecordId != null) {
            return Result.success(sortingService.listDiscrepanciesBySortingRecord(sortingRecordId));
        }
        if (type != null) {
            return Result.success(sortingService.listDiscrepanciesByType(type));
        }
        return Result.success(sortingService.listAllDiscrepancies());
    }

    @PutMapping("/discrepancies/{id}")
    public Result<SortingDiscrepancy> handleDiscrepancy(@PathVariable Long id, @RequestBody SortingDiscrepancy update) {
        return Result.success(sortingService.handleDiscrepancy(id, update));
    }
}
