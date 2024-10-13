package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
    Optional<CommentReaction> findByComment_Id(Long commentId);
}
