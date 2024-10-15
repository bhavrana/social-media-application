package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Optional<PostReaction> findByPost_Id(Long postId);
}
