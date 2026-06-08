package com.fresh.controller;

import com.fresh.common.Result;
import com.fresh.entity.PresaleOrder;
import com.fresh.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Result<List<PresaleOrder>> list(
            @RequestParam(required = false) Long leaderId,
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) Long presaleBatchId) {
        if (leaderId != null) {
            return Result.success(orderService.listByLeader(leaderId));
        }
        if (communityId != null) {
            return Result.success(orderService.listByCommunity(communityId));
        }
        if (presaleBatchId != null) {
            return Result.success(orderService.listByPresaleBatch(presaleBatchId));
        }
        return Result.success(orderService.listAll());
    }

    @GetMapping("/{id}")
    public Result<PresaleOrder> getById(@PathVariable Long id) {
        return Result.success(orderService.getById(id));
    }

    @PostMapping
    public Result<PresaleOrder> create(@RequestBody PresaleOrder order) {
        return Result.success(orderService.create(order));
    }

    @PutMapping("/{id}/status")
    public Result<PresaleOrder> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return Result.success(orderService.updateStatus(id, status));
    }
}
