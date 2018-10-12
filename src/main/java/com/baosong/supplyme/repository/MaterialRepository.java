package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.Material;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Material entity.
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    // Material findFirstByPartNumberAndNotId(String partNumber, @Nullable Long id);
}
