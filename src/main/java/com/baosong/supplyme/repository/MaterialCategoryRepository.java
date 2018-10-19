package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.MaterialCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the MaterialCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialCategoryRepository extends JpaRepository<MaterialCategory, Long> {

    @Query("select material_category from MaterialCategory material_category where material_category.creationUser.login = ?#{principal.username}")
    List<MaterialCategory> findByCreationUserIsCurrentUser();

}
