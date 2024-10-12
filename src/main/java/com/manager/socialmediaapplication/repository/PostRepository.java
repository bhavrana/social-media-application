package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
