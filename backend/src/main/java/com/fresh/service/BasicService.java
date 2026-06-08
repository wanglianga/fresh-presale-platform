package com.fresh.service;

import com.fresh.entity.PresaleBatch;
import com.fresh.entity.Supplier;
import com.fresh.repository.PresaleBatchRepository;
import com.fresh.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicService {

    private final PresaleBatchRepository presaleBatchRepository;
    private final SupplierRepository supplierRepository;

    public List<PresaleBatch> listPresaleBatches() {
        return presaleBatchRepository.findAll();
    }

    public PresaleBatch createPresaleBatch(PresaleBatch batch) {
        if (batch.getStatus() == null) {
            batch.setStatus("ACTIVE");
        }
        return presaleBatchRepository.save(batch);
    }

    public List<Supplier> listSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }
}
