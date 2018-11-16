package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.MaterialAvailability;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MaterialAvailability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialAvailabilityRepository extends JpaRepository<MaterialAvailability, Long> {

}
