package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.CommentUserInteraction;
import com.manager.socialmediaapplication.model.projection.EndUserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentUserInteractionRepository extends JpaRepository<CommentUserInteraction, Long> {
    @Query(value =
            "SELECT u.id AS id, u.name AS name, u.email AS email FROM comment_user_interaction cui " +
                    "LEFT JOIN end_user u ON cui.end_user_id = u.id " +
                    "WHERE cui.comment_id = :comment_id AND cui.is_liked AND cui.is_active",
            nativeQuery=true)
    Page<EndUserProjection> getUserLikeInteractionByCommentId(@Param("comment_id") long commentId, Pageable pageable);

    @Query(value =
            "SELECT u.id AS id, u.name AS name, u.email AS email FROM comment_user_interaction cui " +
                    "LEFT JOIN end_user u ON cui.end_user_id = u.id " +
                    "WHERE cui.comment_id = :comment_id AND NOT cui.is_liked AND cui.is_active",
            nativeQuery=true)
    Page<EndUserProjection> getUserDislikeInteractionByCommentId(@Param("comment_id") long commentId, Pageable pageable);

    Optional<CommentUserInteraction> findByEndUser_IdAndIsActiveAndComment_Id(Long userId, boolean b, Long commentId);
}
