package com.baosong.supplyme.repository;

import com.baosong.supplyme.domain.Department;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("select department from Department department where department.creationUser.login = ?#{principal.username}")
    List<Department> findByCreationUserIsCurrentUser();

    @Query("select department from Department department where department.headUser.login = ?#{principal.username}")
    List<Department> findByHeadUserIsCurrentUser();

}
