package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.PostUserInteraction;
import com.manager.socialmediaapplication.model.projection.EndUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostUserInteractionRepository extends JpaRepository<PostUserInteraction, Long> {
    Optional<PostUserInteraction> findByEndUser_IdAndIsActiveAndPost_Id(Long userId, Boolean isActive, Long post);

    @Query(value =
            "SELECT u.id AS id, u.name AS name, u.email AS email FROM post_user_interaction pui " +
                    "LEFT JOIN end_user u ON pui.end_user_id = u.id " +
                    "WHERE pui.post_id = :post_id AND pui.is_liked AND pui.is_active",
            nativeQuery=true)
    List<EndUserProjection> getUserLikeInteractionByPostId(@Param("post_id") long postId);

    @Query(value =
            "SELECT u.id AS id, u.name AS name, u.email AS email FROM post_user_interaction pui " +
                    "LEFT JOIN end_user u ON pui.end_user_id = u.id " +
                    "WHERE pui.post_id = :post_id AND NOT pui.is_liked AND pui.is_active",
            nativeQuery=true)
    List<EndUserProjection> getUserDislikeInteractionByPostId(@Param("post_id") long postId);

}
