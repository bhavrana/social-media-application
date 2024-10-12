package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.CommentUserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentUserInteractionRepository extends JpaRepository<CommentUserInteraction, Long> {
}
