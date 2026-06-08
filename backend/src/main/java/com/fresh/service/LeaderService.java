package com.fresh.service;

import com.fresh.entity.*;
import com.fresh.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaderService {

    private final LeaderRepository leaderRepository;
    private final CommunityRepository communityRepository;
    private final LeaderReceiptRepository leaderReceiptRepository;
    private final PickupRecordRepository pickupRecordRepository;
    private final SortingRecordRepository sortingRecordRepository;

    public LeaderService(LeaderRepository leaderRepository, CommunityRepository communityRepository, LeaderReceiptRepository leaderReceiptRepository, PickupRecordRepository pickupRecordRepository, SortingRecordRepository sortingRecordRepository) {
        this.leaderRepository = leaderRepository;
        this.communityRepository = communityRepository;
        this.leaderReceiptRepository = leaderReceiptRepository;
        this.pickupRecordRepository = pickupRecordRepository;
        this.sortingRecordRepository = sortingRecordRepository;
    }

    public List<Leader> listLeaders() {
        return leaderRepository.findAll();
    }

    public Leader createLeader(Leader leader) {
        return leaderRepository.save(leader);
    }

    public List<Community> listCommunities() {
        return communityRepository.findAll();
    }

    public Community createCommunity(Community community) {
        if (community.getLeaderId() != null) {
            leaderRepository.findById(community.getLeaderId()).ifPresent(l -> {
                community.setLeaderName(l.getName());
            });
        }
        return communityRepository.save(community);
    }

    public List<LeaderReceipt> listReceiptsByLeader(Long leaderId) {
        return leaderReceiptRepository.findByLeaderId(leaderId);
    }

    public List<LeaderReceipt> listAllReceipts() {
        return leaderReceiptRepository.findAll();
    }

    public LeaderReceipt getReceiptById(Long id) {
        return leaderReceiptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("签收记录不存在: " + id));
    }

    @Transactional
    public LeaderReceipt createReceipt(LeaderReceipt receipt) {
        receipt.setReceiptNo("LR" + System.currentTimeMillis());
        receipt.setStatus("PENDING");

        if (receipt.getSortingRecordId() != null) {
            sortingRecordRepository.findById(receipt.getSortingRecordId()).ifPresent(sr -> {
                receipt.setSortingNo(sr.getSortingNo());
                receipt.setCommunityId(sr.getCommunityId());
                receipt.setCommunityName(sr.getCommunityName());
            });
        }
        return leaderReceiptRepository.save(receipt);
    }

    @Transactional
    public LeaderReceipt confirmReceipt(Long id, String remark) {
        LeaderReceipt receipt = getReceiptById(id);
        receipt.setStatus("RECEIVED");
        receipt.setReceiptTime(LocalDateTime.now());
        receipt.setRemark(remark);
        return leaderReceiptRepository.save(receipt);
    }

    @Transactional
    public LeaderReceipt notifyPickup(Long id) {
        LeaderReceipt receipt = getReceiptById(id);
        receipt.setStatus("NOTIFIED");
        receipt.setPickupNotifyTime(LocalDateTime.now());
        return leaderReceiptRepository.save(receipt);
    }

    public List<PickupRecord> listPickupRecords() {
        return pickupRecordRepository.findAll();
    }

    @Transactional
    public PickupRecord confirmPickup(PickupRecord record) {
        record.setStatus("PICKED_UP");
        record.setPickupTime(LocalDateTime.now());
        return pickupRecordRepository.save(record);
    }
}
