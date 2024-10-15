package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
    Optional<CommentReaction> findByComment_Id(Long commentId);
}
