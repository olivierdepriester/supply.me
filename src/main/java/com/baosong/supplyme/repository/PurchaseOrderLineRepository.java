package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.PurchaseOrderLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PurchaseOrderLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine, Long> {

}
