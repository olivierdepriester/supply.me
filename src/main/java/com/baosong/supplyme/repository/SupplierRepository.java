package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.Supplier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Supplier entity.
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Supplier getByReferenceNumber(String referenceNumber);
}
