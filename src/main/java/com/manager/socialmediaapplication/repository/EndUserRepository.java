package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.EndUser;
import com.manager.socialmediaapplication.model.projection.EndUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndUserRepository extends JpaRepository<EndUser, Long> {

    List<EndUserProjection> findAllProjectedBy();

    EndUserProjection findEndUserById(Long id);

}
