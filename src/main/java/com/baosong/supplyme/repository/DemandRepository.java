package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.Demand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Demand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemandRepository extends JpaRepository<Demand, Long> {

    @Query("select demand from Demand demand where demand.creationUser.login = ?#{principal.username}")
    List<Demand> findByCreationUserIsCurrentUser();

}
