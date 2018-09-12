package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.MutableProperties;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MutableProperties entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MutablePropertiesRepository extends JpaRepository<MutableProperties, Long> {

}
