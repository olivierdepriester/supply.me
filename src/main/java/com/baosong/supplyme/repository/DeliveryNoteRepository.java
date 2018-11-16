package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.DeliveryNote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the DeliveryNote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote, Long> {

    @Query("select delivery_note from DeliveryNote delivery_note where delivery_note.creationUser.login = ?#{principal.username}")
    List<DeliveryNote> findByCreationUserIsCurrentUser();

}
