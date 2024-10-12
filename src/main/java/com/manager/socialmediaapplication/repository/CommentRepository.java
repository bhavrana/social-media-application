package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
