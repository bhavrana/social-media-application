package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Optional<PostReaction> findByPost_Id(Long postId);
}
