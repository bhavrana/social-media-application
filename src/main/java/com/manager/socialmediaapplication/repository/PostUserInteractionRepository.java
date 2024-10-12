package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.PostUserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostUserInteractionRepository extends JpaRepository<PostUserInteraction, Long> {
}
