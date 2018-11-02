package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.AttachmentFile;
import com.baosong.supplyme.domain.Demand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Demand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentFileRepository extends JpaRepository<AttachmentFile, Long> {
    List<AttachmentFile> findByDemandId(Long id);
}
