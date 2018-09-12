package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.DeliveryNoteLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DeliveryNoteLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryNoteLineRepository extends JpaRepository<DeliveryNoteLine, Long> {

}
