package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.DemandCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the DemandCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemandCategoryRepository extends JpaRepository<DemandCategory, Long> {

    @Query("select demand_category from DemandCategory demand_category where demand_category.creationUser.login = ?#{principal.username}")
    List<DemandCategory> findByCreationUserIsCurrentUser();

}
