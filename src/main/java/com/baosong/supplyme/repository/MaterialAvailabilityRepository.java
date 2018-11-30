package com.baosong.supplyme.repository;

import java.util.List;

import com.baosong.supplyme.domain.MaterialAvailability;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MaterialAvailability entity.
 */
@Repository
public interface MaterialAvailabilityRepository extends JpaRepository<MaterialAvailability, Long> {
    List<MaterialAvailability> getByMaterialIdOrderByUpdateDateDesc(Long materialId);
}
