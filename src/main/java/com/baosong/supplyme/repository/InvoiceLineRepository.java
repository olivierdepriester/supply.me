package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.InvoiceLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InvoiceLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {

}
