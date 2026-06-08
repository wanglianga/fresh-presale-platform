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
public class OrderService {

    private final PresaleOrderRepository orderRepository;
    private final PresaleOrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CommunityRepository communityRepository;
    private final LeaderRepository leaderRepository;
    private final PresaleBatchRepository presaleBatchRepository;

    public OrderService(PresaleOrderRepository orderRepository, PresaleOrderItemRepository orderItemRepository, ProductRepository productRepository, CommunityRepository communityRepository, LeaderRepository leaderRepository, PresaleBatchRepository presaleBatchRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.communityRepository = communityRepository;
        this.leaderRepository = leaderRepository;
        this.presaleBatchRepository = presaleBatchRepository;
    }

    public List<PresaleOrder> listAll() {
        return orderRepository.findAll();
    }

    public List<PresaleOrder> listByLeader(Long leaderId) {
        return orderRepository.findByLeaderId(leaderId);
    }

    public List<PresaleOrder> listByCommunity(Long communityId) {
        return orderRepository.findByCommunityId(communityId);
    }

    public List<PresaleOrder> listByPresaleBatch(Long presaleBatchId) {
        return orderRepository.findByPresaleBatchId(presaleBatchId);
    }

    public PresaleOrder getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在: " + id));
    }

    @Transactional
    public PresaleOrder create(PresaleOrder order) {
        order.setOrderNo("SO" + System.currentTimeMillis());
        order.setStatus("PENDING");
        order.setPickupCode(UUID.randomUUID().toString().substring(0, 6).toUpperCase());

        if (order.getCommunityId() != null) {
            Community community = communityRepository.findById(order.getCommunityId()).orElse(null);
            if (community != null) {
                order.setCommunityName(community.getName());
                order.setLeaderId(community.getLeaderId());
                order.setLeaderName(community.getLeaderName());
            }
        }

        BigDecimal total = BigDecimal.ZERO;
        if (order.getItems() != null) {
            for (PresaleOrderItem item : order.getItems()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("商品不存在: " + item.getProductId()));
                item.setProductName(product.getName());
                item.setProductImage(product.getImageUrl());
                if (item.getPrice() == null) {
                    item.setPrice(product.getPrice());
                }
                item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                item.setStatus("PENDING");
                total = total.add(item.getSubtotal());
            }
        }
        order.setTotalAmount(total);
        order.setRefundAmount(BigDecimal.ZERO);

        PresaleOrder saved = orderRepository.save(order);
        if (order.getItems() != null) {
            for (PresaleOrderItem item : order.getItems()) {
                item.setOrderId(saved.getId());
            }
        }
        return orderRepository.save(saved);
    }

    @Transactional
    public PresaleOrder updateStatus(Long id, String status) {
        PresaleOrder order = getById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
