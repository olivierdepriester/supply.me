package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.Invoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("select invoice from Invoice invoice where invoice.creationUser.login = ?#{principal.username}")
    List<Invoice> findByCreationUserIsCurrentUser();

}
