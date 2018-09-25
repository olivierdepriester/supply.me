package com.baosong.supplyme.repository;

import java.util.List;

import com.baosong.supplyme.domain.PurchaseOrderLine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PurchaseOrderLine entity.
 */
@Repository
public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine, Long> {
    List<PurchaseOrderLine> findByDemandId(Long demandId);
}
