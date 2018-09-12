package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the PurchaseOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("select purchase_order from PurchaseOrder purchase_order where purchase_order.creationUser.login = ?#{principal.username}")
    List<PurchaseOrder> findByCreationUserIsCurrentUser();

}
