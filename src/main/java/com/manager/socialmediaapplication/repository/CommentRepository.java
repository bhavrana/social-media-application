package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.Comment;
import com.manager.socialmediaapplication.model.projection.CommentProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value =
            "SELECT c.id AS id, c.content AS content, c.created_date AS createdDate, u.name AS endUserName, COALESCE(cr.like_count, 0) AS likeCount, COALESCE(cr.dislike_count, 0) AS dislikeCount FROM comment c " +
                    "LEFT JOIN end_user u ON c.end_user_id = u.id " +
                    "LEFT JOIN comment_reaction cr ON cr.comment_id = c.id " +
                    "WHERE c.parent_id = :parent_id",
            nativeQuery=true)
    Page<CommentProjection> findCommentsByCommentParentId(@Param("parent_id") long parentId, Pageable pageable);

    @Query(value =
            "SELECT c.id AS id, c.content AS content, c.created_date AS createdDate, u.name AS endUserName, COALESCE(cr.like_count, 0) AS likeCount, COALESCE(cr.dislike_count, 0) AS dislikeCount FROM comment c " +
                    "LEFT JOIN end_user u ON c.end_user_id = u.id " +
                    "LEFT JOIN comment_reaction cr ON cr.comment_id = c.id " +
                    "WHERE c.post_id = :post_id AND c.parent_id IS NULL ",
            nativeQuery=true)
    Page<CommentProjection> getCommentsByPost(@Param("post_id") long postId, Pageable pageable);

    @Query(value =
            "SELECT c.id AS id, c.content AS content, c.created_date AS createdDate, u.name AS endUserName, COALESCE(cr.like_count, 0) AS likeCount, COALESCE(cr.dislike_count, 0) AS dislikeCount FROM comment c " +
                    "LEFT JOIN end_user u ON c.end_user_id = u.id " +
                    "LEFT JOIN comment_reaction cr ON cr.comment_id = c.id " +
                    "WHERE c.id = :comment_id ",
            nativeQuery=true)
    CommentProjection findCommentByCommentId(@Param("comment_id") long commentId);

    @Modifying
    @Transactional
    @Query(value =
            "UPDATE comment c set c.content = :updatedContent WHERE c.id = :commentId",
            nativeQuery=true)
    void updateCommentById(@Param("commentId") Long commentId, @Param("updatedContent") String updatedContent);
}
