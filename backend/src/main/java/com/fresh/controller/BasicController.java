package com.fresh.controller;

import com.fresh.common.Result;
import com.fresh.entity.*;
import com.fresh.service.BasicService;
import com.fresh.service.LeaderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basic")
public class BasicController {

    private final BasicService basicService;
    private final LeaderService leaderService;

    public BasicController(BasicService basicService, LeaderService leaderService) {
        this.basicService = basicService;
        this.leaderService = leaderService;
    }

    @GetMapping("/presale-batches")
    public Result<List<PresaleBatch>> listPresaleBatches() {
        return Result.success(basicService.listPresaleBatches());
    }

    @PostMapping("/presale-batches")
    public Result<PresaleBatch> createPresaleBatch(@RequestBody PresaleBatch batch) {
        return Result.success(basicService.createPresaleBatch(batch));
    }

    @GetMapping("/suppliers")
    public Result<List<Supplier>> listSuppliers() {
        return Result.success(basicService.listSuppliers());
    }

    @PostMapping("/suppliers")
    public Result<Supplier> createSupplier(@RequestBody Supplier supplier) {
        return Result.success(basicService.createSupplier(supplier));
    }

    @GetMapping("/leaders")
    public Result<List<Leader>> listLeaders() {
        return Result.success(leaderService.listLeaders());
    }

    @PostMapping("/leaders")
    public Result<Leader> createLeader(@RequestBody Leader leader) {
        return Result.success(leaderService.createLeader(leader));
    }

    @GetMapping("/communities")
    public Result<List<Community>> listCommunities() {
        return Result.success(leaderService.listCommunities());
    }

    @PostMapping("/communities")
    public Result<Community> createCommunity(@RequestBody Community community) {
        return Result.success(leaderService.createCommunity(community));
    }

    @GetMapping("/receipts")
    public Result<List<LeaderReceipt>> listReceipts(@RequestParam(required = false) Long leaderId) {
        if (leaderId != null) {
            return Result.success(leaderService.listReceiptsByLeader(leaderId));
        }
        return Result.success(leaderService.listAllReceipts());
    }

    @PostMapping("/receipts")
    public Result<LeaderReceipt> createReceipt(@RequestBody LeaderReceipt receipt) {
        return Result.success(leaderService.createReceipt(receipt));
    }

    @PutMapping("/receipts/{id}/confirm")
    public Result<LeaderReceipt> confirmReceipt(@PathVariable Long id, @RequestParam(required = false) String remark) {
        return Result.success(leaderService.confirmReceipt(id, remark));
    }

    @PutMapping("/receipts/{id}/notify")
    public Result<LeaderReceipt> notifyPickup(@PathVariable Long id) {
        return Result.success(leaderService.notifyPickup(id));
    }

    @GetMapping("/pickups")
    public Result<List<PickupRecord>> listPickups() {
        return Result.success(leaderService.listPickupRecords());
    }

    @PostMapping("/pickups")
    public Result<PickupRecord> confirmPickup(@RequestBody PickupRecord record) {
        return Result.success(leaderService.confirmPickup(record));
    }
}
