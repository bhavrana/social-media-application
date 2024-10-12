package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<EndUser, Long> {
}
