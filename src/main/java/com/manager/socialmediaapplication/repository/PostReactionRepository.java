package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
}
