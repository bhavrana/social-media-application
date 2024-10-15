package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.EndUser;
import com.manager.socialmediaapplication.model.projection.EndUserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndUserRepository extends JpaRepository<EndUser, Long> {

    Page<EndUserProjection> findAllProjectedBy(Pageable pageable);

    EndUserProjection findEndUserById(Long id);

}
