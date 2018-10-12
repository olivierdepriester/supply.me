package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.MutableProperties;
import com.baosong.supplyme.domain.enumeration.PropertiesKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MutableProperties entity.
 */
@Repository
public interface MutablePropertiesRepository extends JpaRepository<MutableProperties, Long> {
    MutableProperties findByKey(PropertiesKey key);

}
